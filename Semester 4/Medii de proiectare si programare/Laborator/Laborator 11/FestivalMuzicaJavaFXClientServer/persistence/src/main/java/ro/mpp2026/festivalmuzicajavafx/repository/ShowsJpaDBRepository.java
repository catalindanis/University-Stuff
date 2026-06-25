package ro.mpp2026.festivalmuzicajavafx.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ShowsJpaDBRepository implements ShowsRepository {
    @Override
    public int size() {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(s) FROM Show s", Long.class);
            return query.getSingleResult().intValue();
        }
    }

    @Override
    public Long save(Show entity) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
                return entity.getId();
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
            Show show = entityManager.find(Show.class, aLong);
            if (show != null) {
                entityManager.getTransaction().begin();
                try {
                    entityManager.remove(show);
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
    public void update(Long aLong, Show entity) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                Show show = entityManager.find(Show.class, aLong);
                if (show != null) {
                    show.setArtistName(entity.getArtistName());
                    show.setDate(entity.getDate());
                    show.setLocation(entity.getLocation());
                    show.setRemainingSeats(entity.getRemainingSeats());
                    entityManager.merge(show);
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
    public Show findOne(Long aLong) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            return entityManager.find(Show.class, aLong);
        }
    }

    @Override
    public Iterable<Show> findAll() {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Show> query = entityManager.createQuery("SELECT s FROM Show s", Show.class);
            return query.getResultList();
        }
    }

    @Override
    public Iterable<Show> findAll(ShowFilter filter) {
        if (filter == null) {
            return findAll();
        }

        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            StringBuilder jpql = new StringBuilder("SELECT s FROM Show s WHERE 1=1");
            List<Object> params = new ArrayList<>();
            List<String> names = new ArrayList<>();

            if (filter.artistName() != null && !filter.artistName().isEmpty()) {
                jpql.append(" AND s.artistName LIKE :artistName");
                names.add("artistName");
                params.add("%" + filter.artistName() + "%");
            }
            if (filter.date() != null) {
                jpql.append(" AND s.date = :date");
                names.add("date");
                params.add(filter.date());
            }
            if (filter.location() != null && !filter.location().isEmpty()) {
                jpql.append(" AND s.location LIKE :location");
                names.add("location");
                params.add("%" + filter.location() + "%");
            }
            if (filter.remainingSeats() != null) {
                jpql.append(" AND s.remainingSeats = :remainingSeats");
                names.add("remainingSeats");
                params.add(filter.remainingSeats());
            }

            TypedQuery<Show> query = entityManager.createQuery(jpql.toString(), Show.class);
            for (int i = 0; i < names.size(); i++) {
                query.setParameter(names.get(i), params.get(i));
            }
            return query.getResultList();
        }
    }
}
