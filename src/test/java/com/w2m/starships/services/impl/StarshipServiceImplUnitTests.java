package com.w2m.starships.services.impl;

import com.w2m.starships.amqp.producers.MessageProducer;
import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.models.entities.Starship;
import com.w2m.starships.models.exceptions.StarshipNotFoundException;
import com.w2m.starships.models.filters.StarshipFilter;
import com.w2m.starships.repositories.StarshipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StarshipServiceImplUnitTests {

    private static final Starship TEST_STARSHIP = Starship.builder()
            .starshipName("Test name 2")
            .movieName("Test movie 2")
            .numberOfPassengers(8)
            .build();

    private static final StarshipDTO TEST_STARSHIP_DTO = StarshipDTO.builder()
            .starshipName(TEST_STARSHIP.getStarshipName())
            .movieName(TEST_STARSHIP.getMovieName())
            .numberOfPassengers(TEST_STARSHIP.getNumberOfPassengers())
            .build();

    @Mock
    private StarshipRepository starshipRepository;

    @Mock
    private MessageProducer messageProducer;

    @InjectMocks
    private StarshipServiceImpl starshipService;



    @Test
    void givenValidFilterAndPageable_whenFindAll_thenReturnsStarshipDTOPage() {
        StarshipFilter filter = new StarshipFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Starship> page = new PageImpl<>(List.of(TEST_STARSHIP));

        when(starshipRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<StarshipDTO> result = starshipService.findAll(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(starshipRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void givenValidId_whenFindById_thenReturnsStarshipDTO() {
        when(starshipRepository.findById(1L)).thenReturn(Optional.of(TEST_STARSHIP));

        StarshipDTO result = starshipService.findById(1L);

        assertNotNull(result);
        assertEquals(TEST_STARSHIP_DTO.getStarshipName(), result.getStarshipName());
        verify(starshipRepository).findById(1L);
    }

    @Test
    void givenInvalidId_whenFindById_thenThrowsStarshipNotFoundException() {
        when(starshipRepository.findById(1L)).thenThrow(StarshipNotFoundException.class);

        assertThrows(StarshipNotFoundException.class, () -> starshipService.findById(1L));
        verify(starshipRepository).findById(1L);
    }

    @Test
    void givenStarshipDTO_whenSave_thenReturnsSavedStarshipDTO() {
        when(starshipRepository.save(any())).thenReturn(TEST_STARSHIP);

        StarshipDTO result = starshipService.save(TEST_STARSHIP_DTO);

        assertNotNull(result);
        assertEquals(TEST_STARSHIP_DTO.getStarshipName(), result.getStarshipName());
        verify(starshipRepository).save(any());
        verify(messageProducer).notifyNewStarship(any());
    }

    @Test
    void givenValidIdAndStarshipDTO_whenUpdate_thenReturnsUpdatedStarshipDTO() {
        when(starshipRepository.findById(1L)).thenReturn(Optional.of(TEST_STARSHIP));
        when(starshipRepository.save(any())).thenReturn(TEST_STARSHIP);

        StarshipDTO result = starshipService.update(1L, TEST_STARSHIP_DTO);

        assertNotNull(result);
        assertEquals(TEST_STARSHIP_DTO.getStarshipName(), result.getStarshipName());
        verify(starshipRepository).findById(1L);
        verify(starshipRepository).save(any());
    }

    @Test
    void givenInvalidId_whenUpdate_thenThrowsStarshipNotFoundException() {
        when(starshipRepository.findById(1L)).thenThrow(StarshipNotFoundException.class);

        assertThrows(StarshipNotFoundException.class, () -> starshipService.update(1L, TEST_STARSHIP_DTO));
        verify(starshipRepository).findById(1L);
    }

    @Test
    void givenValidId_whenDelete_thenReturnsDeletedStarshipDTO() {
        when(starshipRepository.findById(1L)).thenReturn(Optional.of(TEST_STARSHIP));

        StarshipDTO result = starshipService.delete(1L);

        assertNotNull(result);
        assertEquals(TEST_STARSHIP_DTO.getStarshipName(), result.getStarshipName());
        verify(starshipRepository).findById(1L);
        verify(starshipRepository).delete(TEST_STARSHIP);
    }

    @Test
    void givenInvalidId_whenDelete_thenThrowsStarshipNotFoundException() {
        when(starshipRepository.findById(1L)).thenThrow(StarshipNotFoundException.class);

        assertThrows(StarshipNotFoundException.class, () -> starshipService.delete(1L));
        verify(starshipRepository).findById(1L);
    }
}
