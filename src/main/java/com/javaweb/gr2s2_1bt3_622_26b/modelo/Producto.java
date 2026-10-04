package com.javaweb.gr2s2_1bt3_622_26b.modelo;

import jakarta.persistence.Entity;
import java.time.LocalDate;

/** Trazabilidad: Diagrama de clases Fig. 7 - clase Producto, "es un" ItemMenu. */
@Entity
public class Producto extends ItemMenu {

    private int stock;
    private LocalDate fechaCaducidad;

    protected Producto() { }

    public Producto(String nombre, double precio, String estado, String descripcion,
                    int stock, LocalDate fechaCaducidad) {
        super(nombre, precio, estado, descripcion);
        this.stock = stock;
        this.fechaCaducidad = fechaCaducidad;
    }

    /** Fig. 7 - método +getStock(). */
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}