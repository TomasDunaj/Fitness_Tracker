package sk.tomas.fitness_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tomas.fitness_tracker.model.Cvik;
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
}
