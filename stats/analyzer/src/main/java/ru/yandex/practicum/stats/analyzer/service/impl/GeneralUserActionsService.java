package ru.yandex.practicum.stats.analyzer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.stats.analyzer.repository.UserActionRepository;
import ru.yandex.practicum.stats.analyzer.service.UserActionService;
import ru.yandex.practicum.stats.analyzer.service.mapper.UserActionMapper;

@Service
@RequiredArgsConstructor
public class GeneralUserActionsService implements UserActionService {

    private final UserActionRepository userActionRepository;
    private final UserActionMapper userActionMapper;

    @Override
    public void save(UserActionAvro userActionAvro) {
        userActionRepository.save(
                userActionMapper.toUserAction(userActionAvro)
        );
    }
}
