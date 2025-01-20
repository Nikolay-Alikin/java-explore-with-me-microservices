package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.recomendation.RecommendationDto;

public interface RecommendationService {

    List<RecommendationDto> getRecommendations(long userId, int size);
}
