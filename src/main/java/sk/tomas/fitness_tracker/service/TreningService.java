package sk.tomas.fitness_tracker.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.dto.CvikProgresStatistika;
import sk.tomas.fitness_tracker.dto.SvalovaPartiaStatistika;
import sk.tomas.fitness_tracker.model.cvik.Cvik;
import sk.tomas.fitness_tracker.model.enums.SvalovaPartia;
import sk.tomas.fitness_tracker.model.repository.cvik.CvikRepository;
import sk.tomas.fitness_tracker.model.repository.trening.TreningovyZaznamRepository;
import sk.tomas.fitness_tracker.model.trening.Seria;
import sk.tomas.fitness_tracker.model.trening.Trening;
import sk.tomas.fitness_tracker.model.repository.trening.TreningRepository;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TreningService {

    private final TreningRepository treningRepository;
    private final TreningovyZaznamRepository treningovyZaznamRepository;
    private final CvikRepository cvikRepository;

    public TreningService(TreningRepository treningRepository, TreningovyZaznamRepository treningovyZaznamRepository, CvikRepository cvikRepository) {
        this.treningRepository = treningRepository;
        this.treningovyZaznamRepository = treningovyZaznamRepository;
        this.cvikRepository = cvikRepository;
    }

    @Transactional
    public Trening ulozTrening(Trening trening) {
        if (trening.getZaznamy() != null) {
            for (TreningovyZaznam zaznam : trening.getZaznamy()) {
                zaznam.setTrening(trening);

                Cvik cvikZFrontendu = zaznam.getCvik();

                Cvik finalnyCvik = cvikRepository.findByNazovCviku(cvikZFrontendu.getNazovCviku())
                        .orElseGet(() -> {
                            Cvik novyCvik = new Cvik();
                            novyCvik.setNazovCviku(cvikZFrontendu.getNazovCviku());
                            novyCvik.setSvalovaPartia(cvikZFrontendu.getSvalovaPartia());
                            return cvikRepository.save(novyCvik);
                        });

                zaznam.setCvik(finalnyCvik);

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

    public List<CvikProgresStatistika> najdiZaznamyPreCvik(Long cvikId) {
        List<TreningovyZaznam> zaznamy = treningovyZaznamRepository.najdiZaznamyPreCvikSorted(cvikId);

        Map<LocalDate, Double> maxVahaPoDnoch = new HashMap<>();

        for (TreningovyZaznam zaznam : zaznamy) {
            LocalDate datum = zaznam.getTrening().getDatum();
            double maxVaha = 0;

            if (zaznam.getSerie() != null) {
                for (Seria seria : zaznam.getSerie()) {
                    if (seria.getVaha() > maxVaha) {
                        maxVaha = seria.getVaha();
                    }
                }
            }

            if (maxVaha > 0) {
                double doterajsieMaximum = maxVahaPoDnoch.getOrDefault(datum, 0.0);

                if (maxVaha > doterajsieMaximum) {
                    maxVahaPoDnoch.put(datum, maxVaha);
                }
            }
        }

        List<CvikProgresStatistika> statistiky = new ArrayList<>();
        for(Map.Entry<LocalDate, Double> entry : maxVahaPoDnoch.entrySet()) {
            statistiky.add(new CvikProgresStatistika(entry.getKey(), entry.getValue()));
        }

        statistiky.sort(Comparator.comparing(CvikProgresStatistika::getDatum));

        return statistiky;
    }

    public Trening getAleboVytvorDnesnyTrening(LocalDate datum) {
        LocalDate dnes = LocalDate.now();

        return treningRepository.findByDatum(dnes)
                .orElseGet(() -> {
                        Trening novyTrening = new Trening();
                        novyTrening.setDatum(dnes);
                        return treningRepository.save(novyTrening);
                });
    }
}
