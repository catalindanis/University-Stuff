package validators;

import models.Event;

public class EventValidator implements Validator<Event> {
    @Override
    public boolean validate(Event event) {
        return true;
    }
}
