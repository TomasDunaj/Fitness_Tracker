package sk.tomas.fitness_tracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "treningy")
public class Trening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datum;

    @OneToMany(mappedBy = "trening", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("trening")
    private List<TreningovyZaznam> zaznamy;


    public Trening() {
    }

    public Trening(LocalDate datum) {
        this.datum = datum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public List<TreningovyZaznam> getZaznamy() {
        return zaznamy;
    }

    public void setZaznamy(List<TreningovyZaznam> zaznamy) {
        this.zaznamy = zaznamy;
    }
}
