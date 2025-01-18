package ru.yandex.practicum.stats.analyzer.service;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface EventSimilarityService {

    void save(EventSimilarityAvro eventSimilarityAvro);
}
