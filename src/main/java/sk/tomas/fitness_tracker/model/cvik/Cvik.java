package sk.tomas.fitness_tracker.model.cvik;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import sk.tomas.fitness_tracker.model.enums.SvalovaPartia;
import sk.tomas.fitness_tracker.model.trening.TreningovyZaznam;

import java.util.List;

@Entity
@Table(name = "cviky")
public class Cvik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Názov cviku nesmie byť prázdny!")
    @Column(nullable = false, unique = true)
    private String nazovCviku;

    @Enumerated
    @Column(name = "svalova_partia", nullable = false)
    private SvalovaPartia svalovaPartia;


    @OneToMany(mappedBy = "cvik", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("cvik")
    private List<TreningovyZaznam> zaznamy;

    @JsonCreator
    public Cvik() {
    }

    public Cvik(String nazovCviku, SvalovaPartia partia) {
        this.nazovCviku = nazovCviku;
        this.svalovaPartia = partia;
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

    public SvalovaPartia getSvalovaPartia() {
        return svalovaPartia;
    }

    public void setSvalovaPartia(SvalovaPartia svalovaPartia) {
        this.svalovaPartia = svalovaPartia;
    }
}
