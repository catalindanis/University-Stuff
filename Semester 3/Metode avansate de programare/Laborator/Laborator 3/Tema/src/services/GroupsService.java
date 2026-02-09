package services;

import factories.GroupsFactory;
import models.Group;
import models.GroupDTO;
import repositories.FileGroupsRepository;
import repositories.Repository;

import java.util.List;

public class GroupsService implements Service<Group> {
    private static final GroupsService instance = new GroupsService();
    private final Repository<Long, Group> repository;

    private GroupsService() { repository = new FileGroupsRepository("groups.csv"); }

    public static GroupsService getInstance() { return instance; }

    public Group add(GroupDTO groupDTO) {
        return repository.add(
                GroupsFactory.getInstance().create(
                        generateId(),
                        groupDTO.name,
                        groupDTO.ducksType,
                        groupDTO.ducksIds
                )
        );
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
