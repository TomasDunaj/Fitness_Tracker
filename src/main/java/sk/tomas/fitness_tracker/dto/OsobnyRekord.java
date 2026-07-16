package sk.tomas.fitness_tracker.dto;

public class OsobnyRekord {
    private String nazovCviku;
    private double vaha;

    public OsobnyRekord(String nazovCviku, double vaha) {
        this.nazovCviku = nazovCviku;
        this.vaha = vaha;
    }

    public String getNazovCviku() {
        return nazovCviku;
    }

    public void setNazovCviku(String nazovCviku) {
        this.nazovCviku = nazovCviku;
    }

    public double getVaha() {
        return vaha;
    }

    public void setVaha(double vaha) {
        this.vaha = vaha;
    }
}
