package validators;

import models.Event;
import models.RaceEvent;

public class RaceEventValidator extends EventValidator {
    RaceValidator raceValidator = new RaceValidator();

    @Override
    public boolean validate(Event event) {
        if(!super.validate(event))
            return false;

        RaceEvent raceEvent = (RaceEvent) event;

        if(!raceValidator.validate(raceEvent.getRace()))
            return false;

        return true;
    }
}
