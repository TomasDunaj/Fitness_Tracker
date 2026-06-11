package sk.tomas.fitness_tracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.model.TreningovyZaznam;
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
    public TreningovyZaznam vytvorNovyZaznam(@RequestBody TreningovyZaznam novyZaznam) {
        return this.treningovyZaznamService.ulozZaznam(novyZaznam);
    }
}
