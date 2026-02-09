package Domain;

import java.time.LocalDateTime;

public class Order {
    private Integer id, driverId;
    private OrderStatus status;
    private LocalDateTime startDate,endDate;
    private String pickupAddress,destinationAddress,clientName;

    public Order(Integer id, Integer driverId, OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, String pickupAddress, String destinationAddress, String clientName) {
        this.id = id;
        this.driverId = driverId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pickupAddress = pickupAddress;
        this.destinationAddress = destinationAddress;
        this.clientName = clientName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getClientName() {
        return clientName;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
