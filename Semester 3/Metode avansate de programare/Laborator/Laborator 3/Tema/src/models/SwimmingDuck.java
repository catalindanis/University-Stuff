package models;

import java.io.Serial;
import java.io.Serializable;

public class SwimmingDuck extends Duck implements Swimmer, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public SwimmingDuck(long id, String username, String email, String password, DuckType type, double speed, double resistance, Group group) {
        super(id, username, email, password, type, speed, resistance, group);
    }

    @Override
    public void swim() {

    }
}
