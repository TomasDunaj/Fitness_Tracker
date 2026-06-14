package sk.tomas.fitness_tracker.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TreningRepository extends JpaRepository<Trening, Long> {
}
