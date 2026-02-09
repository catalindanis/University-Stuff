package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Transaction extends Entity<Integer> {
    private Integer userId;
    private String coinSymbol;
    private String type;
    private double price;
    private LocalDateTime date;

    public Transaction(Integer integer, Integer userId, String coinSymbol, String type, double price, LocalDateTime date) {
        super(integer);
        this.userId = userId;
        this.coinSymbol = coinSymbol;
        this.type = type;
        this.price = price;
        this.date = date;
    }
}
