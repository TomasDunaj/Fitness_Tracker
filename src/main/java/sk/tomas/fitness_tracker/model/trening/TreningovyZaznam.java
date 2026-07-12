package sk.tomas.fitness_tracker.model.trening;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import sk.tomas.fitness_tracker.model.cvik.Cvik;

import java.util.List;

@Entity
@Table(name = "treningove_Zaznamy")
public class TreningovyZaznam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cvik_id")
    @JsonIgnoreProperties("zaznamy")
    private Cvik cvik;

    @ManyToOne
    @JoinColumn(name = "trening_id")
    @JsonBackReference
    private Trening trening;

    public TreningovyZaznam() {
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "treningovyZaznam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seria> serie;

    private Boolean splnene;


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

    public Boolean isSplnene() {
        return this.splnene;
    }

    public void setSplnene(boolean splnene) {
        this.splnene = splnene;
    }
}