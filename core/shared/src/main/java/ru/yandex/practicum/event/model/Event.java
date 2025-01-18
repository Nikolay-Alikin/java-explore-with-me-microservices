package ru.yandex.practicum.event.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.yandex.practicum.state.State;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(name = "category_id", nullable = false)
    private Long category;

    @Column(nullable = false)
    private int confirmedRequests;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false, length = 7000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @ToString.Exclude
    @Column(name = "initiator_id", nullable = false)
    private long initiator;

    @ToString.Exclude
    @Column(name = "location_id", nullable = false)
    private long location;

    @Column(nullable = false)
    private boolean paid;

    @Column(nullable = false)
    private int participantLimit;

    @Column
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    private boolean requestModeration;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false)
    private long views;

    @Transient
    Double rating;
}
