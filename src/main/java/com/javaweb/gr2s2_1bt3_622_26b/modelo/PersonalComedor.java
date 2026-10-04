package com.javaweb.gr2s2_1bt3_622_26b.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Trazabilidad: Diagrama de clases Fig. 7 - clase PersonalComedor. Casos de uso Fig. 1 - actor. */
@Entity
public class PersonalComedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;               // técnico

    private String nombres;
    private String apellidos;
    private String rol;

    @Column(unique = true, nullable = false)
    private String cedula;

    private String clave;             // técnico: inicio de sesión ("se identifica exitosamente")

    /** Fig. 7 - relación "administra" (1 a *) con Menu. */
    @OneToMany(mappedBy = "personal")
    private List<Menu> menus = new ArrayList<>();

    public PersonalComedor() { }

    public PersonalComedor(String nombres, String apellidos, String rol, String cedula, String clave) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.rol = rol;
        this.cedula = cedula;
        this.clave = clave;
    }

    /** Fig. 7 - +crearMenuDiario(). Fig. 8 - mensajes 2 y 3: crea el Menu con fecha de hoy y estado "Borrador". */
    public Menu crearMenuDiario() {
        Menu menu = new Menu(LocalDate.now(), Menu.BORRADOR);
        menu.setPersonal(this);
        menus.add(menu);
        return menu;
    }

    /** Fig. 7 - +eliminarPlato(). CU 02: retira el plato cambiando su estado, no lo borra. */
    public void eliminarPlato(Plato plato) {
        plato.cambiarEstado(ItemMenu.AGOTADO);
    }

    /** Fig. 7 - +eliminarProducto(). CU 02. */
    public void eliminarProducto(Producto producto) {
        producto.cambiarEstado(ItemMenu.AGOTADO);
    }

    public Integer getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getRol() { return rol; }
    public String getCedula() { return cedula; }
    public String getClave() { return clave; }
}