package sk.tomas.fitness_tracker.model;

public class ZaznamRequest {
    private double vaha;
    private int pocetSerii;
    private int pocetOpakovani;
    private Long cvikId;

    public ZaznamRequest() {}

    public double getVaha() { return vaha; }
    public void setVaha(double vaha) { this.vaha = vaha; }
    public int getPocetSerii() { return pocetSerii; }
    public void setPocetSerii(int pocetSerii) { this.pocetSerii = pocetSerii; }
    public int getPocetOpakovani() { return pocetOpakovani; }
    public void setPocetOpakovani(int pocetOpakovani) { this.pocetOpakovani = pocetOpakovani; }
    public Long getCvikId() { return cvikId; }
    public void setCvikId(Long cvikId) { this.cvikId = cvikId; }
}