package com.github.artemlv.stats.service;

import dto.EndpointHit;
import dto.ViewStats;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    ResponseEntity<Void> hit(final EndpointHit endpointHit);

    ResponseEntity<List<ViewStats>> getStats(final LocalDateTime start, final LocalDateTime end,
                                             final List<String> uris, final Boolean unique);
}
