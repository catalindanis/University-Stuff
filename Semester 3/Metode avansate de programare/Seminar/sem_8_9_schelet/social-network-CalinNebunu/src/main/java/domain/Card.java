package domain;

public class Card extends Entity<Long> {

    private String numeCard;
    private String tipMembri;

    // Constructor
    public Card(String numeCard, String tipMembri) {
        this.numeCard = numeCard;
        this.tipMembri = tipMembri;
    }

    // --- Getters/Setters ---

    public String getNumeCard() {
        return numeCard;
    }

    public void setNumeCard(String numeCard) {
        this.numeCard = numeCard;
    }

    public String getTipMembri() {
        return tipMembri;
    }

    public void setTipMembri(String tipMembri) {
        this.tipMembri = tipMembri;
    }

    @Override
    public String toString() {
        return String.format("Card {ID=%d, Name='%s', Type='%s'}",
                id,
                numeCard,
                tipMembri);
    }

}