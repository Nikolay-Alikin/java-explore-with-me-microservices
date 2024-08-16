package com.github.artemlv.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.artemlv.ewm.event.validation.ConstraintFutureInTwoHours;
import com.github.artemlv.ewm.location.model.dto.LocationLatAndLonDto;
import com.github.artemlv.ewm.state.StateAction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record UpdateEventDto(
        @Length(min = 1, max = 500)
        @NotBlank
        String annotation,
        @Positive
        long category,
        @Length(min = 1, max = 1500)
        @NotBlank
        String description,
        @NotNull
        @ConstraintFutureInTwoHours
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,
        @NotNull
        LocationLatAndLonDto location,
        boolean paid,
        @PositiveOrZero
        int participantLimit,
        boolean requestModeration,
        StateAction stateAction,
        @Length(min = 1, max = 120)
        @NotBlank
        String title
) {
}
