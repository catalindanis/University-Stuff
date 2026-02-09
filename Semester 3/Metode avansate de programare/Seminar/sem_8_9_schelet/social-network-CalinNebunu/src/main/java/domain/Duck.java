package domain;

public abstract class Duck extends User {

    private Double viteza;
    private Double rezistenta;
    private Long cardId;
    private String duckType;

    // Constructor
    public Duck(String username, String email, String password, Double viteza, Double rezistenta, Long cardId, String duckType) {
        // Constructor din User
        super(username, email, password);

        this.viteza = viteza;
        this.rezistenta = rezistenta;
        this.cardId = cardId;
        this.duckType = duckType;
    }

    // --- Getters/Setters ---

    public Double getViteza() {
        return viteza;
    }

    public void setViteza(Double viteza) {
        this.viteza = viteza;
    }

    public Double getRezistenta() {
        return rezistenta;
    }

    public void setRezistenta(Double rezistenta) {
        this.rezistenta = rezistenta;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getDuckType() {
        return duckType;
    }

    public void setDuckType(String duckType) {
        this.duckType = duckType;
    }

    @Override
    public String toString() {
        return String.format("%s Duck {Speed=%.2f, Stamina=%.2f, Card(ID)=%s, Type='%s'}",
                super.toString(),
                viteza,
                rezistenta,
                cardId != null ? cardId : "none",
                duckType);
    }

}
