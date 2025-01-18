package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.event.model.AdminParameter;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.event.model.dto.UpdateEventDto;
import ru.yandex.practicum.service.EventService;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private static final String SIMPLE_NAME = Event.class.getSimpleName();
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAll(@Valid final AdminParameter adminParameter) {
        log.info("Administrator's request to provide {} by parameters - {}", SIMPLE_NAME, adminParameter);
        return eventService.getAllByAdmin(adminParameter);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@RequestBody @Valid final UpdateEventDto updateEventDto,
            @PathVariable @Positive final long eventId) {
        log.info("Request by the administrator to change an {} by id - {} - {}", SIMPLE_NAME, eventId, updateEventDto);
        return eventService.updateByAdmin(eventId, updateEventDto);
    }
}
