package sk.tomas.fitness_tracker.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.dto.OsobnyRekord;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;
import sk.tomas.fitness_tracker.service.TreningovyZaznamService;

import java.util.List;

@RestController
@RequestMapping("/api/zaznamy")
@CrossOrigin(origins = "*")
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
    public TreningovyZaznam vytvorNovyZaznam(@Valid @RequestBody TreningovyZaznam zaznam) {
        return this.treningovyZaznamService.ulozZaznam(zaznam);
    }

    @PutMapping("/{id}")
    public TreningovyZaznam aktualizujStav(@PathVariable Long id) {
        return this.treningovyZaznamService.aktualizujStav(id);

    }

    @DeleteMapping("/{id}")
    public void vymazZaznam(@PathVariable Long id) {
        this.treningovyZaznamService.vymazZaznam(id);
    }

    @GetMapping("/cvik/{cvikId}")
    public List<TreningovyZaznam> getZaznamyCviku(@PathVariable Long cvikId) {
        return this.treningovyZaznamService.getZaznamyPreCvik(cvikId);
    }

    @GetMapping("/osobne-rekordy")
    public List<OsobnyRekord> getOsobneRekordy() {
        return this.treningovyZaznamService.dajOsobneRekordy();
    }
}
