package ro.mpp2026.festivalmuzicajavafx.repository;

public interface FilterableRepository<Entity, EntityFiler> {
    Iterable<Entity> findAll(EntityFiler filter);
}
