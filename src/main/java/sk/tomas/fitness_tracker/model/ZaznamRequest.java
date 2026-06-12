package sk.tomas.fitness_tracker.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ZaznamRequest {

    @NotNull(message = "Váha musí byť zadaná!")
    @Min(value = 0, message = "Váha nemôže byť záporná!")
    private double vaha;

    @NotNull(message = "Počet sérii musí byť zadaný!")
    @Min(value = 1, message = "Musíš odcvičiť aspoň 1 sériu.")
    private int pocetSerii;

    @NotNull(message = "Počet opakovaní musí byť zadaný!")
    @Min(value = 1, message = "Počet opakovaní musí byť aspoň 1.")
    private int pocetOpakovani;

    @NotNull(message = "ID cviku musí byť zadané!")
    private Long cvikId;

    public ZaznamRequest() {
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

    public Long getCvikId() {
        return cvikId;
    }

    public void setCvikId(Long cvikId) {
        this.cvikId = cvikId;
    }
}