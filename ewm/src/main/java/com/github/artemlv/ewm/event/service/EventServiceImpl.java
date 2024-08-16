package com.github.artemlv.ewm.event.service;

import com.github.artemlv.ewm.category.model.Category;
import com.github.artemlv.ewm.category.storage.CategoryStorage;
import com.github.artemlv.ewm.event.model.AdminParameter;
import com.github.artemlv.ewm.event.model.Event;
import com.github.artemlv.ewm.event.model.dto.CreateEventDto;
import com.github.artemlv.ewm.event.model.dto.EventDto;
import com.github.artemlv.ewm.event.model.dto.UpdateEventDto;
import com.github.artemlv.ewm.event.storage.EventStorage;
import com.github.artemlv.ewm.exception.type.ConflictException;
import com.github.artemlv.ewm.exception.type.NotFoundException;
import com.github.artemlv.ewm.location.model.Location;
import com.github.artemlv.ewm.location.service.LocationService;
import com.github.artemlv.ewm.request.model.UpdateRequestByIdsDto;
import com.github.artemlv.ewm.request.model.dto.RequestDto;
import com.github.artemlv.ewm.state.State;
import com.github.artemlv.ewm.user.model.User;
import com.github.artemlv.ewm.user.storage.UserStorage;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.artemlv.ewm.event.model.QEvent.event;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final LocationService locationService;
    private static final String SIMPLE_NAME = Event.class.getSimpleName();


    @Override
    public List<EventDto> getAllByAdmin(final AdminParameter adminParameter) {
        BooleanExpression predicate = event.isNotNull();

        if (!ObjectUtils.isEmpty(adminParameter.getUsers())) {
            predicate = predicate.and(event.initiator.id.in(adminParameter.getUsers()));
        }

        if (!ObjectUtils.isEmpty(adminParameter.getStates())) {
            predicate = predicate.and(event.state.in(adminParameter.getStates()));
        }
        if (!ObjectUtils.isEmpty(adminParameter.getCategories())) {
            predicate = predicate.and(event.category.id.in(adminParameter.getCategories()));
        }

        if (!ObjectUtils.isEmpty(adminParameter.getRangeStart()) && !ObjectUtils.isEmpty(adminParameter.getRangeEnd())) {
            predicate = predicate.and(event.createdOn.between(adminParameter.getRangeStart(),
                    adminParameter.getRangeEnd()));
        } else if (!ObjectUtils.isEmpty(adminParameter.getRangeStart())) {
            predicate = predicate.and(event.createdOn.after(adminParameter.getRangeStart()));
        } else if (!ObjectUtils.isEmpty(adminParameter.getRangeEnd())) {
            predicate = predicate.and(event.createdOn.before(adminParameter.getRangeEnd()));
        }

        return eventStorage.findAll(predicate, PageRequest.of(adminParameter.getFrom() / adminParameter.getSize(),
                        adminParameter.getSize())).stream()
                .map(event -> cs.convert(event, EventDto.class))
                .toList();
    }

    @Override
    public EventDto updateByIdAdmin(final long eventId, final UpdateEventDto updateEventDto) {
        Event event = eventStorage.getByIdOrElseThrow(eventId);

        if (!ObjectUtils.isEmpty(updateEventDto.stateAction())) {
            switch (updateEventDto.stateAction()) {
                case REJECT_EVENT -> {
                    checkEventStatePublished(event.getState());
                    event.setState(State.CANCELED);
                }

                case PUBLISH_EVENT -> {
                    checkEventStatePublished(event.getState());
                    event.setPublishedOn(LocalDateTime.now());
                    event.setState(State.PUBLISHED);
                }
            }
        }

        if (!ObjectUtils.isEmpty(updateEventDto.location())) {
            event.setLocation(cs.convert(
                    locationService.getByCoordinatesOrElseCreate(updateEventDto.location()), Location.class)
            );
        }
        if (!ObjectUtils.isEmpty(updateEventDto.category())) {
            event.setCategory(categoryStorage.getByIdOrElseThrow(updateEventDto.category()));
        }

        return cs.convert(event, EventDto.class);
    }

    private void checkEventStatePublished(final State state) {
        if (state == State.PUBLISHED) {
            throw new ConflictException("An event can only be rejected if it has not yet been published");
        }
    }

    @Override
    public EventDto create(final CreateEventDto createEventDto, final long userId) {
        final User user = userStorage.getByIdOrElseThrow(userId);
        final Category category = categoryStorage.getByIdOrElseThrow(createEventDto.category());
        final Location location = cs.convert(locationService.getByCoordinatesOrElseCreate(createEventDto.location()),
                Location.class);

        Event event = cs.convert(createEventDto, Event.class);

        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);

        return cs.convert(eventStorage.save(event), EventDto.class);
    }

    @Override
    public List<EventDto> getAllByUserId(final long userId, final int from, final int size) {
        userStorage.existsByIdOrElseThrow(userId);
        return eventStorage.findAllByInitiatorId(userId, PageRequest.of(from / size, size)).stream()
                .map(event -> cs.convert(event, EventDto.class))
                .toList();
    }

    @Override
    public List<RequestDto> getRequestsByUserIdAndEventId(final long userId, final long eventId) {
        checkIfTheUserIsTheEventCreator(userId, eventId);
        return List.of();
    }

    @Override
    public EventDto getByIdAndUserId(final long eventId, final long userId) {
        userStorage.existsByIdOrElseThrow(userId);
        Event event = eventStorage.getByIdOrElseThrow(eventId);
        checkIfTheUserIsTheEventCreator(userId, eventId);
        return cs.convert(event, EventDto.class);
    }

    @Override
    public UpdateRequestByIdsDto updateRequestsStatusByUserIdAndEventId(final long userId, final long eventId,
                                                                        UpdateRequestByIdsDto update) {
        return null;
    }

    @Override
    public EventDto getById(final long eventId, final HttpServletRequest request) {
        Event event = eventStorage.getByIdOrElseThrow(eventId);

        if (event.getState() != State.PUBLISHED) {
            throw new NotFoundException(SIMPLE_NAME, eventId);
        }

        addStats(request);

        return cs.convert(event, EventDto.class);
    }

    @Override
    public List<EventDto> getAll(final AdminParameter adminParameter, final HttpServletRequest request) {
        BooleanExpression predicate = event.isNotNull();

        if (!ObjectUtils.isEmpty(adminParameter.getUsers())) {
            predicate = predicate.and(event.initiator.id.in(adminParameter.getUsers()));
        }

        if (!ObjectUtils.isEmpty(adminParameter.getStates())) {
            predicate = predicate.and(event.state.in(adminParameter.getStates()));
        }
        if (!ObjectUtils.isEmpty(adminParameter.getCategories())) {
            predicate = predicate.and(event.category.id.in(adminParameter.getCategories()));
        }

        if (!ObjectUtils.isEmpty(adminParameter.getRangeStart()) && !ObjectUtils.isEmpty(adminParameter.getRangeEnd())) {
            predicate = predicate.and(event.createdOn.between(adminParameter.getRangeStart(),
                    adminParameter.getRangeEnd()));
        } else if (!ObjectUtils.isEmpty(adminParameter.getRangeStart())) {
            predicate = predicate.and(event.createdOn.after(adminParameter.getRangeStart()));
        } else if (!ObjectUtils.isEmpty(adminParameter.getRangeEnd())) {
            predicate = predicate.and(event.createdOn.before(adminParameter.getRangeEnd()));
        }

        addStats(request);

        return eventStorage.findAll(predicate, PageRequest.of(adminParameter.getFrom() / adminParameter.getSize(),
                        adminParameter.getSize())).stream()
                .map(event -> cs.convert(event, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> getAllByLocation(final Double lat, final Double lon, final Double radius) {
        List<Event> events;

        if (Objects.nonNull(radius)) {
            events = eventStorage.findAllByLocationAndRadius(lat, lon, radius);
        } else {
            events = eventStorage.findAllEventsByLocation(lat, lon);
        }

        return events.stream()
                .map(event -> cs.convert(event, EventDto.class))
                .collect(Collectors.toList());
    }

    private void addStats(final HttpServletRequest request) {
        //Необходимо положить статистику

    }

    private void checkIfTheUserIsTheEventCreator(final long userId, final long eventId) {
        if (userId == eventId) {
            throw new ConflictException(String.format("Event originator userId: %d cannot add a membership request " +
                    "in its event eventId: %d", userId, eventId));
        }
    }
}
