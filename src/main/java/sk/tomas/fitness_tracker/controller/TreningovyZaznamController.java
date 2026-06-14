package sk.tomas.fitness_tracker.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.model.TreningovyZaznam;
import sk.tomas.fitness_tracker.model.ZaznamRequest;
import sk.tomas.fitness_tracker.service.TreningovyZaznamService;

import java.util.List;

@RestController
@RequestMapping("/api/zaznamy")
public class TreningovyZaznamController {

    private final TreningovyZaznamService treningovyZaznamService;

    public TreningovyZaznamController(TreningovyZaznamService treningovyZaznamService) {
        this.treningovyZaznamService = treningovyZaznamService;
    }

    @GetMapping
    public List<TreningovyZaznam> getVsetkyZaznamy() {
        return this.treningovyZaznamService.getVsetkyZaznamy();
    }

    @PostMapping
    public TreningovyZaznam vytvorNovyZaznam(@Valid @RequestBody ZaznamRequest zaznamRequest) {
        return this.treningovyZaznamService.ulozZaznam(zaznamRequest);
    }

    @PutMapping("/{id}")
    public TreningovyZaznam aktualizujZaznam(@PathVariable Long id, @Valid @RequestBody ZaznamRequest novyZaznam) {
        return this.treningovyZaznamService.aktualizujZaznam(id, novyZaznam);
    }

    @DeleteMapping("/{id}")
    public void vymazZaznam(@PathVariable Long id) {
        this.treningovyZaznamService.vymazZaznam(id);
    }

    @GetMapping("/cvik/{cvikId}")
    public List<TreningovyZaznam> getZaznamyCviku(@PathVariable Long cvikId) {
        return this.treningovyZaznamService.getZaznamyPreCvik(cvikId);
    }
}
