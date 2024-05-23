package com.w2m.starships.mappers;

import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.models.entities.Starship;

public class StarshipMapper {

    public static StarshipDTO mapToStarshipDTO(Starship starship) {
        return StarshipDTO.builder()
                .starshipId(starship.getStarshipId())
                .starshipName(starship.getStarshipName())
                .movieName(starship.getMovieName())
                .numberOfPassengers(starship.getNumberOfPassengers())
                .build();
    }

    public static Starship mapToStarship(StarshipDTO starshipDTO) {
        return Starship.builder()
                .starshipName(starshipDTO.getStarshipName())
                .movieName(starshipDTO.getMovieName())
                .numberOfPassengers(starshipDTO.getNumberOfPassengers())
                .build();
    }

    public static void mapToStarshipUpdate(Starship starship, StarshipDTO starshipDTO) {
        starship.setStarshipName(starshipDTO.getStarshipName());
        starship.setMovieName(starshipDTO.getMovieName());
        starship.setNumberOfPassengers(starshipDTO.getNumberOfPassengers());
    }
}
