package sk.tomas.fitness_tracker.model.repository.trening;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;

import java.util.List;

public interface TreningovyZaznamRepository extends JpaRepository<TreningovyZaznam, Long> {
    List<TreningovyZaznam> findByCvikId(Long cvikId);
}
