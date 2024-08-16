package com.github.artemlv.ewm.location.model.dto;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UpdateLocationDto(
        double lat,
        double lon,
        double radius,
        @Length(max = 255)
        String name
) {
}
