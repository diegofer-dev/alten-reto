package com.w2m.starships.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "STARSHIPS")
public class Starship {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "starships_generator")
    @SequenceGenerator(name = "starships_generator", sequenceName = "starships_seq", allocationSize = 1)

    @EqualsAndHashCode.Include()
    private Long starshipId;

    @Column(nullable = false)
    private String starshipName;

    @Column(nullable = false)
    private String movieName;

    private Integer numberOfPassengers;
}
