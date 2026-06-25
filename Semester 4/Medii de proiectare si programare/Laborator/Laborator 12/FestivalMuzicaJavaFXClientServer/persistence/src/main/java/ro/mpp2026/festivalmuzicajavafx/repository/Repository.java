package ro.mpp2026.festivalmuzicajavafx.repository;

import ro.mpp2026.festivalmuzicajavafx.domain.Entity;

public interface Repository<ID, T extends Entity<ID>> {
    int size();
    ID save(T entity);
    void delete(ID id);
    void update(ID id, T entity);
    T findOne(ID id);
    Iterable<T> findAll();
}
