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

    public TreningovyZaznam ulozZaznam(ZaznamRequest request) {
        TreningovyZaznam novyZaznam = new TreningovyZaznam();
        novyZaznam.setVaha(request.getVaha());
        novyZaznam.setPocetSerii(request.getPocetSerii());
        novyZaznam.setPocetOpakovani(request.getPocetOpakovani());
        novyZaznam.setDatum(LocalDate.now());

        if (request.getCvikId() != null) {
            Cvik existujuciCvik = cvikRepository.findById(request.getCvikId())
                    .orElseThrow(() -> new RuntimeException("Cvik sa nenašiel."));
            novyZaznam.setCvik(existujuciCvik);
        }

        return this.treningovyZaznamRepository.save(novyZaznam);
    }

    public TreningovyZaznam aktualizujZaznam(Long id, ZaznamRequest request) {
        TreningovyZaznam existujuciZaznam = treningovyZaznamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Záznam s ID " + id + " sa nenašiel."));

        existujuciZaznam.setVaha(request.getVaha());
        existujuciZaznam.setPocetSerii(request.getPocetSerii());
        existujuciZaznam.setPocetOpakovani(request.getPocetOpakovani());

        if (request.getCvikId() != null) {
            Cvik novyCvik = cvikRepository.findById(request.getCvikId())
                    .orElseThrow(() -> new RuntimeException("Cvik s ID " + request.getCvikId() + " neexistuje."));

            existujuciZaznam.setCvik(novyCvik);
        } else {
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
