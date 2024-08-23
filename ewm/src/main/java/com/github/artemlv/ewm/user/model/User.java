package com.github.artemlv.ewm.user.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @EqualsAndHashCode.Include
    @Column(nullable = false, unique = true, length = 254)
    private String email;
    @Column(nullable = false, length = 250)
    private String name;
}
