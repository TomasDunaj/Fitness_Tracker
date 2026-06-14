package sk.tomas.fitness_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "treningove_Zaznamy")
public class TreningovyZaznam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cvik_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("zaznamy")
    private Cvik cvik;

    @ManyToOne
    @JoinColumn(name = "trening_id")
    private Trening trening;

    public TreningovyZaznam() {
    }

    @OneToMany(mappedBy = "treningovyZaznam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seria> serie;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cvik getCvik() {
        return cvik;
    }

    public void setCvik(Cvik cvik) {
        this.cvik = cvik;
    }

    public List<Seria> getSerie() {
        return serie;
    }

    public void setSerie(List<Seria> serie) {
        this.serie = serie;
    }

    public Trening getTrening() {
        return trening;
    }

    public void setTrening(Trening trening) {
        this.trening = trening;
    }
}