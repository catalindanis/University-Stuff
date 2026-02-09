package repositories;

import models.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class EventsRepository implements Repository<Long, Event> {
    protected List<Event> events;

    public EventsRepository() { this.events = new ArrayList<>(); }

    @Override
    public Event get(Long id) { return events.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null); }

    @Override
    public List<Event> getAll() { return events; }
}
