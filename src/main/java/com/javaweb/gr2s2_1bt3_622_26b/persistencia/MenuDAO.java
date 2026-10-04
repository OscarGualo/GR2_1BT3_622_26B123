package com.javaweb.gr2s2_1bt3_622_26b.persistencia;

import com.javaweb.gr2s2_1bt3_622_26b.modelo.Menu;
import java.time.LocalDate;

public class MenuDAO extends GenericDAO<Menu> {
    public MenuDAO() { super(Menu.class); }

    /** Devuelve el menú con fecha de hoy, o null si aún no se creó. */
    public Menu buscarMenuDelDia() {
        return consultar(em -> em.createQuery(
                        "SELECT m FROM Menu m WHERE m.fecha = :hoy", Menu.class)
                .setParameter("hoy", LocalDate.now())
                .getResultStream().findFirst().orElse(null));
    }
}