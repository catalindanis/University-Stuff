package models;

public class Lane {
    private int buoyDistance;

    public Lane(int buoyDistance) {
        this.buoyDistance = buoyDistance;
    }

    public int getBuoyDistance() {
        return buoyDistance;
    }

    public void setBuoyDistance(int buoyDistance) {
        this.buoyDistance = buoyDistance;
    }

    @Override
    public String toString() {
        return "Culoar, " +
                "distantaBaliza=" + buoyDistance;
    }
}
