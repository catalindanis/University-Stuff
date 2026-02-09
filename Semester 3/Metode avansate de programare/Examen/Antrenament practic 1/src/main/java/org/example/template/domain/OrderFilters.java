package org.example.template.domain;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class OrderFilters {
    public Optional<Integer> driverId;
    public Optional<OrderStatus> status;
}
