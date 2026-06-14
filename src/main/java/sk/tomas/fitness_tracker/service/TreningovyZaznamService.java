package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.Cvik;
import sk.tomas.fitness_tracker.model.CvikRepository;
import sk.tomas.fitness_tracker.model.TreningovyZaznam;
import sk.tomas.fitness_tracker.model.TreningovyZaznamRepository;
import sk.tomas.fitness_tracker.model.ZaznamRequest;

import java.time.LocalDate;
import java.util.List;

@Service
public class TreningovyZaznamService {

    private final TreningovyZaznamRepository treningovyZaznamRepository;
    private final CvikRepository cvikRepository;

    public TreningovyZaznamService(TreningovyZaznamRepository treningovyZaznamRepository, CvikRepository cvikRepository) {
        this.treningovyZaznamRepository = treningovyZaznamRepository;
        this.cvikRepository = cvikRepository;
    }

    public List<TreningovyZaznam> getVsetkyZaznamy() {
        return this.treningovyZaznamRepository.findAll();
    }

    public TreningovyZaznam ulozZaznam(TreningovyZaznam zaznam) {
        if (zaznam.getSerie() != null) {
            zaznam.getSerie().forEach(seria -> seria.setTreningovyZaznam(zaznam));
        }
        return this.treningovyZaznamRepository.save(zaznam);
    }

    public TreningovyZaznam aktualizujZaznam(Long id, ZaznamRequest request) {
        TreningovyZaznam existujuciZaznam = treningovyZaznamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Záznam s ID " + id + " sa nenašiel."));


        if (request.getCvikId() != null) {
            Cvik novyCvik = cvikRepository.findById(request.getCvikId())
                    .orElseThrow(() -> new RuntimeException("Cvik s ID " + request.getCvikId() + " neexistuje."));

            existujuciZaznam.setCvik(novyCvik);
        }
        else {
            existujuciZaznam.setCvik(null);
        }

        return treningovyZaznamRepository.save(existujuciZaznam);
    }

    public void vymazZaznam(Long id) {
        this.treningovyZaznamRepository.deleteById(id);
    }

    public List<TreningovyZaznam> getZaznamyPreCvik(Long id) {
        return this.treningovyZaznamRepository.findByCvikId(id);
    }
}
