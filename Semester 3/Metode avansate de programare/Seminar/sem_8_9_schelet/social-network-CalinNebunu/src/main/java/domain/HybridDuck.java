package domain;

public class HybridDuck extends Duck implements Inotator, Zburator {

    public HybridDuck(String username, String email, String password, Double viteza, Double rezistenta, Long card_id) {
        super(username, email, password, viteza, rezistenta, card_id, "hybrid");
    }

    @Override
    public void inoata() {
        System.out.println(this.getUsername() + " is swimming!");
    }

    @Override
    public void zboara() {
        System.out.println(this.getUsername() + " is flying!");
    }
}