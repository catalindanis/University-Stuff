package models;

import lombok.Getter;
import utils.Observable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Event extends Entity<Long> implements Observable<User> {
    List<User> subscribers;

    public Event(Long id) {
        super(id);
        subscribers = new ArrayList<>();
    }

    public synchronized <T> T start() { notifyObservers(); return null; }

    @Override
    public synchronized void subscribe(User user) {
        this.subscribers.add(user);
    }

    @Override
    public synchronized void unsubscribe(User user) {
        this.subscribers.remove(user);
    }

    @Override
    public synchronized void notifyObservers() {
        for(var subscriber: subscribers) {
            subscriber.update(this);
        }
    }

    @Override
    public String toString() {
        return "Eveniment, " +
                super.toString() + ", " +
                "abonati=" + subscribers;
    }
}
