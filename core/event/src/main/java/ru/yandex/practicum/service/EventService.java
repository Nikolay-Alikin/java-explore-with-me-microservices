package ru.yandex.practicum.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import ru.yandex.practicum.event.model.AdminParameter;
import ru.yandex.practicum.event.model.PublicParameter;
import ru.yandex.practicum.event.model.dto.CreateEventDto;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.event.model.dto.UpdateEventDto;

public interface EventService {

    List<EventFullDto> getAllByAdmin(final AdminParameter adminParameter);

    EventFullDto updateByAdmin(final long eventId, final UpdateEventDto updateEventDto);

    EventFullDto create(final CreateEventDto createEventDto, final long userId);

    List<EventFullDto> getAllByUserId(final long userId, final int from, final int size);


    EventFullDto getByIdAndUserId(final long eventId, final long userId);

    EventFullDto getById(final long eventId, final HttpServletRequest request, long userId);

    List<EventFullDto> getAll(final PublicParameter publicParameter, final HttpServletRequest request);

    List<EventFullDto> getAllByLocation(final double lat, final double lon, final double radius);

    EventFullDto updateByUser(final long userId, final long eventId, final UpdateEventDto updateEventDto);

    void addEventLike(long userId, long eventId);
}
