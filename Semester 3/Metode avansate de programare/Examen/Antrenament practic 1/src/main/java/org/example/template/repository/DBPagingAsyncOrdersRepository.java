package org.example.template.repository;

import org.example.template.domain.Order;
import org.example.template.domain.OrderFilters;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBPagingAsyncOrdersRepository extends DBPagingOrdersRepository {
    ExecutorService executor = Executors.newCachedThreadPool();

    public CompletableFuture<Order> findByIDAsync(Integer ID) {
        return CompletableFuture.supplyAsync(() -> super.findById(ID), executor);
    }

    public CompletableFuture<List<Order>> findAllAsync() {
        return CompletableFuture.supplyAsync(super::findAll, executor);
    }

    public CompletableFuture<Iterable<Order>> findPageAsync(int pageNumber, int pageSize) {
        return CompletableFuture.supplyAsync(() -> { return super.findPage(pageNumber, pageSize); }, executor);
    }

    public CompletableFuture<List<Order>> findAllAsyncFiltered(OrderFilters filters) {
        return CompletableFuture.supplyAsync(() -> { return super.findAllFiltered(filters); }, executor);
    }
}
