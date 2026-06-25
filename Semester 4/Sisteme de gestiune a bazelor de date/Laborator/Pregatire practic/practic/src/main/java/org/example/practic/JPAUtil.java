package org.example.practic;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static EntityManagerFactory emf;
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("SQLServerPU");
        } return emf;
    }
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) { emf.close(); }
    }
}
