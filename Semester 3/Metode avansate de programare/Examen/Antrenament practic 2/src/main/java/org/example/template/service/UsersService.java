package org.example.template.service;

import lombok.Getter;
import org.example.template.domain.User;
import org.example.template.repository.Repository;
import org.example.template.repository.UsersRepository;

public class UsersService extends Service {
    @Getter
    private static final UsersService instance = new UsersService();
    private final UsersRepository repository;

    private UsersService() {
        repository = new UsersRepository();
    }

    public User login(String username, String password) {
        return repository.findByCredentials(username, password);
    }
}
