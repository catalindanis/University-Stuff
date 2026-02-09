package repositories;

import exceptions.EventException;
import models.Event;

public class InMemoryEventsRepository extends EventsRepository {
    @Override
    public Event add(Event event) {
        events.add(event);
        return event;
    }

    @Override
    public Event remove(Event event) {
        if(!events.remove(event))
            throw new EventException("Event not found");

        return event;
    }
}
