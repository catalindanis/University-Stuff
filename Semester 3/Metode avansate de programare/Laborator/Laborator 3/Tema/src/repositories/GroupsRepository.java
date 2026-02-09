package repositories;

import models.Group;

import java.util.ArrayList;
import java.util.List;

public abstract class GroupsRepository implements Repository<Long, Group> {
    protected List<Group> groups;

    public GroupsRepository() { this.groups = new ArrayList<>(); }

    @Override
    public abstract Group add(Group group);

    @Override
    public abstract Group remove(Group group);

    @Override
    public Group get(Long id) {
        return groups.stream().filter(g -> g.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Group> getAll() {
        return groups;
    }
}
