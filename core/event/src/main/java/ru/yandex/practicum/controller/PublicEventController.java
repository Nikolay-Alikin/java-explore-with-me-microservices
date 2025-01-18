package ru.yandex.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublicParameter;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.service.EventService;
import ru.yandex.practicum.validation.ConstraintNotZero;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private static final String SIMPLE_NAME = Event.class.getSimpleName();
    private final EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable @Positive final long id, final HttpServletRequest request) {
        log.info("Public event {} by id - {}", SIMPLE_NAME, id);
        return eventService.getById(id, request);
    }

    @GetMapping
    public List<EventFullDto> getAll(@Valid final PublicParameter publicParameter,
                                 final HttpServletRequest request) {
        log.info("Public {} request with parameters - {}", SIMPLE_NAME, publicParameter);
        return eventService.getAll(publicParameter, request);
    }

    @GetMapping("/locations")
    public List<EventFullDto> getEventsByLatAndLon(@RequestParam @ConstraintNotZero final Double lat,
                                               @RequestParam @ConstraintNotZero final Double lon,
                                               @RequestParam(required = false, defaultValue = "0")
                                               @PositiveOrZero final double radius) {
        log.info("Public {} to receive events by location and radius - {} - {} - {}", SIMPLE_NAME, lat, lon, radius);
        return eventService.getAllByLocation(lat, lon, radius);
    }
}
