package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RaceEvent extends Event {

    private List<Double> distantaBalize;

    // Constructor
    public RaceEvent(String descriere, List<Double> distantaBalize) {
        // Constructor din Event
        super(descriere, "race");

        // Sortam balizele pentru a ne asigura ca sunt in ordine crescatoare
        Collections.sort(distantaBalize);
        this.distantaBalize = distantaBalize;
    }

    // --- Getters/Setters ---

    public List<Double> getDistantaBalize() {
        return distantaBalize;
    }

    public void setDistantaBalize(List<Double> distantaBalize) {
        this.distantaBalize = distantaBalize;
    }

    @Override
    public String toString() {
        return String.format("%s, Buoys=%s",
                super.toString(),
                distantaBalize
        );
    }

}