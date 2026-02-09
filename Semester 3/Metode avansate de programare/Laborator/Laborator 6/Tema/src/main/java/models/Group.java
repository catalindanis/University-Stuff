package models;

import java.util.ArrayList;
import java.util.List;

public abstract class Group extends Entity<Long> {
    protected String name;
    protected DuckType ducksType;
    protected List<Duck> ducks;

    public Group(Long id, String name, List<Duck> ducks) {
        super(id);
        this.name = name;
        this.ducks = ducks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Duck> getDucks() {
        return new ArrayList<>(ducks);
    }

    public void setDucks(List<Duck> ducks) {
        this.ducks = new ArrayList<>(ducks);
    }

    public DuckType getDucksType() { return ducksType; }

    public double getAveragePerformance() {
        double totalSpeed = 0, totalResistance = 0;

        for(var duck : ducks) {
            totalSpeed += duck.getSpeed();
            totalResistance += duck.getResistance();
        }

        return totalSpeed / totalResistance;
    }

    @Override
    public String toString() {
        return "Card" + ", " +
                super.toString() + ", " +
                "nume='" + name + '\'' +
                ", rate=" + ducks +
                ", performanta medie=" + getAveragePerformance();
    }
}

