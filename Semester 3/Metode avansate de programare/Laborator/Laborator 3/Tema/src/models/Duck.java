package models;

import java.io.Serial;
import java.io.Serializable;

public abstract class Duck extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    DuckType type;
    double speed;
    double resistance;
    Group group;

    public Duck(long id, String username, String email, String password, DuckType type, double speed, double resistance, Group group) {
        super(id, username, email, password);
        this.type = type;
        this.speed = speed;
        this.resistance = resistance;
        this.group = group;
    }

    public DuckType getType() {
        return type;
    }

    public void setType(DuckType type) {
        this.type = type;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Rata" + ", " +
                super.toString() + ", " +
                "tip=" + type +
                ", viteza=" + speed +
                ", rezistenta=" + resistance;
//                ", group=" + group;
    }
}

interface Flyer {
    void fly();
}

interface Swimmer {
    void swim();
}