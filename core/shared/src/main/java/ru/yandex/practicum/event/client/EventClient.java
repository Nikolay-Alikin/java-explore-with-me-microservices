package ru.yandex.practicum.event.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.event.model.dto.EventFullDto;
import ru.yandex.practicum.event.model.dto.UpdateEventDto;

@FeignClient("event-service")
public interface EventClient {

    @GetMapping("/admin/events")
    List<EventFullDto> getAll(@RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "states", required = false) List<String> states,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "events", required = false) List<Long> events,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    @GetMapping("events/{id}")
    EventFullDto getEventByEventId(@PathVariable Long id);

    @PatchMapping("/admin/events/{eventId}")
    EventFullDto update(@RequestBody UpdateEventDto updateEventDto,
            @PathVariable("eventId") long eventId);
}
