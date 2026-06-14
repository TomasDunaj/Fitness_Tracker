package sk.tomas.fitness_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.model.trening.Trening;
import sk.tomas.fitness_tracker.service.TreningService;

import java.util.List;

@RestController
@RequestMapping("/api/treningy")
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
}
