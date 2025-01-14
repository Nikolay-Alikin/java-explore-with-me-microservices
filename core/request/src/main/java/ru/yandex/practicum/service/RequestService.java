package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.request.model.dto.RequestDto;
import ru.yandex.practicum.request.model.dto.RequestStatusUpdateResultDto;
import ru.yandex.practicum.request.model.dto.UpdateRequestByIdsDto;

public interface RequestService {

    RequestDto create(final long userId, final long eventId);

    List<RequestDto> getAll(final long userId);

    RequestDto cancel(final long userId, final long requestId);

    RequestStatusUpdateResultDto updateRequestStatus(long userId, long eventId,
            UpdateRequestByIdsDto updateRequestByIdsDto);

    List<RequestDto> getRequestsOnEvent(long userId, long eventId);

    List<RequestDto> getRequestsByEventId(long eventId);
}
