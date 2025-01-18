package ru.yandex.practicum.stats.analyzer.service;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionService {

    void save(UserActionAvro userActionAvro);
}
