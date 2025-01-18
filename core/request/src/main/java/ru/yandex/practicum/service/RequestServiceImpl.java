package ru.yandex.practicum.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.event.client.EventClient;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.event.model.dto.UpdateEventDto;
import ru.yandex.practicum.exception.type.ConflictException;
import ru.yandex.practicum.exception.type.NotFoundException;
import ru.yandex.practicum.location.model.dto.LocationLatAndLonDto;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.request.model.dto.RequestDto;
import ru.yandex.practicum.request.model.dto.RequestStatusUpdateResultDto;
import ru.yandex.practicum.request.model.dto.UpdateRequestByIdsDto;
import ru.yandex.practicum.state.State;
import ru.yandex.practicum.storage.RequestStorage;
import ru.yandex.practicum.user.client.UserClient;
import ru.yandex.practicum.user.model.dto.UserDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private static final String SIMPLE_NAME = Request.class.getSimpleName();
    @Qualifier("mvcConversionService")
    private final ConversionService cs;
    private final RequestStorage requestStorage;
    private final UserClient userClient;
    private final EventClient eventClient;

    @Override
    @Transactional
    public RequestDto create(final long userId, final long eventId) {
        requestStorage.ifExistsByRequesterIdAndEventIdThenThrow(userId, eventId);
        var user = getUser(userId);
        var event = getEvent(eventId);

        if (event.initiator().id() == user.id()) {
            throw new ConflictException("%s : can`t add a request to your own: %d eventId: %d".formatted(SIMPLE_NAME,
                    userId, eventId));
        }

        if (event.state() != State.PUBLISHED) {
            throw new ConflictException("Cannot add a request to an unpublished eventId: %d".formatted(eventId));
        } else if (event.participantLimit() != 0
                   && requestStorage.countByEventIdAndStatus(event.id(), State.CONFIRMED)
                      >= event.participantLimit()) {
            throw new ConflictException("Event participation limit exceeded eventId: %d".formatted(eventId));
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event.id())
                .requester(user.id())
                .status(event.participantLimit() == 0 || !event.requestModeration() ? State.CONFIRMED
                        : State.PENDING)
                .build();

        if (request.getStatus() == State.CONFIRMED) {
            int confirmedRequests = event.confirmedRequests() + 1;
            var eventToSend = UpdateEventDto.builder()
                    .annotation(event.annotation())
                    .category(event.category().id())
                    .description(event.description())
                    .eventDate(event.eventDate())
                    .location(LocationLatAndLonDto.builder()
                            .lat(event.location().lat())
                            .lon(event.location().lon())
                            .build())
                    .paid(event.paid())
                    .participantLimit(event.participantLimit())
                    .requestModeration(event.requestModeration())
                    .title(event.title())
                    .confirmedRequests(confirmedRequests)
                    .build();

            eventClient.update(eventToSend, event.id());
        }
        return cs.convert(requestStorage.save(request), RequestDto.class);
    }

    @Override
    public List<RequestDto> getAll(final long userId) {
        return requestStorage.findAllByRequesterId(userId).stream()
                .map(request -> cs.convert(request, RequestDto.class))
                .toList();
    }

    @Override
    @Transactional
    public RequestDto cancel(final long userId, final long requestId) {
        getUser(userId);
        var request = requestStorage.getById(requestId)
                .orElseThrow(() -> new NotFoundException(Request.class.getSimpleName(), requestId));

        if (request.getStatus() == State.CONFIRMED) {
            var eventDto = eventClient.getEventByEventId(request.getEvent());

            var updatedEvent = EventFullDto.builder()
                    .id(eventDto.id())
                    .annotation(eventDto.annotation())
                    .category(eventDto.category())
                    .confirmedRequests(eventDto.confirmedRequests() - 1)
                    .createdOn(eventDto.createdOn())
                    .description(eventDto.description())
                    .eventDate(eventDto.eventDate())
                    .initiator(eventDto.initiator())
                    .location(eventDto.location())
                    .paid(eventDto.paid())
                    .participantLimit(eventDto.participantLimit())
                    .publishedOn(eventDto.publishedOn())
                    .requestModeration(eventDto.requestModeration())
                    .state(eventDto.state())
                    .title(eventDto.title())
                    .views(eventDto.views())
                    .build();

            eventClient.update(cs.convert(updatedEvent, UpdateEventDto.class), updatedEvent.id());
        }
        request.setStatus(State.CANCELED);

        return cs.convert(requestStorage.save(request), RequestDto.class);
    }

    @Override
    @Transactional
    public RequestStatusUpdateResultDto updateRequestStatus(long userId, long eventId,
            UpdateRequestByIdsDto updateRequestByIdsDto) {
        var requests = requestStorage.findAllByIdInAndEventId(updateRequestByIdsDto.requestIds(),
                eventId);

        checkStatusesIsNotPending(requests);

        if (requests.isEmpty()) {
            throw new NotFoundException("requests not found");
        }
        long countConfirmedRequests = requests.stream()
                .filter(request -> request.getStatus() == State.CONFIRMED)
                .count();

        var dto = RequestStatusUpdateResultDto.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();
        var requestsToSave = new ArrayList<Request>();

        requests.forEach(request -> {
            var eventDto = eventClient.getEventByEventId(request.getEvent());

            if (countConfirmedRequests >= eventDto.participantLimit()) {
                throw new ConflictException("The limit on applications for this event has been reached");
            }
            if (eventDto.requestModeration()) {
                request.setStatus(updateRequestByIdsDto.status());
            }
            switch (updateRequestByIdsDto.status()) {
                case CONFIRMED -> dto.confirmedRequests().add(cs.convert(request, RequestDto.class));
                case REJECTED -> dto.rejectedRequests().add(cs.convert(request, RequestDto.class));
            }
            if (countConfirmedRequests + 1 == eventDto.participantLimit()) {
                request.setStatus(State.CANCELED);
            }
            requestsToSave.add(request);
        });
        requestStorage.saveAll(requestsToSave);
        return dto;
    }

    @Override
    public List<RequestDto> getRequestsOnEvent(long userId, long eventId) {
        var eventDtos = eventClient.getAll(List.of(userId), null, null, List.of(eventId), null,
                null, 0, Integer.MAX_VALUE);

        if (!eventDtos.isEmpty()) {
            return requestStorage.findAllByEventId(eventId).stream()
                    .map(request -> cs.convert(request, RequestDto.class))
                    .toList();
        }
        return List.of();
    }

    @Override
    public List<RequestDto> getRequestsByEventId(long eventId) {
        return requestStorage.findAllByEventId(eventId).stream()
                .map(request -> cs.convert(request, RequestDto.class))
                .toList();
    }

    private void checkStatusesIsNotPending(List<Request> requests) {
        requests.stream().filter(request -> request.getStatus() != State.PENDING)
                .findFirst()
                .ifPresent(request -> {
                    throw new ConflictException("this status can't be changed");
                });
    }


    private UserDto getUser(long userId) {
        var user = userClient.getUserById(userId);
        if (user == null) {
            throw new NotFoundException(UserDto.class.getSimpleName(), userId);
        }
        return user;
    }

    private EventFullDto getEvent(long eventId) {
        var event = eventClient.getEventByEventId(eventId);
        if (event == null) {
            throw new NotFoundException(EventFullDto.class.getSimpleName(), eventId);
        }
        return event;
    }
}
