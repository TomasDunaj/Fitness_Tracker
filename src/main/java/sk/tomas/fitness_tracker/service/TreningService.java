package sk.tomas.fitness_tracker.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.SvalovaPartiaStatistika;
import sk.tomas.fitness_tracker.model.enums.SvalovaPartia;
import sk.tomas.fitness_tracker.model.repository.trening.TreningovyZaznamRepository;
import sk.tomas.fitness_tracker.model.trening.Seria;
import sk.tomas.fitness_tracker.model.trening.Trening;
import sk.tomas.fitness_tracker.model.repository.trening.TreningRepository;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreningService {

    private final TreningRepository treningRepository;
    private final TreningovyZaznamRepository treningovyZaznamRepository;

    public TreningService(TreningRepository treningRepository, TreningovyZaznamRepository treningovyZaznamRepository) {
        this.treningRepository = treningRepository;
        this.treningovyZaznamRepository = treningovyZaznamRepository;
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

    public List<SvalovaPartiaStatistika> getSvalovaPartiaStatistiky() {
        List<Object[]> suroveData = treningovyZaznamRepository.spocitajZaznamyPodlaPartii();
        List<SvalovaPartiaStatistika> statistiky = new ArrayList<>();

        for (Object[] riadok : suroveData) {
            SvalovaPartia partiaEnum = (SvalovaPartia) riadok[0];
            String partiaEn = partiaEnum.name();

            long pocet = (long) riadok[1];

            String slovenskyNazov = partiaEn;
            String farba = "#888888";

            switch (partiaEn) {
                case "HRUDNIK":
                    slovenskyNazov = "Hrudník";
                    farba = "#28a745";
                    break;
                case "CHRBAT":
                    slovenskyNazov = "Chrbát";
                    farba = "#ffc107";
                    break;
                case "NOHY":
                    slovenskyNazov = "Nohy";
                    farba = "#007bff";
                    break;
                case "RAMENA":
                    slovenskyNazov = "Ramená";
                    farba = "#dc3545";
                    break;
                case "RUKY":
                    slovenskyNazov = "Ruky";
                    farba = "#17a2b8";
                    break;
            }

            statistiky.add(new SvalovaPartiaStatistika(slovenskyNazov, pocet, farba));
        }

        return statistiky;
    }
}
