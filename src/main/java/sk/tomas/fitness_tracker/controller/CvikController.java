package sk.tomas.fitness_tracker.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.model.cvik.Cvik;
import sk.tomas.fitness_tracker.service.CvikService;

import java.util.List;

@RestController
@RequestMapping("/api/cviky")
public class CvikController {

    private final CvikService cvikService;

    public CvikController(CvikService cvikService) {
        this.cvikService = cvikService;
    }

    @GetMapping
    public List<Cvik> getVsetkyCviky() {
        return this.cvikService.getVsetkyCviky();
    }

    @PostMapping
    public Cvik ulozCvik(@RequestBody Cvik cvik) {
        return this.cvikService.ulozCvik(cvik);
    }

    @PutMapping("/{id}")
    public Cvik aktualizujCvik(@PathVariable Long id, @RequestBody Cvik aktualizovanyCvik) {
        return this.cvikService.aktualizujCvik(id, aktualizovanyCvik);
    }

    @DeleteMapping("/{id}")
    public void vymazCvik(@PathVariable Long id) {
        this.cvikService.vymazCvik(id);
    }
}
