package domain;

public class FlyingDuck extends Duck implements Zburator {

    public FlyingDuck(String username, String email, String password, Double viteza, Double rezistenta, Long cardId) {
        super(username, email, password, viteza, rezistenta, cardId, "flying");
    }

    @Override
    public void zboara() {
        System.out.println(this.getUsername() + " is flying!");
    }
}