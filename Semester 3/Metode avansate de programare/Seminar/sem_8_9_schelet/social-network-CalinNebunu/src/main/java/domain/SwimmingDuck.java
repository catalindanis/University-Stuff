package domain;

public class SwimmingDuck extends Duck implements Inotator {

    public SwimmingDuck(String username, String email, String password, Double viteza, Double rezistenta, Long card_id) {
        super(username, email, password, viteza, rezistenta, card_id, "swimming");
    }

    @Override
    public void inoata() {
        System.out.println(this.getUsername() + " is swimming!");
    }
}
