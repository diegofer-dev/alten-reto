package com.w2m.starships.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class StarshipDTO implements Serializable {
    private Long starshipId;

    @NotBlank(message = "'starshipName' can't be null or empty")
    private String starshipName;

    @NotBlank(message = "'movieName' can't be null or empty")
    private String movieName;

    private Integer numberOfPassengers;
}
