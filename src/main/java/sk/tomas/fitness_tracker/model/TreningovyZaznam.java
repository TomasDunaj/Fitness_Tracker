package sk.tomas.fitness_tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "treningove_Zaznamy")
public class TreningovyZaznam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nazovCviku;
    private double vaha;
    private int pocetSerii;
    private int pocetOpakovani;

    private LocalDate datum;

    public TreningovyZaznam() {
    }

    public TreningovyZaznam(String nazovCviku, double vaha, int pocetSerii, int pocetOpakovani) {
        this.nazovCviku = nazovCviku;
        this.vaha = vaha;
        this.pocetSerii = pocetSerii;
        this.pocetOpakovani = pocetOpakovani;
    }

    public String getNazovCviku() {
        return nazovCviku;
    }

    public void setNazovCviku(String nazovCviku) {
        this.nazovCviku = nazovCviku;
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
}