package models;

import java.util.List;

public class FlyingSwimmingGroup extends Group {
    public FlyingSwimmingGroup(Long id, String name, List<Duck> ducks) {
        super(id, name, ducks);
        this.ducksType = DuckType.FLYING_AND_SWIMMING;
    }
}
