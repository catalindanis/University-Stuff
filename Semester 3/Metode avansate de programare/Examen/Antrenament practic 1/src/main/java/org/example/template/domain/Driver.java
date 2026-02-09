package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Driver extends Entity<Integer> {
    private String name;

    public Driver(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
