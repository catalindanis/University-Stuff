package org.example.template.service;

import lombok.Getter;
import org.example.template.domain.Coin;
import org.example.template.domain.Transaction;
import org.example.template.domain.User;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsersService extends Service {
    @Getter
    private static final UsersService instance = new UsersService();
    private final UsersRepository repository;

    private ExecutorService executors;

    private UsersService() {
        repository = new UsersRepository();

        executors = Executors.newCachedThreadPool();
    }

    public void update(Integer id, User user) {
        repository.update(id, user);
    }

    public void placeBuyOrder(Integer userId, Integer coinId) {
        CompletableFuture.runAsync(() -> {
            User user = UsersService.getInstance().findById(userId);
            Coin coin = CoinsService.getInstance().findById(coinId);

            if(user.getBalance() >= coin.getPrice()) {
                user.setBalance(user.getBalance() - coin.getPrice());
                update(user.getId(), user);
                TransactionsService.getInstance().save(new Transaction(1, user.getId(), coin.getSymbol(), "BUY", coin.getPrice(), LocalDateTime.now()));

                notifyObservers(new EntityChangeEvent(EntityChangeEventType.PLACEHOLDER_ADDED, user.getId()));
            }
        }, executors);
    }

    public void placeSellOrder(Integer userId, Integer coinId) {
        CompletableFuture.runAsync(() -> {
            User user = UsersService.getInstance().findById(userId);
            Coin coin = CoinsService.getInstance().findById(coinId);

            long numberOfBuys = TransactionsService.getInstance().findAll().stream()
                    .filter(t -> t.getUserId() == user.getId())
                    .filter(t -> t.getCoinSymbol().equals(coin.getSymbol()))
                    .filter(t -> t.getType().equals("BUY"))
                    .count();

            long numberOfSells = TransactionsService.getInstance().findAll().stream()
                    .filter(t -> t.getUserId() == user.getId())
                    .filter(t -> t.getCoinSymbol().equals(coin.getSymbol()))
                    .filter(t -> t.getType().equals("SELL"))
                    .count();

            if(numberOfBuys > numberOfSells) {
                user.setBalance(user.getBalance() + coin.getPrice());
                update(user.getId(), user);
                TransactionsService.getInstance().save(new Transaction(1, user.getId(), coin.getSymbol(), "SELL", coin.getPrice(), LocalDateTime.now()));

                notifyObservers(new EntityChangeEvent(EntityChangeEventType.PLACEHOLDER_REMOVED, user.getId()));
            }
        }, executors);
    }

    public double getBudgetForUser(Integer id) {
        return repository.findById(id).getBalance();
    }

    public User findById(Integer id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
