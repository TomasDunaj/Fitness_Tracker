package sk.tomas.fitness_tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "treningove_Zaznamy")
public class TreningovyZaznam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double vaha;
    private int pocetSerii;
    private int pocetOpakovani;

    private LocalDate datum;

    @ManyToOne
    @JoinColumn(name = "cvik_id", nullable = false)
    private Cvik cvik;

    public TreningovyZaznam() {
    }

    public TreningovyZaznam(double vaha, int pocetSerii, int pocetOpakovani) {
        this.vaha = vaha;
        this.pocetSerii = pocetSerii;
        this.pocetOpakovani = pocetOpakovani;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getVaha() {
        return vaha;
    }

    public void setVaha(double vaha) {
        this.vaha = vaha;
    }

    public int getPocetSerii() {
        return pocetSerii;
    }

    public void setPocetSerii(int pocetSerii) {
        this.pocetSerii = pocetSerii;
    }

    public int getPocetOpakovani() {
        return pocetOpakovani;
    }

    public void setPocetOpakovani(int pocetOpakovani) {
        this.pocetOpakovani = pocetOpakovani;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Cvik getCvik() {
        return cvik;
    }

    public void setCvik(Cvik cvik) {
        this.cvik = cvik;
    }
}