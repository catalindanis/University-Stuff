package services;

import factories.UsersFactory;
import models.Duck;
import models.DuckDTO;
import models.PersonDTO;
import models.User;
import repositories.FileUsersRepository;
import repositories.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class UsersService implements Service<User> {
    private static final UsersService instance = new UsersService();
    private final Repository<Long, User> repository;

    private UsersService() {
        repository = new FileUsersRepository("users.csv");
    }

    public static UsersService getInstance() {
        return instance;
    }

    public User add(DuckDTO duckDTO) {
        return repository.add(
                UsersFactory.getInstance().createDuck(
                        generateId(),
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

    public User add(PersonDTO personDTO) {
        return repository.add(
                UsersFactory.getInstance().createPerson(
                        generateId(),
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

    public List<Duck> getDucks() {
        return repository.getAll().stream()
                .filter(user -> user instanceof Duck)
                .map(user -> (Duck) user)
                .collect(Collectors.toList());
    }

    private long generateId() {
        return repository.getAll().stream().map(User::getId).max(Long::compare).orElse(0L) + 1;
    }
}
