package sk.tomas.fitness_tracker.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreningovyZaznamRepository extends JpaRepository<TreningovyZaznam, Long> {
    List<TreningovyZaznam> findByCvikId(Long cvikId);
}
