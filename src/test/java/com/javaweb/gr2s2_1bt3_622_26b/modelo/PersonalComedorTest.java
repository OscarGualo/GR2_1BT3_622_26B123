package com.javaweb.gr2s2_1bt3_622_26b.modelo;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/** Trazabilidad: caso de prueba CU 02 "Seco de Pollo" y métodos de la Fig. 7. */
class PersonalComedorTest {

    private final PersonalComedor personal =
            new PersonalComedor("Ana", "Pérez", "Administrador", "1700000001", "1234");

    @Test
    void eliminarPlatoDejaElPlatoAgotado() {
        Plato plato = new Plato("Seco de Pollo", 3.00, ItemMenu.DISPONIBLE, "Pollo guisado");
        personal.eliminarPlato(plato);
        assertEquals(ItemMenu.AGOTADO, plato.getEstado());
    }

    @Test
    void eliminarProductoDejaElProductoAgotado() {
        Producto agua = new Producto("Agua", 0.60, ItemMenu.DISPONIBLE, "500 ml", 20, LocalDate.now().plusMonths(6));
        personal.eliminarProducto(agua);
        assertEquals(ItemMenu.AGOTADO, agua.getEstado());
    }

    @Test
    void cambiarEstadoActualizaElEstado() {
        Plato plato = new Plato("Locro", 2.50, ItemMenu.DISPONIBLE, "Sopa de papa");
        plato.cambiarEstado("Inactivo");
        assertEquals("Inactivo", plato.getEstado());
    }

    @Test
    void crearMenuDiarioCreaBorradorConFechaDeHoy() {
        Menu menu = personal.crearMenuDiario();
        assertEquals(Menu.BORRADOR, menu.getEstado());
        assertEquals(LocalDate.now(), menu.getFecha());
        assertSame(personal, menu.getPersonal());
    }
}