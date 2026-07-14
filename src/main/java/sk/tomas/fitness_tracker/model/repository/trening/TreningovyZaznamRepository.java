package sk.tomas.fitness_tracker.model.repository.trening;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;

import java.util.List;

public interface TreningovyZaznamRepository extends JpaRepository<TreningovyZaznam, Long> {
    List<TreningovyZaznam> findByCvikId(Long cvikId);
    List<TreningovyZaznam> findAllByOrderByIdDesc();
    @Query("SELECT z.cvik.svalovaPartia, COUNT(z) FROM TreningovyZaznam z WHERE z.cvik.svalovaPartia IS NOT NULL GROUP BY z.cvik.svalovaPartia")
    List<Object[]> spocitajZaznamyPodlaPartii();
}
