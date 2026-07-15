package sk.tomas.fitness_tracker.model.repository.cvik;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.fitness_tracker.model.cvik.Cvik;

import java.util.Optional;

public interface CvikRepository extends JpaRepository<Cvik, Long> {
    Optional<Cvik> findByNazovCviku(String nazovCviku);
}
