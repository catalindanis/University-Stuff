package models;

import java.util.Arrays;
import java.util.Objects;

public class Race {
    private int noDucks;
    private Duck[] ducks;
    private int noLanes;
    private Lane[] lanes;
    private int startTime;
    private double endTime;

    public Race(int noDucks, Duck[] ducks, int noLanes, Lane[] lanes) {
        this.noDucks = noDucks;
        this.ducks = ducks;
        this.noLanes = noLanes;
        this.lanes = lanes;
        this.startTime = 0;
        this.endTime = -1;
    }

    public int getNoDucks() {
        return noDucks;
    }

    public void setNoDucks(int noDucks) {
        this.noDucks = noDucks;
    }

    public Duck[] getDucks() {
        return ducks;
    }

    public void setDucks(Duck[] ducks) {
        this.ducks = ducks;
    }

    public int getNoLanes() {
        return noLanes;
    }

    public void setNoLanes(int noLanes) {
        this.noLanes = noLanes;
    }

    public Lane[] getLanes() {
        return lanes;
    }

    public void setLanes(Lane[] lanes) {
        this.lanes = lanes;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setEndTime(double endTime) { this.endTime = endTime; }

    public double getEndTime() {
        return endTime;
    }

    public Duck getSlowestDuck() {
        int position = 0;

        for(int i=1; i<noDucks; i++)
            if(ducks[i].getSpeed() < ducks[position].getSpeed())
                position = i;

        return ducks[position];
    }

    @Override
    public String toString() {
        return "Race{" +
                "ducks=" + Arrays.toString(ducks) +
                ", lanes=" + Arrays.toString(lanes) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return noDucks == race.noDucks && noLanes == race.noLanes && startTime == race.startTime && endTime == race.endTime && Objects.deepEquals(ducks, race.ducks) && Objects.deepEquals(lanes, race.lanes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noDucks, Arrays.hashCode(ducks), noLanes, Arrays.hashCode(lanes), startTime, endTime);
    }
}
