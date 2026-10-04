package com.javaweb.gr2s2_1bt3_622_26b.modelo;

import jakarta.persistence.*;

/**
 * Trazabilidad: Diagrama de clases Fig. 7 - clase <<ItemMenu>> (superclase de Plato y Producto).
 * Diagrama de robustez Fig. 5 - entidades Plato y Producto.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ItemMenu {

    public static final String DISPONIBLE = "Disponible";
    public static final String AGOTADO = "Agotado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;               // técnico, no modelado

    @Version
    private Integer version;          // técnico: bloqueo optimista (condición del caso de prueba CU 02)

    private String nombre;
    private double precio;
    private String estado;
    private String descripcion;

    /** Fig. 7 - extremo "*" de la relación "contiene" con Menu. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected ItemMenu() { }

    protected ItemMenu(String nombre, double precio, String estado, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    /** Fig. 7 - método +cambiarEstado(). */
    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public Integer getId() { return id; }
    public Integer getVersion() { return version; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getEstado() { return estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }
}