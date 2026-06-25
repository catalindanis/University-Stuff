package repository;

import domain.Entity;

public interface Repository<ID, T extends Entity<ID>> {
    int size();
    void save(T entity);
    void delete(ID id);
    void update(ID id, T entity);
    T findOne(ID id);
    Iterable<T> findAll();
}
