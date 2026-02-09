package utils;

public interface Observable<T extends Observer> {
    void subscribe(T t);
    void unsubscribe(T t);
    void notifyObservers();
}
