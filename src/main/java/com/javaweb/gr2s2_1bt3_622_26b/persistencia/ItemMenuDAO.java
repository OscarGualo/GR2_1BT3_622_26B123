package com.javaweb.gr2s2_1bt3_622_26b.persistencia;

import com.javaweb.gr2s2_1bt3_622_26b.modelo.ItemMenu;
import com.javaweb.gr2s2_1bt3_622_26b.modelo.Menu;
import java.util.List;

public class ItemMenuDAO extends GenericDAO<ItemMenu> {
    public ItemMenuDAO() { super(ItemMenu.class); }

    /** Ítems de un menú con un estado dado (por ejemplo "Disponible"). */
    public List<ItemMenu> listarPorMenuYEstado(Menu menu, String estado) {
        return consultar(em -> em.createQuery(
                        "SELECT i FROM ItemMenu i WHERE i.menu.id = :idMenu AND i.estado = :estado "
                                + "ORDER BY i.nombre", ItemMenu.class)
                .setParameter("idMenu", menu.getId())
                .setParameter("estado", estado)
                .getResultList());
    }
}