package com.github.artemlv.stats.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.generated.model.dto.EndpointHit;
import ru.yandex.practicum.generated.model.dto.ViewStats;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ViewStatsMapper viewStatsMapper;

    public void save(EndpointHit endpointHit) {
        jdbcTemplate.update(StatQuery.SAVE_HIT,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp());
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String sql = StatQuery.getStringQueryForStats(uris, unique);

        List<Object> params = new ArrayList<>();
        params.add(Timestamp.valueOf(start));
        params.add(Timestamp.valueOf(end));
        if (uris != null && !uris.isEmpty()) {
            params.addAll(uris);
        }

        return jdbcTemplate.query(sql, params.toArray(), viewStatsMapper);
    }
}
