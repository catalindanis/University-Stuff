package validators;

import models.Race;

public class RaceValidator implements Validator<Race> {
    @Override
    public boolean validate(Race race) {
        if(race.getDucks() == null || race.getNoDucks() == 0)
            return false;

        if(race.getLanes() == null || race.getNoLanes() == 0)
            return false;

        if(race.getNoDucks() < race.getNoLanes())
            return false;

        return true;
    }
}
