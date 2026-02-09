package models;

import java.util.List;

public class SwimmingGroup extends Group {

    public SwimmingGroup(Long id, String name, List<Duck> ducks) {
        super(id, name, ducks);
        this.ducksType = DuckType.SWIMMING;
    }
}
