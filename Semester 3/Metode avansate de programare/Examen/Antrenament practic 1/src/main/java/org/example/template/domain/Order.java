package org.example.template.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class Order extends Entity<Integer> {
    private Integer driverId;
    private OrderStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String pickupAdress;
    private String destinationAdress;
    private String clientName;

    public Order(Integer integer, Integer driverId, OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, String pickupAdress, String destinationAdress, String clientName) {
        super(integer);
        this.driverId = driverId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupAdress = pickupAdress;
        this.destinationAdress = destinationAdress;
        this.clientName = clientName;
    }
}

