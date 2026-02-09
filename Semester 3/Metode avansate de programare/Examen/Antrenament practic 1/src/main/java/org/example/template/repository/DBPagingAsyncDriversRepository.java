package org.example.template.repository;

import org.example.template.domain.Driver;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBPagingAsyncDriversRepository extends DBPagingDriversRepository {
    ExecutorService executor = Executors.newCachedThreadPool();

    public CompletableFuture<Driver> findByIDAsync(Integer ID) {
        return CompletableFuture.supplyAsync(() -> super.findById(ID), executor);
    }

    public CompletableFuture<List<Driver>> findAllAsync() {
        return CompletableFuture.supplyAsync(super::findAll, executor);
    }
}
