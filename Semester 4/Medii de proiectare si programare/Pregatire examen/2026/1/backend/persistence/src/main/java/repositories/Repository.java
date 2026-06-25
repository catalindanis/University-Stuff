package repositories;

import entities.Entity;

import java.util.List;

public interface Repository<ID, T extends Entity<ID>> {
    ID save(T entity);
    void delete(ID id);
    void update(T entity);
    T findOne(ID id);
    List<T> findAll();
}
