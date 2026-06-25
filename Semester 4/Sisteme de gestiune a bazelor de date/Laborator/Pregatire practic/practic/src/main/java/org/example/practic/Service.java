package org.example.practic;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.practic.domain.Customer;
import org.hibernate.Session;

import java.sql.Date;

public class Service {
    public void dirtyRead(){
        Thread t1 = new Thread(() ->{
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            EntityManager em = emf.createEntityManager();

            EntityTransaction tx = em.getTransaction();
            try{
                tx.begin();
                System.out.println("Thread 1: Starting transaction");
                Customer customer = em.find(Customer.class, 1L);
                customer.setCity("Paris");
                em.flush();
                System.out.println("Thread 1: Updated city to Paris but not committed yet");
                Thread.sleep(5000);
                System.out.println("Thread 1: Aborting transaction");
                tx.rollback();
            } catch (Exception e) {
                if(tx.isActive()){
                    tx.rollback();
                }
            } finally {
                em.close();
            }
        });

        Thread t2 = new Thread(() ->{
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            try{
                Thread.sleep(1000);
                tx.begin();
                System.out.println("Thread 2: Starting transaction");
                Session session = em.unwrap(Session.class);
                session.doWork(connection -> {
                    connection.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
                });
                em.clear();
                Customer dirtyCustomer = em.find(Customer.class, 1L);
                System.out.println("Thread 2: Read city as " + dirtyCustomer.getCity() + " (dirty read)");
                tx.commit();
            }catch (Exception e){
                if(tx.isActive()){
                    tx.rollback();
                }
            }finally {
                em.close();
            }
        });
        t1.start();
        t2.start();
    }

    public void phantomRead(){
        Thread t1 = new Thread(() ->{
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            try{
                tx.begin();
                System.out.println("Thread 1: Starting transaction");
                Session session = em.unwrap(Session.class);
                session.doWork(connection -> {
                    connection.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
                });
                long count1 = em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class).getSingleResult();
                System.out.println("Thread 1: Customer count before: " + count1);
                Thread.sleep(5000);

                em.clear();

                long  count2 = em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class).getSingleResult();
                System.out.println("Thread 1: Customer count after: " + count2);

                tx.commit();
            }catch (Exception e){
                if(tx.isActive()){
                    tx.rollback();
                }
            }finally {
                em.close();
            }
        });

        Thread t2 = new Thread(() ->{
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            try{
                Thread.sleep(2000);
                tx.begin();
                System.out.println("Thread 2: Starting transaction");

                Customer phantomCustomer = new Customer();
                phantomCustomer.setName("Phantom");
                phantomCustomer.setCity("Paris");
                phantomCustomer.setRegistrationDate(new Date(System.currentTimeMillis()));
                em.persist(phantomCustomer);
                tx.commit();
                System.out.println("Thread 2: Inserted new customer 'Phantom'");

            }catch (Exception e){
                if(tx.isActive()){
                    tx.rollback();
                }
            }finally {
                em.close();
            }
        });
        t1.start();
        t2.start();
    }

    public void setupAndSeedDatabase() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Customer existing = em.find(Customer.class, 1L);

            if (existing == null) {
                System.out.println("[Setup] Customers table created/verified. Seeding initial record...");
                Customer alice = new Customer();
                alice.setName("Alice");
                alice.setCity("London");
                alice.setRegistrationDate(new Date(System.currentTimeMillis()));

                em.persist(alice);
                System.out.println("[Setup] Seed complete! 'Alice' added to database.");
            } else {
                System.out.println("[Setup] Database already has data. Skipping seed.");
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
