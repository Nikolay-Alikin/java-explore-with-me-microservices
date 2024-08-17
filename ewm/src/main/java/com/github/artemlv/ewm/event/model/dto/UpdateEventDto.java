package com.github.artemlv.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.artemlv.ewm.location.model.dto.LocationLatAndLonDto;
import com.github.artemlv.ewm.state.StateAction;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record UpdateEventDto(
        @Length(max = 500)
        String annotation,
        long category,
        @Length(max = 1500)
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,
        LocationLatAndLonDto location,
        boolean paid,
        @PositiveOrZero
        int participantLimit,
        boolean requestModeration,
        StateAction stateAction,
        @Length(min = 1, max = 120)
        String title
) {
}
