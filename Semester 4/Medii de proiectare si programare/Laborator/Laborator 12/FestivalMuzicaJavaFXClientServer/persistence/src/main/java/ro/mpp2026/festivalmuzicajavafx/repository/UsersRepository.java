package ro.mpp2026.festivalmuzicajavafx.repository;

import ro.mpp2026.festivalmuzicajavafx.domain.User;

import java.util.Optional;

public interface UsersRepository extends Repository<Long, User> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}
