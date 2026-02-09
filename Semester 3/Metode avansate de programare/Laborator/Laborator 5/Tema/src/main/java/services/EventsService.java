package services;

import exceptions.EventException;
import factories.EventsFactory;
import models.Event;
import models.RaceEventDTO;
import repositories.DatabaseEventsRepository;
import repositories.Repository;

import java.util.List;

public class EventsService implements Service<Event> {
    private static EventsService instance;
    private final DatabaseEventsRepository repository;

    private EventsService() { repository = new DatabaseEventsRepository("jdbc:postgresql://localhost:5432/DuckSocialNetwork", "laborator", "1234"); }

    public static EventsService getInstance() {
        if (instance == null)
            instance = new EventsService();

        return instance;
    }

    public Event add(RaceEventDTO eventDTO) {
        return repository.add(
                EventsFactory.getInstance().create(
                   generateId(),
                   eventDTO.swimmingGroupId,
                   eventDTO.lanes
                )
        );
    }

    public void subscribe(long userId, long eventId) {
        try {
            repository.subscribe(eventId, userId);
        }
        catch (NullPointerException e) {
            throw new EventException("Eveniment negasit");
        }
    }

    public <T> T start(long id) { return repository.get(id).start(); }

    public List<Event> getEvents() { return repository.getAll(); }

    private long generateId() { return repository.getAll().stream().map(Event::getId).max(Long::compare).orElse(0L) + 1; }
}
