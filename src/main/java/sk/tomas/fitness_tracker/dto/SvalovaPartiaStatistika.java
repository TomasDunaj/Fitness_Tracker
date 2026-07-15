package sk.tomas.fitness_tracker.dto;

public class SvalovaPartiaStatistika {
    private String name;
    private long value;
    private String fill;

    public SvalovaPartiaStatistika(String name, long value, String fill) {
        this.name = name;
        this.value = value;
        this.fill = fill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }
}
