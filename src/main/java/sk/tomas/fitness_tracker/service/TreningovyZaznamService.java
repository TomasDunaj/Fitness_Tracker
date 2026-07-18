package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.dto.OsobnyRekord;
import sk.tomas.fitness_tracker.model.cvik.Cvik;
import sk.tomas.fitness_tracker.model.repository.cvik.CvikRepository;
import sk.tomas.fitness_tracker.model.repository.trening.TreningRepository;
import sk.tomas.fitness_tracker.model.trening.Seria;
import sk.tomas.fitness_tracker.model.trening.Trening;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;
import sk.tomas.fitness_tracker.model.repository.trening.TreningovyZaznamRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreningovyZaznamService {

    private final TreningovyZaznamRepository treningovyZaznamRepository;
    private final CvikRepository cvikRepository;
    private final TreningRepository treningRepository;

    public TreningovyZaznamService(TreningovyZaznamRepository treningovyZaznamRepository, CvikRepository cvikRepository, TreningRepository treningRepository) {
        this.treningovyZaznamRepository = treningovyZaznamRepository;
        this.cvikRepository = cvikRepository;
        this.treningRepository = treningRepository;
    }

    public List<TreningovyZaznam> getVsetkyZaznamy() {
        return this.treningovyZaznamRepository.findAllByOrderByIdDesc();
    }

    public TreningovyZaznam ulozZaznam(Long treningId, TreningovyZaznam zaznam) {
        if (zaznam.getCvik() == null || zaznam.getCvik().getNazovCviku() == null) {
            throw new IllegalArgumentException("Tréningový záznam musí obsahovať platný cvik s názvom.");
        }

        Trening existujuciTrening = treningRepository.findById(treningId)
                .orElseThrow(() -> new RuntimeException("Tréning s id : " + treningId + " sa nenašiel."));
        zaznam.setTrening(existujuciTrening);

        String nazov = zaznam.getCvik().getNazovCviku();
        Cvik finalnyCvik = cvikRepository.findByNazovCviku(nazov)
                .orElseGet(() -> {
                    // Ak cvik neexistuje, uložíme ho ako nový do databázy
                    System.out.println("Cvik s názvom '" + nazov + "' sa nenašiel, vytváram nový.");
                    return cvikRepository.save(zaznam.getCvik());
                });

        zaznam.setCvik(finalnyCvik);

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

    public TreningovyZaznam aktualizujStav(Long id) {
        TreningovyZaznam zaznam = this.treningovyZaznamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Záznam s ID " + id + " sa nenašiel."));

        boolean aktualnyStav = (zaznam.isSplnene() != null) ? zaznam.isSplnene() : false;

        zaznam.setSplnene(!aktualnyStav);

        return treningovyZaznamRepository.save(zaznam);
    }

    public List<OsobnyRekord> dajOsobneRekordy() {
        List<TreningovyZaznam> vsetkyZaznamy = this.treningovyZaznamRepository.findAll();

        Map<String, Optional<TreningovyZaznam>> maximaPodlaCviku = vsetkyZaznamy.stream()
                .collect(Collectors.groupingBy(
                        zaznam -> zaznam.getCvik().getNazovCviku(),
                        Collectors.maxBy(Comparator.comparingDouble(this::ziskajMaxVahuZoZaznamu))
                ));

        return maximaPodlaCviku.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .map(entry -> {
                    TreningovyZaznam najlepsiZaznam = entry.getValue().get();
                    double maxVaha = ziskajMaxVahuZoZaznamu(najlepsiZaznam);

                    return new OsobnyRekord(entry.getKey(), maxVaha);
                })
                .collect(Collectors.toList());

    }

    private double ziskajMaxVahuZoZaznamu(TreningovyZaznam zaznam) {
        return zaznam.getSerie().stream()
                .mapToDouble(Seria::getVaha)
                .max()
                .orElse(0);
    }
}
