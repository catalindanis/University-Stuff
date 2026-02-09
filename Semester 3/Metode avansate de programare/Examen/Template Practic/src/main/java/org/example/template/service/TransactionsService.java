package org.example.template.service;

import lombok.Getter;
import org.example.template.domain.Transaction;
import org.example.template.repository.TransactionsRepository;

import java.util.Comparator;
import java.util.List;

public class TransactionsService extends Service {
    @Getter
    private static final TransactionsService instance = new TransactionsService();
    private final TransactionsRepository repository;

    private TransactionsService() {
        repository = new TransactionsRepository();
    }

    public void save(Transaction transaction) {
        repository.save(transaction);
    }

    public List<Transaction> findAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getDate)
                        .reversed())
                .toList();
    }
}
