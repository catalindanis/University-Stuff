package repositories;

import models.User;

import java.util.ArrayList;
import java.util.List;

public abstract class UsersRepository implements Repository<Long, User> {
    protected List<User> users;

    public UsersRepository() {
        users = new ArrayList<>();
    }

    @Override
    public abstract User add(User user);

    @Override
    public abstract User remove(User user);

    @Override
    public User get(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return users;
    }
}
