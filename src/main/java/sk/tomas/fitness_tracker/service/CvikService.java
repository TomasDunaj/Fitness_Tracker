package sk.tomas.fitness_tracker.service;

import org.springframework.stereotype.Service;
import sk.tomas.fitness_tracker.model.Cvik;
import sk.tomas.fitness_tracker.model.CvikRepository;

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
}
