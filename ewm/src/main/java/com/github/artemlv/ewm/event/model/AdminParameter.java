package com.github.artemlv.ewm.event.model;

import com.github.artemlv.ewm.event.validation.EventStartDateBeforeEndDate;
import com.github.artemlv.ewm.state.State;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@EventStartDateBeforeEndDate
public class AdminParameter {
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;

    @PositiveOrZero
    private int from = 0;

    @Positive
    private int size = 10;
}
