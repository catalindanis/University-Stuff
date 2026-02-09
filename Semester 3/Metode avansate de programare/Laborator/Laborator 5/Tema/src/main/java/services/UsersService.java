package services;

import factories.UsersFactory;
import lombok.Getter;
import models.Duck;
import models.DuckDTO;
import models.PersonDTO;
import models.User;
import repositories.DatabaseUsersRepository;
import repositories.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class UsersService implements Service<User> {
    @Getter
    private static final UsersService instance = new UsersService();
    private final Repository<Long, User> repository;

    private UsersService() {
        repository = new DatabaseUsersRepository("jdbc:postgresql://localhost:5432/DuckSocialNetwork", "laborator", "1234");
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

    public List<Duck> getDucks() {
        return repository.getAll().stream()
                .filter(user -> user instanceof Duck)
                .map(user -> (Duck) user)
                .collect(Collectors.toList());
    }
}
