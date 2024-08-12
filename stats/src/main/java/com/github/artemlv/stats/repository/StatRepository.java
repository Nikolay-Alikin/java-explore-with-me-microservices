package com.github.artemlv.stats.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.generated.model.dto.EndpointHit;
import ru.yandex.practicum.generated.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ViewStatsMapper viewStatsMapper;

    public void save(EndpointHit endpointHit) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("app", endpointHit.getApp());
        parameters.addValue("uri", endpointHit.getUri());
        parameters.addValue("ip", endpointHit.getIp());
        parameters.addValue("timestamp", endpointHit.getTimestamp());

        namedParameterJdbcTemplate.update(StatQuery.SAVE_HIT, parameters);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String sql = StatQuery.getStringQueryForStats(uris, unique);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("start", start);
        parameters.addValue("end", end);

        if (uris != null && !uris.isEmpty()) {
            parameters.addValue("uris", uris);
        }

        return namedParameterJdbcTemplate.query(sql, parameters, viewStatsMapper);
    }
}
