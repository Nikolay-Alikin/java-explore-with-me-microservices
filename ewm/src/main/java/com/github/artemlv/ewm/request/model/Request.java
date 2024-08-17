package com.github.artemlv.ewm.request.model;

import com.github.artemlv.ewm.state.State;
import com.github.artemlv.ewm.event.model.Event;
import com.github.artemlv.ewm.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime created;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State status;
}
