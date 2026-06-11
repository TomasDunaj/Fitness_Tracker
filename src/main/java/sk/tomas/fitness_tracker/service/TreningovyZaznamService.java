package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.TreningovyZaznam;
import sk.tomas.fitness_tracker.model.TreningovyZaznamRepository;

import java.util.List;

@Service
public class TreningovyZaznamService {

    private final TreningovyZaznamRepository treningovyZaznamRepository;

    public TreningovyZaznamService(TreningovyZaznamRepository treningovyZaznamRepository) {
        this.treningovyZaznamRepository = treningovyZaznamRepository;
    }

    public List<TreningovyZaznam> getVsetkyZaznamy() {
        return treningovyZaznamRepository.findAll();
    }

    public TreningovyZaznam ulozZaznam(TreningovyZaznam novyZaznam) {
        return this.treningovyZaznamRepository.save(novyZaznam);
    }
}
