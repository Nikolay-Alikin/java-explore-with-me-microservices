package ru.yandex.practicum.service;

import static ru.yandex.practicum.event.model.QEvent.event;

import client.CollectorClient;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.yandex.practicum.category.client.CategoryClient;
import ru.yandex.practicum.client.model.EndpointHit;
import ru.yandex.practicum.client.model.ViewStats;
import ru.yandex.practicum.constant.UserActionType;
import ru.yandex.practicum.event.model.AdminParameter;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublicParameter;
import ru.yandex.practicum.event.model.dto.CreateEventDto;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.event.model.dto.UpdateEventDto;
import ru.yandex.practicum.exception.type.ConflictException;
import ru.yandex.practicum.exception.type.NotFoundException;
import ru.yandex.practicum.location.LocationClient;
import ru.yandex.practicum.location.model.dto.CreateLocationDto;
import ru.yandex.practicum.location.model.dto.LocationDto;
import ru.yandex.practicum.request.client.RequestClient;
import ru.yandex.practicum.state.State;
import ru.yandex.practicum.storage.EventStorage;
import ru.yandex.practicum.user.client.UserClient;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.model.dto.UserWithoutEmailDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final String SIMPLE_NAME = Event.class.getSimpleName();
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final EventStorage eventStorage;
    private final UserClient userClient;
    private final CategoryClient categoryClient;
    private final LocationClient locationClient;
    private final CollectorClient collectorClient;
    private final RequestClient requestClient;

    @Override
    public List<EventFullDto> getAllByAdmin(final AdminParameter adminParameter) {
        final List<Event> lists = eventStorage.findAll(getSpecification(adminParameter),
                PageRequest.of(adminParameter.getFrom() / adminParameter.getSize(),
                        adminParameter.getSize()));

        lists.forEach(event -> updateStats(event, adminParameter.getRangeStart(), adminParameter.getRangeEnd(), true));

        return lists.stream()
                .map(event -> cs.convert(event, EventFullDto.class))
                .toList();
    }

    private Event update(Event eventInStorage, final UpdateEventDto updateEventDto) {

        if (!ObjectUtils.isEmpty(updateEventDto.participantLimit())) {
            eventInStorage.setParticipantLimit(updateEventDto.participantLimit());
        }

        if (!ObjectUtils.isEmpty(updateEventDto.paid())) {
            eventInStorage.setPaid(updateEventDto.paid());
        }

        if (!ObjectUtils.isEmpty(updateEventDto.annotation())) {
            eventInStorage.setAnnotation(updateEventDto.annotation());
        }

        if (!ObjectUtils.isEmpty(updateEventDto.description())) {
            eventInStorage.setDescription(updateEventDto.description());
        }

        if (!ObjectUtils.isEmpty(updateEventDto.title())) {
            eventInStorage.setTitle(updateEventDto.title());
        }

        if (updateEventDto.category() != 0) {
            var dto = categoryClient.getById(updateEventDto.category());
            eventInStorage.setCategory(dto.id());
        }

        return eventInStorage;
    }

    @Override
    public EventFullDto updateByAdmin(final long eventId, final UpdateEventDto updateEventDto) {
        Event eventInStorage = eventStorage.getByIdOrElseThrow(eventId);

        if (!ObjectUtils.isEmpty(updateEventDto.stateAction())) {
            switch (updateEventDto.stateAction()) {
                case REJECT_EVENT -> {
                    checkEventIsPublished(eventInStorage.getState());
                    eventInStorage.setState(State.CANCELED);
                }

                case PUBLISH_EVENT -> {
                    if (eventInStorage.getState() != State.PENDING) {
                        throw new ConflictException(
                                "An event can only be published if it is in a pending publication state");
                    }
                    if (LocalDateTime.now().plusHours(1).isAfter(eventInStorage.getEventDate())) {
                        throw new ConflictException("The start date of the event being modified must be no earlier "
                                                    + "than an hour before from date of publication");
                    }

                    eventInStorage.setState(State.PUBLISHED);
                    eventInStorage.setPublishedOn(LocalDateTime.now());
                }
            }
        }
        if (!ObjectUtils.isEmpty(updateEventDto.location())) {
            var locationDto = getLocation(updateEventDto.location().lat(),
                    updateEventDto.location().lon());
            eventInStorage.setLocation(locationDto.id());
        }

        return cs.convert(eventStorage.save(update(eventInStorage, updateEventDto)), EventFullDto.class);
    }

    @Override
    public EventFullDto create(final CreateEventDto createEventDto, final long userId) {
        var userDto = Optional.ofNullable(userClient.getUserById(userId))
                .orElseThrow(() -> new NotFoundException(User.class.getSimpleName(), userId));

        var user = cs.convert(userDto, User.class);

        var categoryDto = categoryClient.getById(createEventDto.category());

        var locationDto = getLocation(createEventDto.location().lat(),
                createEventDto.location().lon());

        var event = cs.convert(createEventDto, Event.class);

        if (Objects.isNull(event) || Objects.isNull(user)) {
            throw new NotFoundException("event or user not found");
        }

        event.setInitiator(user.getId());
        event.setCategory(categoryDto.id());
        event.setLocation(locationDto.id());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);

        eventStorage.save(event);

        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserWithoutEmailDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build())
                .location(locationDto)
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    @Override
    public List<EventFullDto> getAllByUserId(final long userId, final int from, final int size) {
        var user = userClient.getUserById(userId);
        if (Objects.isNull(user)) {
            throw new NotFoundException(User.class.getSimpleName(), userId);
        }
        var userEvents = eventStorage.findAllByInitiatorId(user.id(), PageRequest.of(from, size));
        return userEvents.stream().map(event -> cs.convert(event, EventFullDto.class)).toList();
    }

    @Override
    public EventFullDto getByIdAndUserId(final long eventId, final long userId) {
        var user = userClient.getUserById(userId);
        if (Objects.isNull(user)) {
            throw new NotFoundException(User.class.getSimpleName(), userId);
        }
        var event = eventStorage.getByIdOrElseThrow(eventId);
        checkIfTheUserIsTheEventCreator(userId, eventId);
        return cs.convert(event, EventFullDto.class);
    }

    @Override
    public EventFullDto getById(final long eventId, final HttpServletRequest request, long userId) {
        Event event = eventStorage.getByIdOrElseThrow(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException(SIMPLE_NAME, eventId);
        }

        addStats(request);

        updateStats(event, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(3), true);
        var convert = cs.convert(eventStorage.save(event), EventFullDto.class);
        collectorClient.sendUserAction(userId, eventId, UserActionType.VIEW.toString());
        return convert;
    }

    @Override
    public List<EventFullDto> getAll(final PublicParameter publicParameter, final HttpServletRequest request) {
        BooleanExpression predicate = event.isNotNull();

        if (!ObjectUtils.isEmpty(publicParameter.getText())) {
            predicate = predicate.and(event.annotation.likeIgnoreCase(publicParameter.getText()));
        }

        if (!ObjectUtils.isEmpty(publicParameter.getCategories())) {
            predicate = predicate.and(event.category.in(publicParameter.getCategories()));
        }
        if (!ObjectUtils.isEmpty(publicParameter.getPaid())) {
            predicate = predicate.and(event.paid.eq(publicParameter.getPaid()));
        }

        predicate = predicate.and(
                event.createdOn.between(publicParameter.getRangeStart(), publicParameter.getRangeEnd()));

        addStats(request);

        final List<Event> lists = eventStorage.findAll(
                predicate, PageRequest.of(publicParameter.getFrom() / publicParameter.getSize(),
                        publicParameter.getSize())
        );

        lists.forEach(
                event -> updateStats(event, publicParameter.getRangeStart(), publicParameter.getRangeEnd(), false));

        eventStorage.saveAll(lists);

        return lists.stream()
                .map(event -> cs.convert(event, EventFullDto.class))
                .toList();
    }

    @Override
    public List<EventFullDto> getAllByLocation(final double lat, final double lon, final double radius) {
        List<Event> events;

        if (radius > 0) {
            events = eventStorage.findAllByLocationAndRadius(lat, lon, radius);
        } else {
            events = eventStorage.findAllEventsByLocation(lat, lon);
        }

        if (ObjectUtils.isEmpty(events)) {
            return List.of();
        }

        return events.stream()
                .map(event -> cs.convert(event, EventFullDto.class))
                .toList();
    }

    @Override
    public EventFullDto updateByUser(final long userId, final long eventId, final UpdateEventDto updateEventDto) {
        Event eventInStorage = eventStorage.getByIdOrElseThrow(eventId);

        checkEventIsPublished(eventInStorage.getState());

        if (eventInStorage.getInitiator() != userId) {
            throw new ConflictException("The initiator does not belong to this event");
        }

        if (!ObjectUtils.isEmpty(updateEventDto.stateAction())) {
            switch (updateEventDto.stateAction()) {
                case CANCEL_REVIEW -> eventInStorage.setState(State.CANCELED);

                case SEND_TO_REVIEW -> eventInStorage.setState(State.PENDING);
            }
        }
        eventStorage.save(update(eventInStorage, updateEventDto));

        var user = userClient.getAll(List.of(userId), 0, 1).getFirst();

        if (Objects.isNull(user)) {
            throw new NotFoundException(User.class.getSimpleName(), userId);
        }
        return EventFullDto.builder()
                .id(eventInStorage.getId())
                .annotation(eventInStorage.getAnnotation())
                .category(categoryClient.getById(eventInStorage.getCategory()))
                .confirmedRequests(eventInStorage.getConfirmedRequests())
                .createdOn(eventInStorage.getCreatedOn())
                .description(eventInStorage.getDescription())
                .eventDate(eventInStorage.getEventDate())
                .initiator(UserWithoutEmailDto.builder()
                        .id(user.id())
                        .name(user.name())
                        .build())
                .location(locationClient.getById(eventInStorage.getLocation()))
                .paid(eventInStorage.isPaid())
                .participantLimit(eventInStorage.getParticipantLimit())
                .publishedOn(eventInStorage.getPublishedOn())
                .requestModeration(eventInStorage.isRequestModeration())
                .state(eventInStorage.getState())
                .title(eventInStorage.getTitle())
                .views(eventInStorage.getViews())
                .build();
    }

    @Override
    public void addEventLike(long userId, long eventId) {
        if (requestClient.getAllRequests(userId).stream().anyMatch(request -> request.event() == eventId)) {
            collectorClient.sendUserAction(userId, eventId, UserActionType.LIKE.toString());
        }
        throw new ConflictException("User does not have permission");
    }

    private LocationDto getLocation(double lat, double lon) {
        LocationDto locationDto = locationClient.getByCoordinates(lat, lon);

        if (Objects.isNull(locationDto)) {
            locationDto = locationClient.create(CreateLocationDto.builder()
                    .lat(lat)
                    .lon(lon)
                    .name("lat " + lat + " lon " + lon)
                    .radius(0.0)
                    .build());
        }
        return locationDto;
    }

    private void addStats(final HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.app("ewm");
        endpointHit.ip(request.getRemoteAddr());
        endpointHit.uri(request.getRequestURI());
        endpointHit.timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        statsClient.hit(endpointHit);
    }

    private void updateStats(Event event, final LocalDateTime startRange, final LocalDateTime endRange,
            final boolean unique) {

        List<ViewStats> stats = statsClient.getStats(startRange.toString(), endRange.toString(),
                        List.of("/events/" + event.getId()), unique)
                .getBody();

        long views = 0L;

        for (ViewStats stat : stats) {
            views += stat.getHits();
        }

        var requests = requestClient.getRequestsByEventId(event.getId());

        event.setViews(views);
        event.setConfirmedRequests(
                requests.removeIf(request -> request.status() != State.CONFIRMED) ? requests.size() : 0);
    }

    private void checkIfTheUserIsTheEventCreator(final long userId, final long eventId) {
        if (userId == eventId) {
            throw new ConflictException(String.format("Event originator userId: %d cannot add a membership request " +
                                                      "in its event eventId: %d", userId, eventId));
        }
    }

    private Specification<Event> checkCategories(final List<Long> categories) {
        return ObjectUtils.isEmpty(categories) ? null
                : ((root, query, criteriaBuilder) -> root.get("category").get("id").in(categories));
    }

    private Specification<Event> checkByUserIds(final List<Long> userIds) {
        return ObjectUtils.isEmpty(userIds) ? null
                : ((root, query, criteriaBuilder) -> root.get("initiator").get("id").in(userIds));
    }

    private Specification<Event> checkStates(final List<State> states) {
        return ObjectUtils.isEmpty(states) ? null
                : ((root, query, criteriaBuilder) -> root.get("state").as(String.class).in(states.stream()
                        .map(Enum::toString)
                        .toList())
                );
    }

    private Specification<Event> checkRangeStart(final LocalDateTime start) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
                        start == null ? LocalDateTime.now() : start));
    }

    private Specification<Event> checkRangeEnd(final LocalDateTime end) {
        return ObjectUtils.isEmpty(end) ? null
                : ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), end));
    }

    private Specification<Event> getSpecification(final AdminParameter adminParameter) {
        return Specification.where(checkByUserIds(adminParameter.getUsers()))
                .and(checkStates(adminParameter.getStates()))
                .and(checkCategories(adminParameter.getCategories()))
                .and(checkRangeStart(adminParameter.getRangeStart()))
                .and(checkRangeEnd(adminParameter.getRangeEnd()));
    }

    private void checkEventIsPublished(final State state) {
        if (state == State.PUBLISHED) {
            throw new ConflictException("An event can only be rejected if it has not yet been published");
        }
    }
}
