package repos;

import models.Entity;

import java.util.Optional;

public interface Repository<ID, E extends Entity<ID>> {
    Optional<E> findById(ID id);

    Iterable<E> findAll();

    Optional<E> save(E e);

    Optional<E> update(E e);

    Optional<E> delete(ID id);
}
