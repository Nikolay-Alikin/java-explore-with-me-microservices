package ru.yandex.practicum.request.client;

import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.request.model.dto.RequestDto;
import ru.yandex.practicum.request.model.dto.RequestStatusUpdateResultDto;
import ru.yandex.practicum.request.model.dto.UpdateRequestByIdsDto;

@FeignClient("request")
public interface RequestClient {

    @PostMapping("/users/{userId}/requests")
    RequestDto createRequest(@PathVariable @Positive long userId,
            @RequestParam @Positive long eventId);

    @GetMapping("/users/{userId}/requests")
    List<RequestDto> getAllRequests(@PathVariable @Positive long userId);

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    RequestDto cancelRequest(@PathVariable @Positive long userId,
            @PathVariable @Positive long requestId);

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    List<RequestDto> getRequestsByUserIdAndEventId(@PathVariable @Positive long userId,
            @PathVariable @Positive long eventId);

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    RequestStatusUpdateResultDto updateRequestsByUserAndEvent(
            @RequestBody UpdateRequestByIdsDto updateRequestByIdsDto,
            @PathVariable @Positive long userId,
            @PathVariable @Positive long eventId);

    @GetMapping("requests/events/{eventId}")
    List<RequestDto> getRequestsByEventId(@PathVariable @Positive long eventId);

}
