package domain;

public abstract class Event extends Entity<Long> {

    protected String descriere;
    protected String tip;

    public Event(String descriere, String tip) {
        this.descriere = descriere;
        this.tip = tip;

    }

    // --- Getters/Setters ---

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return String.format("Event {ID=%d, Description=%s, Type=%s}",
                id,
                descriere,
                tip);
    }

}