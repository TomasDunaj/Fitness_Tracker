package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.TreningovyZaznam;
import sk.tomas.fitness_tracker.model.TreningovyZaznamRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TreningovyZaznamService {

    private final TreningovyZaznamRepository treningovyZaznamRepository;

    public TreningovyZaznamService(TreningovyZaznamRepository treningovyZaznamRepository) {
        this.treningovyZaznamRepository = treningovyZaznamRepository;
    }

    public List<TreningovyZaznam> getVsetkyZaznamy() {
        return this.treningovyZaznamRepository.findAllByOrderByDatumDesc();
    }

    public TreningovyZaznam ulozZaznam(TreningovyZaznam novyZaznam) {
        novyZaznam.setDatum(LocalDate.now());
        return this.treningovyZaznamRepository.save(novyZaznam);
    }

    public TreningovyZaznam aktualizujzaznam(long id, TreningovyZaznam noveData) {
        return this.treningovyZaznamRepository.findById(id).map(zaznam -> {
            zaznam.setNazovCviku(noveData.getNazovCviku());
            zaznam.setVaha(noveData.getVaha());
            zaznam.setPocetOpakovani(noveData.getPocetOpakovani());
            zaznam.setPocetSerii(noveData.getPocetSerii());
            zaznam.setDatum(noveData.getDatum());

            return this.treningovyZaznamRepository.save(zaznam);

        }).orElseThrow(() -> new RuntimeException("Záznam s id : " + id + " sa nenašiel."));
    }

    public void vymazZaznam(long id) {
        this.treningovyZaznamRepository.deleteById(id);
    }
}
