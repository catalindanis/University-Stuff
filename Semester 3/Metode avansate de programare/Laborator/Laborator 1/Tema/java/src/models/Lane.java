package models;

import java.util.Objects;

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
        return "Lane{" +
                "buoyDistance=" + buoyDistance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lane lane = (Lane) o;
        return buoyDistance == lane.buoyDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(buoyDistance);
    }
}
