package com.w2m.starships.models.exceptions;

import lombok.Getter;

@Getter
public class StarshipNotFoundException extends RuntimeException {

    private StarshipNotFoundException(Long id){
        super("Starship with id " + id + " not found");
        this.id = id;
    }
    private final Long id;

    public static StarshipNotFoundException fromId(Long id) {
        return new StarshipNotFoundException(id);
    }
}
