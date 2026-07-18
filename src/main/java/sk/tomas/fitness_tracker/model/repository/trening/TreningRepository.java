package sk.tomas.fitness_tracker.model.repository.trening;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.fitness_tracker.model.trening.Trening;

import java.time.LocalDate;
import java.util.Optional;

public interface TreningRepository extends JpaRepository<Trening, Long> {
    Optional<Trening> findByDatum(LocalDate datum);
}
