package sk.tomas.fitness_tracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "cviky")
public class Cvik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String nazovCviku;

    private String partia;


    @OneToMany(mappedBy = "cvik", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("cvik")
    private List<TreningovyZaznam> zaznamy;

    @JsonCreator
    public Cvik() {
    }

    public Cvik(String nazovCviku, String partia) {
        this.nazovCviku = nazovCviku;
        this.partia = partia;
    }

    public List<TreningovyZaznam> getZaznamy() {
        return zaznamy;
    }

    public void setZaznamy(List<TreningovyZaznam> zaznamy) {
        this.zaznamy = zaznamy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
