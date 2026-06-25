package ro.mpp2026.festivalmuzicajavafx.utils;

public interface Observable {
    void subscribe(Observer observer);
    void notifyObservers();
}
