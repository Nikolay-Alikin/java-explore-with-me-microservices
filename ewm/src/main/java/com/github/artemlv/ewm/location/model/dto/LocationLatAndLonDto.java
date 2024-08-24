package com.github.artemlv.ewm.location.model.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record LocationLatAndLonDto(
        double lat,
        double lon
) {
}
