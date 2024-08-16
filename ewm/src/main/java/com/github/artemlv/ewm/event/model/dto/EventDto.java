package com.github.artemlv.ewm.event.model.dto;

import com.github.artemlv.ewm.category.model.Category;
import com.github.artemlv.ewm.location.model.dto.LocationLatAndLonDto;
import com.github.artemlv.ewm.state.State;
import com.github.artemlv.ewm.user.model.dto.UserWithoutEmailDto;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventDto(
        long id,
        String annotation,
        Category category,
        int confirmedRequests,
        LocalDateTime createdOn,
        String description,
        LocalDateTime eventDate,
        UserWithoutEmailDto initiator,
        LocationLatAndLonDto location,
        boolean paid,
        int participantLimit,
        LocalDateTime publishedOn,
        boolean requestModeration,
        State state,
        String title,
        long views
) {
}
