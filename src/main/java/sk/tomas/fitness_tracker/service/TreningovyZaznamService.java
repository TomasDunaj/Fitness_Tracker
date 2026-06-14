package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.cvik.Cvik;
import sk.tomas.fitness_tracker.model.repository.cvik.CvikRepository;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;
import sk.tomas.fitness_tracker.model.repository.trening.TreningovyZaznamRepository;

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
        if (zaznam.getCvik() == null || zaznam.getCvik().getId() == null) {
            throw new IllegalArgumentException("Tréningový záznam musí obsahovať platné ID cviku.");
        }

        Long cvikId = zaznam.getCvik().getId();
        Cvik existujuciCvik = cvikRepository.findById(cvikId)
                .orElseThrow(() -> new RuntimeException("Nemožno vytvoriť záznam. Cvik s ID " + cvikId + " neexistuje."));

        // Pre istotu priradíme plne načítaný cvik z DB do záznamu
        zaznam.setCvik(existujuciCvik);

        if (zaznam.getSerie() != null) {
            zaznam.getSerie().forEach(seria -> seria.setTreningovyZaznam(zaznam));
        }
        return this.treningovyZaznamRepository.save(zaznam);
    }

    public TreningovyZaznam aktualizujZaznam(Long id, Long cvikId) {
        if (cvikId == null) {
            throw new IllegalArgumentException("Musíš zadať ID cviku na aktualizáciu.");
        }

        TreningovyZaznam existujuciZaznam = treningovyZaznamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Záznam s ID " + id + " sa nenašiel."));

        Cvik novyCvik = cvikRepository.findById(cvikId)
                .orElseThrow(() -> new RuntimeException("Cvik s ID " + cvikId + " neexistuje."));

        existujuciZaznam.setCvik(novyCvik);
        return treningovyZaznamRepository.save(existujuciZaznam);
    }

    public void vymazZaznam(Long id) {
        this.treningovyZaznamRepository.deleteById(id);
    }

    public List<TreningovyZaznam> getZaznamyPreCvik(Long id) {
        return this.treningovyZaznamRepository.findByCvikId(id);
    }
}
