package ru.yandex.practicum.recomendation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RecommendationDto {

    private long eventId;
    private double score;
}
