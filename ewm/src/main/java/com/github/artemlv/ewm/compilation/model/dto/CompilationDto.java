package com.github.artemlv.ewm.compilation.model.dto;

import com.github.artemlv.ewm.event.model.dto.EventDto;
import lombok.Builder;

import java.util.Set;

@Builder
public record CompilationDto(
        long id,
        Set<EventDto> events,
        boolean pinned,
        String title
) {
}
