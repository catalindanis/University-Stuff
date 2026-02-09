package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User extends Entity<Integer> {
    private String username;
    private String password;
    private UserRole role;

    public User(Integer integer, String username, String password, UserRole role) {
        super(integer);
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
