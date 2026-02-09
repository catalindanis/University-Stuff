package Service;

import Domain.Driver;
import Domain.Order;
import Domain.OrderStatus;
import Observer.Observable;
import Observer.Observer;
import Repository.DriverRepo;
import Repository.OrderRepo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service implements Observable {
    private DriverRepo driverRepo;
    private OrderRepo orderRepo;
    private Map<Integer, Order> pendingNotifications = new HashMap<>();
    private List<Observer> observers = new ArrayList<>();
    private volatile boolean orderAccepted = false;
    private volatile int currentOrderId = -1;

    public Service(DriverRepo driverRepo, OrderRepo orderRepo) {
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : new ArrayList<>(observers)) {
            o.update();
        }
    }

    public List<Driver> getAllDrivers() {
        return driverRepo.findAll();
    }

    public List<Order> getActiveOrders(int driverId) {
        return orderRepo.findAllActiveByDriver(driverId);
    }

    public void addOrder(String p, String d, String c) {
        Order order = new Order(null, null, OrderStatus.PENDING, LocalDateTime.now(), null, p, d, c);
        order = orderRepo.save(order);
        if (order.getId() != null) {
            startNotificationChain(order);
        }
    }

    private void startNotificationChain(Order order) {
        List<Driver> eligible = getEligibleDrivers();
        if (eligible.isEmpty()) return;
        orderAccepted = false;
        currentOrderId = order.getId();
        notifyNextDriver(order, eligible, 0);
    }

    private void notifyNextDriver(Order order, List<Driver> eligible, int index) {
        if (index >= eligible.size() || orderAccepted) return;
        if (currentOrderId != -1 && !order.getId().equals(currentOrderId)) return;

        Driver current = eligible.get(index);
        pendingNotifications.put(current.getId(), order);
        notifyObservers();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                Order pending = pendingNotifications.get(current.getId());
                if (!orderAccepted && pending != null && pending.getId().equals(order.getId())) {
                    pendingNotifications.remove(current.getId());
                    javafx.application.Platform.runLater(() -> {
                        notifyObservers();
                        notifyNextDriver(order, eligible, index + 1);
                    });
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }


    private List<Driver> getEligibleDrivers() {
        return driverRepo.findEligibleDrivers();
    }

    public Order getNotificationForDriver(int driverId) {
        return pendingNotifications.get(driverId);
    }

    public void acceptOrder(int driverId, int orderId) {
        orderAccepted = true;
        pendingNotifications.clear();
        orderRepo.update(orderId, driverId, "IN_PROGRESS", null);
        notifyObservers();
    }

    public void finishOrder(int orderId, int driverId) {
        orderRepo.update(orderId, driverId, "FINISHED", Timestamp.valueOf(LocalDateTime.now()));
        notifyObservers();
    }
}
