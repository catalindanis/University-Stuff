package ro.mpp2026.festivalmuzicajavafx.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;

public class TicketsJpaDBRepository implements TicketsRepository {
    @Override
    public int size() {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(t) FROM Ticket t", Long.class);
            return query.getSingleResult().intValue();
        }
    }

    @Override
    public void save(Ticket entity) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public void delete(Long aLong) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            Ticket ticket = entityManager.find(Ticket.class, aLong);
            if (ticket != null) {
                entityManager.getTransaction().begin();
                try {
                    entityManager.remove(ticket);
                    entityManager.getTransaction().commit();
                } catch (Exception e) {
                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                    throw e;
                }
            }
        }
    }

    @Override
    public void update(Long aLong, Ticket entity) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                Ticket ticket = entityManager.find(Ticket.class, aLong);
                if (ticket != null) {
                    ticket.setClientName(entity.getClientName());
                    ticket.setShow(entity.getShow());
                    ticket.setNoSeats(entity.getNoSeats());
                    entityManager.merge(ticket);
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                throw e;
            }
        }
    }

    @Override
    public Ticket findOne(Long aLong) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            return entityManager.find(Ticket.class, aLong);
        }
    }

    @Override
    public Iterable<Ticket> findAll() {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Ticket> query = entityManager.createQuery("SELECT t FROM Ticket t", Ticket.class);
            return query.getResultList();
        }
    }

    @Override
    public Iterable<Ticket> findAllForShow(Long showId) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Ticket> query = entityManager.createQuery(
                    "SELECT t FROM Ticket t WHERE t.show.id = :showId",
                    Ticket.class
            );
            query.setParameter("showId", showId);
            return query.getResultList();
        }
    }
}
