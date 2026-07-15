package sk.tomas.fitness_tracker.dto;

import java.time.LocalDate;

public class CvikProgresStatistika {
    private LocalDate datum;
    private double vaha;

    public CvikProgresStatistika(LocalDate datum, double vaha) {
        this.datum = datum;
        this.vaha = vaha;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public double getVaha() {
        return vaha;
    }

    public void setVaha(double vaha) {
        this.vaha = vaha;
    }
}

