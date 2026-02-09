package models;

import java.util.List;

public class FlyingGroup extends Group {
    public FlyingGroup(Long id, String name, List<Duck> ducks) {
        super(id, name, ducks);
        this.ducksType = DuckType.FLYING;
    }
}
