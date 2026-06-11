package sk.tomas.fitness_tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "cviky")
public class Cvik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true, unique = true)
    private String nazovCviku;

    private String partia;

    public Cvik() {
    }

    public Cvik(String nazovCviku, String partia) {
        this.nazovCviku = nazovCviku;
        this.partia = partia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNazovCviku() {
        return nazovCviku;
    }

    public void setNazovCviku(String nazovCviku) {
        this.nazovCviku = nazovCviku;
    }

    public String getPartia() {
        return partia;
    }

    public void setPartia(String partia) {
        this.partia = partia;
    }
}
