package services;

import config.Config;
import dto.DuckDTO;
import dto.PersonDTO;
import dto.UsersFilterDTO;
import factories.UsersFactory;
import lombok.Getter;
import models.Duck;
import models.DuckType;
import models.User;
import repositories.DatabaseUsersRepository;
import repositories.PageableRepository;
import utils.paging.Page;
import utils.paging.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class UsersService implements Service<User> {
    @Getter
    private static final UsersService instance = new UsersService();
    private final PageableRepository<Long, User, UsersFilterDTO> repository;

    private UsersService() {
        String url = Config.getProperties().getProperty("db.url");
        String username = Config.getProperties().getProperty("db.username");
        String password = Config.getProperties().getProperty("db.password");
        repository = new DatabaseUsersRepository(url, username, password);
    }

    public User add(DuckDTO duckDTO) {
        return repository.add(
                UsersFactory.getInstance().createDuck(
                        -1,
                        duckDTO.username,
                        duckDTO.email,
                        duckDTO.password,
                        duckDTO.type,
                        duckDTO.speed,
                        duckDTO.resistance,
                        duckDTO.group
                )
        );
    }

    public User update(long id, Duck duck) {
        return repository.update(id, duck);
    }

    public User add(PersonDTO personDTO) {
        return repository.add(
                UsersFactory.getInstance().createPerson(
                        -1,
                        personDTO.username,
                        personDTO.email,
                        personDTO.password,
                        personDTO.firstName,
                        personDTO.lastName,
                        personDTO.dateOfBirth,
                        personDTO.occupation,
                        personDTO.empathyLevel
                )
        );
    }

    public User removeById(long id) {
        User deletedUser = repository.remove(repository.get(id));

        if(deletedUser != null)
            FriendshipsService.getInstance().removeFriendships(deletedUser.getId());

        return deletedUser;
    }

    public User getById(long id) {
        return repository.get(id);
    }

    public List<User> getUsers() {
        return repository.getAll();
    }

    public Page<User> getUsers(Pageable page, UsersFilterDTO usersFilterDTO) {
        return repository.getAllOnPage(page, usersFilterDTO);
    }

    public List<Duck> getDucks() {
        return repository.getAll().stream()
                .filter(user -> user instanceof Duck)
                .map(user -> (Duck) user)
                .collect(Collectors.toList());
    }

    public List<Duck> getDucks(DuckType duckType) {
        if(duckType == null)
            return getDucks();

        return repository.getAll().stream()
                .filter(user -> user instanceof Duck)
                .map(user -> (Duck) user)
                .filter(duck -> duck.getType().equals(duckType))
                .collect(Collectors.toList());
    }
}
