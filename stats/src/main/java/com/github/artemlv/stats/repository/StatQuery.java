package com.github.artemlv.stats.repository;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class StatQuery {

    public static final String SAVE_HIT = "INSERT INTO endpoint_hits (app, uri, ip, timestamp) " +
            "VALUES (:app, :uri, :ip, :timestamp)";

    public static String getStringQueryForStats(List<String> uris, Boolean unique) {
        return "SELECT app, uri, COUNT(" + (unique ? "DISTINCT ip" : "ip") + ") AS hits " +
                "FROM endpoint_hits " +
                "WHERE timestamp BETWEEN :start AND :end " +
                (uris != null && !uris.isEmpty() ? "AND uri IN (:uris) " : "") +
                "GROUP BY app, uri " +
                "ORDER BY hits DESC";
    }
}
