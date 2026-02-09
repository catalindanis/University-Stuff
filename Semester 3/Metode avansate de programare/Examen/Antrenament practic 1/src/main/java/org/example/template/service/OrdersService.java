package org.example.template.service;

import lombok.Getter;
import org.example.template.domain.Driver;
import org.example.template.domain.Order;
import org.example.template.domain.OrderFilters;
import org.example.template.domain.OrderStatus;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.repository.DBPagingAsyncOrdersRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class OrdersService extends Service {
    @Getter
    private final static OrdersService instance = new OrdersService();
    private final DBPagingAsyncOrdersRepository repository;

    private OrdersService() {
        repository = new DBPagingAsyncOrdersRepository();
    }

    public void add(String pickupAddress, String destinationAddress, String clientName) {
        Order order = new Order(0, null, OrderStatus.PENDING, LocalDateTime.now(), null, pickupAddress, destinationAddress, clientName);
        Integer orderId = repository.save(order);
        order.setId(orderId);
        DriversService.getInstance().getFreeDrivers().thenAccept(drivers -> {
            for(Driver driver : drivers) {
                notifyObservers(new EntityChangeEvent(EntityChangeEventType.ADDED, Map.of("driverId", driver.getId(), "order", order)));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(repository.findById(orderId).getStatus() != OrderStatus.PENDING)
                    break;
            }
        });
    }

    public synchronized void acceptOrder(Integer orderId, Integer driverId) {
        Order order = repository.findById(orderId);
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setDriverId(driverId);
        order.setEndDate(LocalDateTime.now());
        repository.update(orderId, order);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.UPDATED, driverId));
    }

    public CompletableFuture<List<Order>> findAllInProgressForDriver(Integer driverId) {
        OrderFilters filters = new OrderFilters(Optional.of(driverId), Optional.of(OrderStatus.IN_PROGRESS));
        return repository.findAllAsyncFiltered(filters);
    }

    public synchronized void finishOrder(Integer id) {
        Order order = repository.findById(id);
        order.setStatus(OrderStatus.FINISHED);
        order.setEndDate(LocalDateTime.now());
        repository.update(id, order);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.UPDATED, order.getDriverId()));
    }

    public LocalDateTime getLatestOrderDate(Integer driverId) {
        var result = repository.findAll().stream()
                .filter(order -> Objects.equals(order.getDriverId(), driverId))
                .max(Comparator.comparing(Order::getEndDate))
                .filter(o -> o.getEndDate() != null)
                .map(Order::getEndDate)
                .orElse(LocalDateTime.MIN);
        return result;
    }
}
