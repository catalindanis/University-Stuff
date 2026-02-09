package models;

import utils.DuckTask;
import utils.DuckTaskResult;

import java.util.List;

public class RaceEvent extends Event {
    Race race;
    DuckTaskResult raceResult;

    public RaceEvent(Long id, SwimmingGroup participants, List<Lane> lanes) {
        super(id);
        race = new Race(participants.ducks, lanes);
        raceResult = null;
    }

    public Race getRace() { return race; }

    @Override
    public DuckTaskResult start() { raceResult = new DuckTask(race).execute(); notifyObservers(); return raceResult; }

    @Override
    public String toString() {
        return "Eveniment cursa, " +
                super.toString() + ", " +
                "cursa=" + race +
                (raceResult != null ? ", rezultat=" + raceResult : "");
    }
}
