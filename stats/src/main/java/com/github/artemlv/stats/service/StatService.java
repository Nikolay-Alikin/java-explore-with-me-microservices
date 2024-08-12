package com.github.artemlv.stats.service;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.generated.model.dto.EndpointHit;
import ru.yandex.practicum.generated.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    ResponseEntity<Void> hit(final EndpointHit endpointHit);

    ResponseEntity<List<ViewStats>> getStats(final LocalDateTime start, final LocalDateTime end,
                                             final List<String> uris, final Boolean unique);
}
