package sk.tomas.fitness_tracker.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.dto.CvikProgresStatistika;
import sk.tomas.fitness_tracker.dto.SvalovaPartiaStatistika;
import sk.tomas.fitness_tracker.model.repository.trening.TreningovyZaznamRepository;
import sk.tomas.fitness_tracker.model.trening.Trening;
import sk.tomas.fitness_tracker.service.TreningService;

import java.util.List;

@RestController
@RequestMapping("/api/treningy")
@CrossOrigin(origins = "http://localhost:5173")
public class TreningController {
    private final TreningService treningService;

    public TreningController(TreningService treningService) {
        this.treningService = treningService;
    }

    @PostMapping
    public Trening ulozTrening(@RequestBody Trening trening) {
        return this.treningService.ulozTrening(trening);
    }

    @GetMapping
    public List<Trening> getTreningy() {
        return this.treningService.getVsetkyTreningy();
    }

    @DeleteMapping("/{id}")
    public void vymazTrening(@PathVariable Long id) {
        this.treningService.vymazTrening(id);
    }

    @GetMapping("/statistiky/partie")
    public List<SvalovaPartiaStatistika> getStatistikyPartii() {
        return this.treningService.getSvalovaPartiaStatistiky();
    }

    @GetMapping("/statistiky/progres")
    public List<CvikProgresStatistika> getProgresCviku(@RequestParam Long cvikId) {
        return this.treningService.najdiZaznamyPreCvik(cvikId);
    }
}
