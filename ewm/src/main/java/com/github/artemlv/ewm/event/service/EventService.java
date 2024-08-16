package com.github.artemlv.ewm.event.service;

import com.github.artemlv.ewm.event.model.AdminParameter;
import com.github.artemlv.ewm.event.model.dto.CreateEventDto;
import com.github.artemlv.ewm.event.model.dto.EventDto;
import com.github.artemlv.ewm.event.model.dto.UpdateEventDto;
import com.github.artemlv.ewm.request.model.UpdateRequestByIdsDto;
import com.github.artemlv.ewm.request.model.dto.RequestDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface EventService {
    List<EventDto> getAllByAdmin(final AdminParameter adminParameter);

    EventDto updateByIdAdmin(final long eventId, final UpdateEventDto updateEventDto);

    EventDto create(final CreateEventDto createEventDto, final long userId);

    List<EventDto> getAllByUserId(final long userId, final int from, final int size);

    List<RequestDto> getRequestsByUserIdAndEventId(final long userId, final long eventId);

    EventDto getByIdAndUserId(final long eventId, final long userId);

    UpdateRequestByIdsDto updateRequestsStatusByUserIdAndEventId(final long userId,
                                                                 final long eventId,
                                                                 final UpdateRequestByIdsDto update);

    EventDto getById(final long eventId, final HttpServletRequest request);

    List<EventDto> getAll(final AdminParameter adminParameter, final HttpServletRequest request);

    List<EventDto> getAllByLocation(final Double lat, final Double lon, final Double radius);
}
