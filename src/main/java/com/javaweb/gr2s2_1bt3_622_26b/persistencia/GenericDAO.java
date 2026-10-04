package com.javaweb.gr2s2_1bt3_622_26b.persistencia;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.function.Function;

public class GenericDAO<T> {
    private final Class<T> clase;

    public GenericDAO(Class<T> clase) {
        this.clase = clase;
    }
    protected <R> R enTransaccion(Function<EntityManager, R> operacion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            R resultado = operacion.apply(em);
            em.getTransaction().commit();
            return resultado;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /** Ejecuta una consulta de solo lectura y cierra el EntityManager. */
    protected <R> R consultar(Function<EntityManager, R> consulta) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return consulta.apply(em);
        } finally {
            em.close();
        }
    }

    public void guardar(T entidad) {
        enTransaccion(em -> { em.persist(entidad); return null; });
    }

    public T actualizar(T entidad) {
        return enTransaccion(em -> em.merge(entidad));
    }

    public T buscarPorId(Object id) {
        return consultar(em -> em.find(clase, id));
    }

    public List<T> listarTodos() {
        return consultar(em -> em.createQuery(
                "SELECT e FROM " + clase.getSimpleName() + " e", clase).getResultList());
    }

    public void eliminar(Object id) {
        enTransaccion(em -> {
            T entidad = em.find(clase, id);
            if (entidad != null) {
                em.remove(entidad);
            }
            return null;
        });
    }
}