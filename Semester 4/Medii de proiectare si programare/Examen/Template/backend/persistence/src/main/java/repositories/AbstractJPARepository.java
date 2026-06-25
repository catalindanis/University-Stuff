package repositories;

import entities.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JpaUtils;

import java.util.List;

public abstract class AbstractJPARepository<ID, T extends Entity<ID>> implements Repository<ID, T> {
    private final Logger logger = LogManager.getLogger();

    private final Class<T> entityClass;
    protected AbstractJPARepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public ID save(T entity) {
        logger.traceEntry("entity={}", entity);
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
                logger.debug("Saved {} entity with id={}", entityClass.getSimpleName(), entity.getId());
                logger.traceExit(entity.getId());
                return entity.getId();
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                logger.error("Failed to save {} entity: {}", entityClass.getSimpleName(), entity, e);
                logger.catching(e);
                throw e;
            }
        }
        catch (Exception e) {
            logger.error("Unexpected error while saving {} entity", entityClass.getSimpleName(), e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public void delete(ID id) {
        logger.traceEntry("id={}", id);
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            T entity = entityManager.find(entityClass, id);

            if (entity != null) {
                entityManager.getTransaction().begin();
                try {
                    entityManager.remove(entity);
                    entityManager.getTransaction().commit();
                    logger.debug("Deleted {} entity with id={}", entityClass.getSimpleName(), id);
                    logger.traceExit(entity);
                    return;
                } catch (Exception e) {
                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                    logger.error("Failed to delete {} entity with id={}", entityClass.getSimpleName(), id, e);
                    logger.catching(e);
                    throw e;
                }
            } else {
                logger.debug("No {} entity found with id={}, nothing to delete", entityClass.getSimpleName(), id);
            }
        }
        logger.traceExit();
    }

    @Override
    public void update(T entity) {
        logger.traceEntry("entity={}", entity);
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            entityManager.getTransaction().begin();
            try {
                entityManager.merge(entity);
                entityManager.getTransaction().commit();
                logger.debug("Updated {} entity with id={}", entityClass.getSimpleName(), entity.getId());
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                logger.error("Failed to update {} entity: {}", entityClass.getSimpleName(), entity, e);
                logger.catching(e);
                throw e;
            }
        }
        logger.traceExit(entity);
    }

    @Override
    public T findOne(ID id) {
        logger.traceEntry("id={}", id);
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            T t = entityManager.find(entityClass, id);
            logger.debug("Found {} entity with id={}: {}", entityClass.getSimpleName(), id, t);
            logger.traceExit(t);
            return t;
        }
        catch (Exception e) {
            logger.error("Failed to find {} entity with id={}", entityClass.getSimpleName(), id, e);
            logger.traceExit();
        }
        return null;
    }

    @Override
    public List<T> findAll() {
        logger.traceEntry();
        try (EntityManager entityManager = JpaUtils.getEntityManager()) {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
            List<T> resultList = query.getResultList();
            logger.debug("Found {} {} entities", resultList.size(), entityClass.getSimpleName());
            logger.traceExit(resultList);
            return resultList;
        }
        catch (Exception e) {
            logger.error("Failed to find all {} entities", entityClass.getSimpleName(), e);
            logger.traceExit();
        }
        return null;
    }
}