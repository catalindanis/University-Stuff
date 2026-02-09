package Observer;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
