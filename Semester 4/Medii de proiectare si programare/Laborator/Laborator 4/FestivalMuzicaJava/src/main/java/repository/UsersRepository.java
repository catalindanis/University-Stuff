package repository;

import domain.User;

import java.util.Optional;

public interface UsersRepository extends Repository<Long, User> {
    Optional<User> findByEmailAndPassword(String email, String password);
}
