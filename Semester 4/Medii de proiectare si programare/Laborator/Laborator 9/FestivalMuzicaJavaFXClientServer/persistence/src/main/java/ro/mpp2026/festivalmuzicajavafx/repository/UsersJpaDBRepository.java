package ro.mpp2026.festivalmuzicajavafx.repository;

import ro.mpp2026.festivalmuzicajavafx.domain.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UsersJpaDBRepository implements UsersRepository {

    @Override
    public int size() {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class);
            return query.getSingleResult().intValue();
        }
    }

    @Override
    public void save(User entity) {
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
            User user = entityManager.find(User.class, aLong);
            if (user != null) {
                entityManager.getTransaction().begin();
                try {
                    entityManager.remove(user);
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
    public void update(Long aLong, User entity) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                User user = entityManager.find(User.class, aLong);
                if (user != null) {
                    user.setEmail(entity.getEmail());
                    user.setPassword(entity.getPassword());
                    entityManager.merge(user);
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
    public User findOne(Long aLong) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            return entityManager.find(User.class, aLong);
        }
    }

    @Override
    public Iterable<User> findAll() {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email AND u.password = :password",
                    User.class
            );
            query.setParameter("email", email);
            query.setParameter("password", password);
            
            List<User> results = query.getResultList();
            return results.stream().findFirst();
        }
    }
}
