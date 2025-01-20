package ru.yandex.practicum.controller;

import client.CollectorClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.constant.UserActionType;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.PublicParameter;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.recomendation.RecommendationDto;
import ru.yandex.practicum.service.EventService;
import ru.yandex.practicum.service.RecommendationService;
import ru.yandex.practicum.validation.ConstraintNotZero;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;
    private final RecommendationService recommendationService;

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable @Positive final long id, final HttpServletRequest request,
            @RequestHeader("X-EWM-USER-ID") long userId) {
        var dto = eventService.getById(id, request,userId);
        return dto;
    }

    @GetMapping("/recommendations")
    public List<RecommendationDto> getRecommendations(
            @RequestHeader("X-EWM-USER-ID") long userId, @RequestParam(name = "size", defaultValue = "10") int size) {
        return recommendationService.getRecommendations(userId, size);
    }

    @PutMapping("/{eventId}/like")
    public void addEventLike(@RequestHeader("X-EWM-USER-ID") long userId, @PathVariable long eventId) {
        eventService.addEventLike(eventId, userId);
    }

    @GetMapping
    public List<EventFullDto> getAll(@Valid final PublicParameter publicParameter,
            final HttpServletRequest request) {
        return eventService.getAll(publicParameter, request);
    }

    @GetMapping("/locations")
    public List<EventFullDto> getEventsByLatAndLon(@RequestParam @ConstraintNotZero final Double lat,
            @RequestParam @ConstraintNotZero final Double lon,
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero final double radius) {
        return eventService.getAllByLocation(lat, lon, radius);
    }
}
