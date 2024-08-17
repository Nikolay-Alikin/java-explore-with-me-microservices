package com.github.artemlv.ewm.request.model.dto;

import com.github.artemlv.ewm.state.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateRequestByIdsDto(
        @NotEmpty
        List<Long> requestIds,
        @NotNull
        State status
) {
}
