package models;

public class Lane {
    private int number;
    private int buoyDistance;

    public Lane(int number, int buoyDistance) {
        this.number = number;
        this.buoyDistance = buoyDistance;
    }

    public Lane(int buoyDistance) {
        this.buoyDistance = buoyDistance;
        this.number = -1; // placeholder; database will set the real number
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBuoyDistance() {
        return buoyDistance;
    }

    public void setBuoyDistance(int buoyDistance) {
        this.buoyDistance = buoyDistance;
    }

    @Override
    public String toString() {
        return "Culoar " + number + ", distanta baliza=" + buoyDistance;
    }
}
