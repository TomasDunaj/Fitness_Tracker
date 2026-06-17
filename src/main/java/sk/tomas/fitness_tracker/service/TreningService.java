package sk.tomas.fitness_tracker.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.trening.Seria;
import sk.tomas.fitness_tracker.model.trening.Trening;
import sk.tomas.fitness_tracker.model.repository.trening.TreningRepository;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;

import java.util.List;

@Service
public class TreningService {

    private final TreningRepository treningRepository;

    public TreningService(TreningRepository treningRepository) {
        this.treningRepository = treningRepository;
    }

    @Transactional
    public Trening ulozTrening(Trening trening) {
        if (trening.getZaznamy() != null) {
            for (TreningovyZaznam zaznam : trening.getZaznamy()) {
                zaznam.setTrening(trening);

                if(zaznam.getSerie() != null) {
                    for (Seria seria : zaznam.getSerie()) {
                        seria.setTreningovyZaznam(zaznam);
                    }
                }
            }
        }

        return treningRepository.save(trening);
    }

    public List<Trening> getVsetkyTreningy() {
        return this.treningRepository.findAll();
    }

    public void vymazTrening(Long id) {
        this.treningRepository.deleteById(id);
    }
}
