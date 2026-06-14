package sk.tomas.fitness_tracker.model.repository.trening;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.fitness_tracker.model.trening.Trening;

public interface TreningRepository extends JpaRepository<Trening, Long> {
}
