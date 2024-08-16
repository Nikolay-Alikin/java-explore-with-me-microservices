package com.github.artemlv.ewm.request.model;

import com.github.artemlv.ewm.state.State;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateRequestByIdsDto(
        List<Long> requestIds,
        State status
) {
}
