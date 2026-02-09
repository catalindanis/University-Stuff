package repositories;

import models.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    E add(E e);
    E remove(E e);
    E get(ID id);
    List<E> getAll();
}
