package com.github.artemlv.stats.repository;

import dto.ViewStats;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ViewStatsMapper implements RowMapper<ViewStats> {

    @Override
    public ViewStats mapRow(ResultSet rs, int rowNum) throws SQLException {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(rs.getString("app"));
        viewStats.setUri(rs.getString("uri"));
        viewStats.setHits(rs.getLong("hits"));
        return viewStats;
    }
}
