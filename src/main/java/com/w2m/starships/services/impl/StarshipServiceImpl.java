package com.w2m.starships.services.impl;

import com.w2m.starships.amqp.producers.MessageProducer;
import com.w2m.starships.mappers.StarshipMapper;
import com.w2m.starships.models.dto.StarshipDTO;
import com.w2m.starships.models.entities.Starship;
import com.w2m.starships.models.exceptions.StarshipNotFoundException;
import com.w2m.starships.models.filters.StarshipFilter;
import com.w2m.starships.repositories.StarshipRepository;
import com.w2m.starships.services.StarshipService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class StarshipServiceImpl implements StarshipService {


    private final StarshipRepository starshipRepository;
    private final MessageProducer messageProducer;

    @Override
    @Cacheable("starships")
    public Page<StarshipDTO> findAll(StarshipFilter filter, Pageable pageable) {
        final Page<Starship> result = starshipRepository.findAll(StarshipRepository.Spec.byFilter(filter), pageable);
        return new PageImpl<>(result.stream().map(StarshipMapper::mapToStarshipDTO).toList(), pageable, result.getTotalElements());
    }

    @Override
    @Cacheable(value = "starships")
    public StarshipDTO findById(Long id) {
        final Starship result = starshipRepository.findById(id)
                .orElseThrow(() -> StarshipNotFoundException.fromId(id));
        return StarshipMapper.mapToStarshipDTO(result);
    }

    @Override
    @Transactional
    @CacheEvict(value = "starships", allEntries = true)
    public StarshipDTO save(StarshipDTO starshipDTO) {
        StarshipDTO newStarship = StarshipMapper.mapToStarshipDTO(starshipRepository.save(StarshipMapper.mapToStarship(starshipDTO)));
        messageProducer.notifyNewStarship(newStarship);
        return newStarship;
    }

    @Override
    @Transactional
    @CacheEvict(value = "starships", allEntries = true)
    public StarshipDTO update(Long id, StarshipDTO starshipDTO) {
        final Starship starship = starshipRepository.findById(id)
                .orElseThrow(() -> StarshipNotFoundException.fromId(id));
        StarshipMapper.mapToStarshipUpdate(starship, starshipDTO);
        return StarshipMapper.mapToStarshipDTO(starshipRepository.save(starship));
    }

    @Override
    @Transactional
    @CacheEvict(value = "starships", allEntries = true)
    public StarshipDTO delete(Long id) {
        final Starship starship = starshipRepository.findById(id)
                .orElseThrow(() -> StarshipNotFoundException.fromId(id));
        starshipRepository.delete(starship);
        return StarshipMapper.mapToStarshipDTO(starship);
    }
}
