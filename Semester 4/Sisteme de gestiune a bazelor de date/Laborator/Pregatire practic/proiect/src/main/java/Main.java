import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");

        EntityManager entityManager1 = emf.createEntityManager();
        EntityManager entityManager2 = emf.createEntityManager();

//        initData(entityManager);

        dirtyReads(entityManager1, entityManager2);

        phantomReads(entityManager1, entityManager2);

        entityManager1.close();
        entityManager2.close();
        emf.close();
    }

    public static void initData(EntityManager entityManager) {
        entityManager.getTransaction().begin();
        for(int i = 0; i < 100; i++) {
            entityManager.persist(
                    new Customer(
                            null,
                            "name" + i,
                            "city" + i,
                            LocalDate.now()
                    )
            );
        }
        entityManager.getTransaction().commit();
    }

    public static void dirtyReads(EntityManager entityManager1, EntityManager entityManager2) {
        entityManager1.getTransaction().begin();
        entityManager2.getTransaction().begin();
        entityManager1.createNativeQuery("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED").executeUpdate();
        entityManager2.createNativeQuery("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED").executeUpdate();

        Customer customer = entityManager1.find(Customer.class, 1);
        customer.setName("anotherName");
        entityManager1.flush();

        Customer customerAfterUpdate = entityManager2.find(Customer.class, 1);
        System.out.println(customerAfterUpdate.getName());

        entityManager1.getTransaction().rollback();
        entityManager2.getTransaction().rollback();
    }

    public static void phantomReads(EntityManager entityManager1, EntityManager entityManager2) {
        entityManager1.getTransaction().begin();
        entityManager2.getTransaction().begin();
        entityManager1.createNativeQuery("SET TRANSACTION ISOLATION LEVEL READ COMMITTED").executeUpdate();
        entityManager2.createNativeQuery("SET TRANSACTION ISOLATION LEVEL READ COMMITTED").executeUpdate();

        String phantomCity = "phantom_city";

        Long countBefore = (Long) entityManager1
                .createQuery("SELECT COUNT(c) FROM Customer c WHERE c.city = :city")
                .setParameter("city", phantomCity)
                .getSingleResult();

        entityManager2.persist(new Customer(null, "phantom_user", phantomCity, LocalDate.now()));
        entityManager2.flush();
        entityManager2.getTransaction().commit();

        Long countAfter = (Long) entityManager1
                .createQuery("SELECT COUNT(c) FROM Customer c WHERE c.city = :city")
                .setParameter("city", phantomCity)
                .getSingleResult();

        System.out.println("Phantom reads - before: " + countBefore + ", after: " + countAfter);

        entityManager1.getTransaction().rollback();
    }
}
