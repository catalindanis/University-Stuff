package org.example.template.service;

import lombok.Getter;
import org.example.template.domain.Coin;
import org.example.template.domain.User;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.repository.CoinsRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoinsService extends Service {
    @Getter
    private static final CoinsService instance = new CoinsService();
    private final CoinsRepository repository;

    private ExecutorService executors;

    private CoinsService() {
        repository = new CoinsRepository();

        executors = Executors.newCachedThreadPool();
        startMarketSimulation();
    }

    public List<Coin> findAll() {
        return repository.findAll();
    }

    public Coin findById(Integer id) {
        return repository.findById(id);
    }

    public void update(Integer id, Coin coin) {
        repository.update(id, coin);
    }

    public void startMarketSimulation() {
        CompletableFuture.runAsync(() -> {
            while(true) {
                try {
                    CoinsService.getInstance().findAll().forEach(coin -> {
                        double randomValue = ((int) ((Math.random() * 10 % 10) * (Math.random() <= 0.5 ? -1 : 1)));
                        coin.setPrice(coin.getPrice() + randomValue);
                        if(coin.getPrice() <= 0)
                            coin.setPrice(1.0);
                        CoinsService.getInstance().update(coin.getId(), coin);
                    });
                    notifyObservers(new EntityChangeEvent(EntityChangeEventType.PLACEHOLDER_UPDATED, null));
                    Thread.sleep(3000);
                } catch(InterruptedException e) {

                }
            }
        }, executors);
    }
}
