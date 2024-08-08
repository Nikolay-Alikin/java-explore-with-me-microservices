package com.github.artemlv.stats.controller;

import com.github.artemlv.stats.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.generated.api.HitApi;
import ru.yandex.practicum.generated.api.StatsApi;
import ru.yandex.practicum.generated.model.dto.EndpointHit;
import ru.yandex.practicum.generated.model.dto.ViewStats;

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
