package org.example.template.service;

import org.example.template.domain.Driver;
import org.example.template.repository.DBPagingAsyncDriversRepository;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DriversService extends Service {
    private static DriversService instance;
    private DBPagingAsyncDriversRepository repository;

    public static DriversService getInstance() {
        if(instance == null)
            instance = new DriversService();

        return instance;
    }

    private DriversService() {
        repository = new DBPagingAsyncDriversRepository();
    }

    public CompletableFuture<List<Driver>> getFreeDrivers() {
        return repository.findAllAsync().thenApply(drivers -> {
            return drivers.stream()
                    .filter(driver -> {
                        try {
                            return OrdersService.getInstance().findAllInProgressForDriver(driver.getId()).get().isEmpty();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .sorted(Comparator.comparing(d -> OrdersService.getInstance().getLatestOrderDate(d.getId())))
                    .toList();
        });
    }

    public Iterable<Driver> findAll() {
        return repository.findAll();
    }

    public CompletableFuture<List<Driver>> findAllAsync() {
        return repository.findAllAsync();
    }
}
