package com.w2m.starships.repositories;

import com.w2m.starships.models.entities.Starship;
import com.w2m.starships.models.filters.StarshipFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StarshipRepository extends JpaRepository<Starship, Long>, JpaSpecificationExecutor<Starship> {

    interface Spec {
        static Specification<Starship> byFilter(StarshipFilter filter) {
            Specification<Starship> spec = Specification.where(null);
            if (filter != null) {
                if (StringUtils.isNotBlank(filter.getStarshipName()))
                    spec = spec.and(likeStarshipName(filter.getStarshipName()));

                if(StringUtils.isNotBlank(filter.getMovieName()))
                    spec = spec.and(likeMovieName(filter.getMovieName()));

                if(filter.getNumberOfPassengers() != null)
                    spec = spec.and(byNumberOfPassengers(filter.getNumberOfPassengers()));

            }
            return spec;
        }

        static Specification<Starship> likeStarshipName(String starshipName) {
            return (root, query, cb) -> cb.like(cb.lower(root.get("starshipName")), ("%" + starshipName + "%").toLowerCase());
        }

        static Specification<Starship> likeMovieName(String movieName) {
            return (root, query, cb) -> cb.like(cb.lower(root.get("movieName")), ("%" + movieName + "%").toLowerCase());
        }

        static Specification<Starship> byNumberOfPassengers(Integer numberOfPassengers) {
            return (root, query, cb) -> cb.equal(root.get("numberOfPassengers"), numberOfPassengers);
        }
    }
}
