package sk.tomas.fitness_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "seria")
public class Seria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1, message = "Váha nemôže byť záporná!")
    private double vaha;

    @NotNull
    @Min(value = 1, message = "Musíš zadať aspoň 1 opakovanie!")
    private int pocetOpakovani;

    @ManyToOne
    @JoinColumn(name = "treningovy_zaznam_id")
    @JsonIgnore
    private TreningovyZaznam treningovyZaznam;

    public Seria() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getVaha() {
        return vaha;
    }

    public void setVaha(double vaha) {
        this.vaha = vaha;
    }

    public int getPocetOpakovani() {
        return pocetOpakovani;
    }

    public void setPocetOpakovani(int pocetOpakovani) {
        this.pocetOpakovani = pocetOpakovani;
    }

    public TreningovyZaznam getTreningovyZaznam() {
        return treningovyZaznam;
    }

    public void setTreningovyZaznam(TreningovyZaznam treningovyZaznam) {
        this.treningovyZaznam = treningovyZaznam;
    }
}
