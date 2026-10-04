package com.javaweb.gr2s2_1bt3_622_26b.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JPAUtil {
    private static final String UNIDAD_PERSISTENCIA = "PoliDinnerPU";
    private static volatile EntityManagerFactory emf;

    private JPAUtil() {
    }

    public static EntityManagerFactory getEmf() {
        EntityManagerFactory instancia = emf;
        if (instancia == null || !instancia.isOpen()) {
            synchronized (JPAUtil.class) {
                instancia = emf;
                if (instancia == null || !instancia.isOpen()) {
                    instancia = Persistence.createEntityManagerFactory(UNIDAD_PERSISTENCIA);
                    emf = instancia;
                }
            }
        }
        return instancia;
    }

    public static EntityManager getEntityManager() {
        return getEmf().createEntityManager();
    }

    public static void cerrar() {
        synchronized (JPAUtil.class) {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            emf = null;
        }
    }
}
