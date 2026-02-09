package services;

import config.Config;
import exceptions.EventException;
import factories.EventsFactory;
import javafx.application.Platform;
import models.Event;
import models.Lane;
import models.RaceEventDTO;
import org.example.tema.EventsController;
import repositories.DatabaseEventsRepository;
import utils.DuckTask;
import utils.DuckTaskResult;
import utils.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventsService implements Service<Event>, Observable<EventsController> {
    private static EventsService instance;
    private final DatabaseEventsRepository repository;
    private final List<EventsController> observers;
    private final ExecutorService executor;

    private EventsService() {
        String url = Config.getProperties().getProperty("db.url");
        String username = Config.getProperties().getProperty("db.username");
        String password = Config.getProperties().getProperty("db.password");
        repository = new DatabaseEventsRepository(url, username, password);
        observers = new ArrayList<>();
        executor = Executors.newCachedThreadPool();
    }

    public static EventsService getInstance() {
        if (instance == null)
            instance = new EventsService();

        return instance;
    }

    public Event add(String name, List<Lane> lanes) {
        Event added = repository.add(
                name,
                EventsFactory.getInstance().create(generateId(), lanes)
        );

        notifyObservers();
        return added;
    }

    public Event add(RaceEventDTO eventDTO) {
        Event added = repository.add(
                EventsFactory.getInstance().create(
                        generateId(),
                        eventDTO.swimmingGroupId,
                        eventDTO.lanes
                )
        );

        notifyObservers();
        return added;
    }

    public Event remove(long id) {
        Event removed = repository.remove(repository.get(id));

        notifyObservers();
        return removed;
    }

    public void join(long userId, long eventId) {
        try {
            repository.join(eventId, userId);
        }
        catch(NullPointerException e) {
            throw new EventException("Eveniment negasit");
        }
    }

    public void unjoin(long userId, long eventId) {
        try {
            repository.unjoin(eventId, userId);
        }
        catch(NullPointerException e) {
            throw new EventException("Eveniment negasit");
        }
    }

    public void subscribe(long userId, long eventId) {
        try {
            repository.subscribe(eventId, userId);
        }
        catch (NullPointerException e) {
            throw new EventException("Eveniment negasit");
        }
    }

    public void unsubscribe(long userId, long eventId) {
        try {
            repository.unsubscribe(eventId, userId);
        }
        catch (NullPointerException e) {
            throw new EventException("Eveniment negasit");
        }
    }

    public <T> T start(long id) {
         T result = repository.get(id).start();

         if(result instanceof DuckTaskResult) {
            notifyObservers((DuckTaskResult) result);
         }

         return result;
    }

    public <T> CompletableFuture<T> startAsync(long id) {
        return CompletableFuture.supplyAsync(() -> {
            T result = repository.get(id).start();

            if (result instanceof DuckTaskResult) {
                notifyObservers((DuckTaskResult) result);
            }

            return result;
        });
    }

    public List<Event> getEvents() { return repository.getAll(); }

    private synchronized long generateId() { return repository.getAll().stream().map(Event::getId).max(Long::compare).orElse(0L) + 1; }

    @Override
    public synchronized void subscribe(EventsController eventsController) {
        observers.add(eventsController);
    }

    @Override
    public synchronized void unsubscribe(EventsController eventsController) {
        observers.remove(eventsController);
    }

    @Override
    public synchronized void notifyObservers() {
        for(var observer : observers) {
            observer.update(null);
        }
    }

    public synchronized void notifyObservers(DuckTaskResult result) {
        for(var observer : observers) {
            observer.update(result);
        }
    }
}
