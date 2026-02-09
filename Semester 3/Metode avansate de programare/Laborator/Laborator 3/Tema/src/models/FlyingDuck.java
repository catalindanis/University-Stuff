package models;

import java.io.Serial;
import java.io.Serializable;

public class FlyingDuck extends Duck implements Flyer, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public FlyingDuck(long id, String username, String email, String password, DuckType type, double speed, double resistance, Group group) {
        super(id, username, email, password, type, speed, resistance, group);
    }

    @Override
    public void fly() {

    }
}
