package org.example.template.service;

import lombok.Getter;
import org.example.template.domain.Car;
import org.example.template.domain.CarStatus;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.repository.CarsRepository;

import java.util.List;
import java.util.Map;

public class CarsService extends Service {
    @Getter
    private static final CarsService instance = new CarsService();
    private final CarsRepository repository;

    private CarsService() {
        repository = new CarsRepository();
    }

    public void sendToAdmins(Car car, String userInput) {
        car.setStatus(CarStatus.NEEDS_APPROVAL);
        repository.update(car.getId(), car);
        Map<String, String> data = Map.of("car", car.getId().toString(), "userInput", userInput);
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.PLACEHOLDER_UPDATED, data));
    }

    public List<Car> getAllWaiting() {
        return repository.findAll().stream()
                .filter(car -> car.getStatus() == CarStatus.NEEDS_APPROVAL)
                .toList();
    }

    public List<Car> getAll() {
        return repository.findAll();
    }
}
