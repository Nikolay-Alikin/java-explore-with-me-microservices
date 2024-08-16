package com.github.artemlv.ewm.event.controller;

import com.github.artemlv.ewm.event.model.AdminParameter;
import com.github.artemlv.ewm.event.model.Event;
import com.github.artemlv.ewm.event.model.dto.EventDto;
import com.github.artemlv.ewm.event.model.dto.UpdateEventDto;
import com.github.artemlv.ewm.event.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;
    private static final String SIMPLE_NAME = Event.class.getSimpleName();

    @GetMapping
    public List<EventDto> getAll(@Valid final AdminParameter adminParameter) {
        log.debug("Administrator's request to provide {} by parameters - {}", SIMPLE_NAME, adminParameter);
        return eventService.getAllByAdmin(adminParameter);
    }

    @PatchMapping("/{eventId}")
    public EventDto update(@RequestBody @Valid final UpdateEventDto updateEventDto,
                           @PathVariable @Positive final long eventId) {
        log.debug("Request by the administrator to change an {} by id - {} - {}", SIMPLE_NAME, eventId, updateEventDto);
        return eventService.updateByIdAdmin(eventId, updateEventDto);
    }
}
