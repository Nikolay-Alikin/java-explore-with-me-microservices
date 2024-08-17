package com.github.artemlv.ewm.request.controller;

import com.github.artemlv.ewm.category.model.Category;
import com.github.artemlv.ewm.request.model.Request;
import com.github.artemlv.ewm.request.model.dto.RequestDto;
import com.github.artemlv.ewm.request.service.RequestService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateUserRequestController {
    private final RequestService requestService;
    private static final String SIMPLE_NAME = Request.class.getSimpleName();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable @Positive final long userId,
                             @RequestParam @Positive final long eventId) {
        log.debug("{} to participate in an event by id - {} by user with id - {}", SIMPLE_NAME, eventId, userId);
        return requestService.create(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getAll(@PathVariable @Positive final long userId) {
        log.debug("{} user participation by id - {}", SIMPLE_NAME, userId);
        return requestService.getAll(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancel(@PathVariable @Positive final long userId,
                                                 @PathVariable @Positive final long requestId) {
        log.debug("{} to cancel participation by id - {} of user with id - {}", SIMPLE_NAME, requestId, userId);
        return requestService.cancel(userId, requestId);
    }
}
