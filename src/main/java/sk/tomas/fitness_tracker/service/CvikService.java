package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.cvik.Cvik;
import sk.tomas.fitness_tracker.model.repository.cvik.CvikRepository;

import java.util.List;

@Service
public class CvikService {

    private final CvikRepository cvikRepository;

    public CvikService(CvikRepository cvikRepository) {
        this.cvikRepository = cvikRepository;
    }

    public List<Cvik> getVsetkyCviky() {
        return this.cvikRepository.findAll();
    }

    public Cvik ulozCvik(Cvik cvik) {
        return this.cvikRepository.save(cvik);
    }

    public Cvik aktualizujCvik(Long id, Cvik aktualizovanyCvik) {
        Cvik existujuci = cvikRepository.findById(id).orElseThrow(() -> new RuntimeException("Cvik s ID : " + id + " sa nenašiel."));

        existujuci.setNazovCviku(aktualizovanyCvik.getNazovCviku());
        existujuci.setSvalovaPartia(aktualizovanyCvik.getSvalovaPartia());

        return cvikRepository.save(existujuci);
    }

    public void vymazCvik(Long id) {
        if (!this.cvikRepository.existsById(id)) {
            throw new RuntimeException("Cvik s ID : " + id + " neexistuje.");
        }
        this.cvikRepository.deleteById(id);
    }
}
