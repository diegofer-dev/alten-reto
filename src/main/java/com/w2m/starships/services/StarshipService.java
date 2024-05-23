package com.w2m.starships.services;

import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.models.filters.StarshipFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StarshipService {

    /**
     * Return the specified page of all the starships that meets the criteria given in the filter 
     * @param filter The filter that stores the criteria 
     * @param pageable The paging specifications
     */
    Page<StarshipDTO> findAll(StarshipFilter filter, Pageable pageable);

    /**
     * Return the starship with given id
     * @param id The starship's id
     */
    StarshipDTO findById(Long id);

    /**
     * Save a new starship with the properties given in the DTO
     * @param starshipDTO The DTO that stores the properties of the new starship
     */
    StarshipDTO save(StarshipDTO starshipDTO);

    /**
     * Update an existing starship with the properties given in the DTO
     * @param id The id of the existing starship
     * @param starshipDTO The DTO that stores the properties of the new starship
     */
    StarshipDTO update(Long id, StarshipDTO starshipDTO);

    /**
     * Delete an existing starship with given id
     * @param id The starship's id
     */
    StarshipDTO delete(Long id);
}
