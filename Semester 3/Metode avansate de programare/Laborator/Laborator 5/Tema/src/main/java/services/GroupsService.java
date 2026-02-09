package services;

import factories.GroupsFactory;
import lombok.Getter;
import models.Duck;
import models.Group;
import models.GroupDTO;
import repositories.DatabaseGroupsRepository;
import repositories.Repository;

import java.util.List;

public class GroupsService implements Service<Group> {
    @Getter
    private static final GroupsService instance = new GroupsService();
    private final Repository<Long, Group> repository;

    private GroupsService() { repository = new DatabaseGroupsRepository("jdbc:postgresql://localhost:5432/DuckSocialNetwork", "laborator", "1234"); }

    public Group add(GroupDTO groupDTO) {
        Group group = repository.add(
                GroupsFactory.getInstance().create(
                        -1,
                        groupDTO.name,
                        groupDTO.ducksType,
                        groupDTO.ducksIds
                )
        );

        for(Duck duck : group.getDucks()) {
            duck.setGroup(group.getId());
            UsersService.getInstance().update(duck.getId(), duck);
        }

        return group;
    }

    public Group removeById(long id) { return repository.remove(repository.get(id)); }

    public Group getById(long id) {
        return repository.get(id);
    }

    public List<Group> getGroups() {
        return repository.getAll();
    }

    private long generateId() {
        return repository.getAll().stream().map(Group::getId).max(Long::compare).orElse(0L) + 1;
    }
}
