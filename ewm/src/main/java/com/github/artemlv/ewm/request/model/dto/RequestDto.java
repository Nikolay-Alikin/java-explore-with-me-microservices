package com.github.artemlv.ewm.request.model.dto;

import com.github.artemlv.ewm.state.State;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RequestDto(
        long id,
        LocalDateTime created,
        long event,
        long requester,
        State status
) {

}
