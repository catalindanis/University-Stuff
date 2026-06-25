package repositories;

import entities.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    ID save(E entity);
    void delete(ID id);
    void update(E entity);
    E findOne(ID id);
    List<E> findAll();
}
