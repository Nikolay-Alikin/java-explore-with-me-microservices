package com.github.artemlv.ewm.location.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateLocationDto(
        @Positive
        double lat,
        @Positive
        double lon,
        @Positive
        double radius,
        @Length(min = 1, max = 255)
        @NotBlank
        String name
) {
}
