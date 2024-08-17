package com.github.artemlv.ewm.event.controller;

import com.github.artemlv.ewm.event.model.AdminParameter;
import com.github.artemlv.ewm.event.model.Event;
import com.github.artemlv.ewm.event.model.dto.EventDto;
import com.github.artemlv.ewm.event.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private static final String SIMPLE_NAME = Event.class.getSimpleName();
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public EventDto getById(@PathVariable @Positive final long eventId, final HttpServletRequest request) {
        log.info("Public event {} by id - {}", SIMPLE_NAME, eventId);
        return eventService.getById(eventId, request);
    }

    @GetMapping
    public List<EventDto> getAll(@Valid final AdminParameter adminParameter,
                                 final HttpServletRequest request) {
        log.info("Public {} request with parameters - {}", SIMPLE_NAME, adminParameter);
        return eventService.getAll(adminParameter, request);
    }

    @GetMapping("/locations")
    public List<EventDto> getEventsByLatAndLon(@RequestParam @NotNull final Double lat,
                                               @RequestParam @NotNull final Double lon,
                                               @RequestParam(required = false) final Double radius) {
        log.info("Public {} to receive events by location and radius - {} - {} - {}", SIMPLE_NAME, lat, lon, radius);
        return eventService.getAllByLocation(lat, lon, radius);
    }
}
