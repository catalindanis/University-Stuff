package utils;

import models.Duck;
import models.Event;
import models.Lane;
import models.RaceEvent;

import java.util.ArrayList;
import java.util.List;

public class DuckTaskResult {
    public List<Duck> ducks;
    public List<Lane> lanes;
    public List<Double> elapsedTimes;
    public double endTime;
    public RaceEvent event;

    public DuckTaskResult() {
        ducks = new ArrayList<>();
        lanes = new ArrayList<>();
        elapsedTimes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Rezultat, " +
                "rate=" + ducks +
                ", culoare=" + lanes +
                ", timpi=" + elapsedTimes +
                ", timp final=" + endTime;
    }
}
