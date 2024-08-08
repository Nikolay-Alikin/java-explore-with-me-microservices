package com.github.artemlv.stats.service.impl;

import com.github.artemlv.stats.repository.StatRepository;
import com.github.artemlv.stats.service.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.generated.model.dto.EndpointHit;
import ru.yandex.practicum.generated.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    @Override
    public ResponseEntity<Void> hit(final EndpointHit endpointHit) {
        statRepository.save(endpointHit);
        log.info("Create hit {}", endpointHit);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ViewStats>> getStats(final LocalDateTime start, final LocalDateTime end,
                                                    final List<String> uris, final Boolean unique) {
        log.info("Get stats param: start {}, end {}, uris: {}, unique {}", start, end, uris, unique);

        if (start.isAfter(end)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<ViewStats> stats = statRepository.getStats(start, end, uris, unique);

        log.info("Result stats: {}", stats);

        if (stats.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}