package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Car extends Entity<Integer> {
    private String name;
    private String description;
    private double price;
    private CarStatus status;

    public Car(Integer integer, String name, String description, double price, CarStatus status) {
        super(integer);
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }
}
