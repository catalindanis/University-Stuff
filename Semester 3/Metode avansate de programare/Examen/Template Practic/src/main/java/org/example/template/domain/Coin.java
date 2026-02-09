package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coin extends Entity<Integer> {
    private String symbol;
    private String name;
    private double price;

    public Coin(Integer integer, String symbol, String name, double price) {
        super(integer);
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}
