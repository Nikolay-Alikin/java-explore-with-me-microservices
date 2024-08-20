package com.github.artemlv.stats.controller;

import com.github.artemlv.stats.controller.api.HitApi;
import com.github.artemlv.stats.controller.api.StatsApi;
import com.github.artemlv.stats.service.StatService;
import dto.EndpointHit;
import dto.ViewStats;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi, HitApi {
    private final StatService service;

    @Override
    public ResponseEntity<Void> hit(EndpointHit endpointHit) {
        return service.hit(endpointHit);
    }

    @Override
    public ResponseEntity<List<ViewStats>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris,
                                                    Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
