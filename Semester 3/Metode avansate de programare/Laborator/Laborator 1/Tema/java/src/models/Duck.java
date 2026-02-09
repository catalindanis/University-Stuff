package models;

import java.util.Objects;

public class Duck {
    private int speed;
    private int stamina;

    public Duck(int speed, int stamina) {
        this.speed = speed;
        this.stamina = stamina;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    @Override
    public String toString() {
        return "Duck{" +
                "speed=" + speed +
                ", stamina=" + stamina +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duck duck = (Duck) o;
        return speed == duck.speed && stamina == duck.stamina;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, stamina);
    }
}
