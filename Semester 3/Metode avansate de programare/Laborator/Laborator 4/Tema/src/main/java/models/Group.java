package models;

import java.util.ArrayList;
import java.util.List;

public abstract class Group extends Entity<Long> {
    protected String name;
    protected DuckType ducksType;

    public Group(Long id, String name, List<Duck> ducks) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DuckType getDucksType() { return ducksType; }

    public double getAveragePerformance() {
        double totalSpeed = 0, totalResistance = 0;

        //TODO: redo
//        for(var duck : ducks) {
//            totalSpeed += duck.getSpeed();
//            totalResistance += duck.getResistance();
//        }

        return totalSpeed / totalResistance;
    }

    @Override
    public String toString() {
        return "Card" + ", " +
                super.toString() + ", " +
                "nume='" + name + '\'' +
                ", performanta medie=" + getAveragePerformance();
    }
}

