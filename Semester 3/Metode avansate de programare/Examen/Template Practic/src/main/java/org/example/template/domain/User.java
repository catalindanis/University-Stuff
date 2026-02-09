package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User extends Entity<Integer> {
    private String name;
    private double balance;

    public User(Integer integer, String name, double balance) {
        super(integer);
        this.name = name;
        this.balance = balance;
    }
}
