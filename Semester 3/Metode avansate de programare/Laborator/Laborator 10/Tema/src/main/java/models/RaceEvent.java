package models;

import lombok.Getter;
import services.GroupsService;
import utils.DuckTask;
import utils.DuckTaskResult;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event {
    Race race;
    DuckTaskResult raceResult;
    @Getter
    long groupId;

    public RaceEvent(Long id, SwimmingGroup participants, List<Lane> lanes) {
        super(id);
        race = new Race(participants.ducks, lanes);
        raceResult = null;
    }

    public RaceEvent(Long id, long groupId, List<Lane> lanes) {
        super(id);
        this.groupId = groupId;
        race = new Race(new ArrayList<>(), lanes);
        raceResult = null;
    }

    public RaceEvent(Long id, long groupId, List<Duck> ducks, List<Lane> lanes) {
        super(id);
        this.groupId = groupId;
        race = new Race(ducks, lanes);
        raceResult = null;
    }

    public RaceEvent(Long id, List<Lane> lanes) {
        super(id);
        race = new Race(new ArrayList<>(), lanes);
        raceResult = null;
    }

    public Race getRace() { return race; }

    @Override
    public synchronized DuckTaskResult start() { raceResult = new DuckTask(this).execute(); notifyObservers(); return raceResult; }

    @Override
    public String toString() {
        return GroupsService.getInstance().getById(groupId).getName();
    }
}
