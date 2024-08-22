package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateStatsDto;
import ru.yandex.practicum.dto.StatCountHitsDto;
import ru.yandex.practicum.dto.StatsResponseHitDto;
import ru.yandex.practicum.SearchStats;

import java.util.List;

public interface StatsService {
    StatsResponseHitDto save(CreateStatsDto hit);

    List<StatCountHitsDto> getStats(SearchStats searchStats);
}
