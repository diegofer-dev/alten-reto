package com.w2m.starships.models.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarshipFilter {
    private String starshipName;
    private String movieName;
    private Integer numberOfPassengers;
}
