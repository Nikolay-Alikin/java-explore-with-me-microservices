package ru.yandex.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.yandex.practicum.category.model.dto.CategoryDto;
import lombok.Builder;
import ru.yandex.practicum.location.model.dto.LocationDto;
import ru.yandex.practicum.state.State;
import ru.yandex.practicum.user.model.dto.UserWithoutEmailDto;

import java.time.LocalDateTime;

@Builder
public record EventFullDto(
        long id,
        String annotation,
        CategoryDto category,
        int confirmedRequests,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdOn,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,
        UserWithoutEmailDto initiator,
        LocationDto location,
        boolean paid,
        int participantLimit,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime publishedOn,
        boolean requestModeration,
        State state,
        String title,
        long views
) {
}
