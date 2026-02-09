package models;

import java.util.List;

public class Race {
    private List<Duck> ducks;
    private List<Lane> lanes;
    private final int startTime;
    private double endTime;

    public Race(List<Duck> ducks, List<Lane> lanes) {
        this.ducks = ducks;
        this.lanes = lanes;
        this.startTime = 0;
        this.endTime = -1;
    }

    public int getNoDucks() { return ducks.size(); }

    public int getNoLanes() { return lanes.size(); }

    public List<Duck> getDucks() {
        return ducks;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public int getStartTime() { return startTime; }

    public void setEndTime(double endTime) { this.endTime = endTime; }

    public double getEndTime() {
        return endTime;
    }

    public Duck getSlowestDuck() {
        int position = 0;

        if(ducks.size() == 0)
            return null;

        for(int i=1; i<ducks.size(); i++)
            if(ducks.get(i).getSpeed() < ducks.get(position).getSpeed())
                position = i;

        return ducks.get(position);
    }

    @Override
    public String toString() {
        return "Cursa, " +
                "rate=" + ducks +
                ", culoare=" + lanes;
    }
}

