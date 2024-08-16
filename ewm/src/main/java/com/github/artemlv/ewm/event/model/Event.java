package com.github.artemlv.ewm.event.model;

import com.github.artemlv.ewm.category.model.Category;
import com.github.artemlv.ewm.location.model.Location;
import com.github.artemlv.ewm.state.State;
import com.github.artemlv.ewm.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;

    @Column(nullable = false)
    private int confirmedRequests;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Location location;

    @Column(nullable = false)
    private boolean paid;

    @Column(nullable = false)
    private int participantLimit;

    @Column(nullable = false)
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    private boolean requestModeration;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private long views;
}
