package com.w2m.starships.controllers;

import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.models.filters.StarshipFilter;
import com.w2m.starships.services.StarshipService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "Starship Management", description = "Management API for all starships featured in all types of media", version = "0.1"))
@RestController
@RequestMapping("/api/starships")
@RequiredArgsConstructor
public class StarshipController {

    private final StarshipService starshipService;

    /**
     * GET public method to get all starships that meet the criteria given in filter
     * @param filter Properties of the ships to be retrieved
     * @param pageable Paging parameters
     */
    @Operation(summary = "Get all starships with a given criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All matching starships. The strings in the filter can match only a substring of the starship property", content = {
                    @Content(mediaType = "application/json", schema = @Schema(example = """
                            {
                                "content": [
                                    {
                                        "starshipId": 2,
                                        "starshipName": "USS Río Grande",
                                        "movieName": "Star Trek",
                                        "numberOfPassengers": 4
                                    },
                                    {
                                        "starshipId": 3,
                                        "starshipName": "USS Orinoco",
                                        "movieName": "Star Trek",
                                        "numberOfPassengers": 6
                                    }
                                ],
                                "pageable": {
                                    "pageNumber": 0,
                                    "pageSize": 2,
                                    "sort": {
                                        "sorted": false,
                                        "empty": true,
                                        "unsorted": true
                                    },
                                    "offset": 0,
                                    "paged": true,
                                    "unpaged": false
                                },
                                "last": false,
                                "totalElements": 6,
                                "totalPages": 3,
                                "first": true,
                                "size": 2,
                                "number": 0,
                                "sort": {
                                    "sorted": false,
                                    "empty": true,
                                    "unsorted": true
                                },
                                "numberOfElements": 2,
                                "empty": false
                            }
                            """))
            }),
    })
    @GetMapping
    public Page<StarshipDTO> getStarships(StarshipFilter filter, Pageable pageable) {
        return starshipService.findAll(filter, pageable);
    }


    /**
     * GET public method to get a starship with a given id
     * @param id The id of the starship
     */
    @Operation(summary = "Get one starship with a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The found starship", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StarshipDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "The starship was not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))
            })
    })
    @GetMapping("/{id}")
    public StarshipDTO getStarshipById(@PathVariable("id") Long id) {
        return starshipService.findById(id);
    }

    /**
     * POST public method to create a new starship
     * @param starshipDTO The starship to create
     */
    @Operation(summary = "Create one starship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The created starship", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StarshipDTO.class))
            })
    })
    @PostMapping
    public StarshipDTO createStarship(@Valid @RequestBody StarshipDTO starshipDTO) {
        return starshipService.save(starshipDTO);
    }

    /**
     * PUT public method to update a starship with a given id
     * @param id The given id
     * @param starshipDTO The new starship whose properties are to replace the old one
     */
    @Operation(summary = "Update one starship with a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The updated starship", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StarshipDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "The starship was not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))
            })
    })
    @PutMapping("/{id}")
    public StarshipDTO updateStarship(@PathVariable("id") Long id, @Valid @RequestBody StarshipDTO starshipDTO) {
        return starshipService.update(id, starshipDTO);
    }

    /**
     * DELETE public method to delete a starship with a given id
     * @param id The id of the starship which to delete
     */
    @Operation(summary = "Delete one starship with a given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The deleted starship", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StarshipDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "The starship was not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))
            })
    })
    @DeleteMapping("/{id}")
    public StarshipDTO deleteStarship(@PathVariable("id") Long id) {
        return starshipService.delete(id);
    }



}
