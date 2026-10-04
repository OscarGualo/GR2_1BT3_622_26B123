package com.javaweb.gr2s2_1bt3_622_26b.modelo;

import jakarta.persistence.Entity;

/** Trazabilidad: Diagrama de clases Fig. 7 - clase Plato, "es un" ItemMenu. */
@Entity
public class Plato extends ItemMenu {

    protected Plato() { }

    public Plato(String nombre, double precio, String estado, String descripcion) {
        super(nombre, precio, estado, descripcion);
    }

    /** Fig. 7 - atributo -id y método +getId(): con herencia JOINED, Plato comparte el id de ItemMenu. */
    @Override
    public Integer getId() {
        return super.getId();
    }
}