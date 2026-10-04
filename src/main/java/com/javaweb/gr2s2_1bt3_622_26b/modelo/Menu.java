package com.javaweb.gr2s2_1bt3_622_26b.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Trazabilidad: Diagrama de clases Fig. 7 - clase Menu. Diagrama de robustez Fig. 5 - entidad Menú. */
@Entity
public class Menu {

    public static final String BORRADOR = "Borrador";
    public static final String PUBLICADO = "Publicado";
    public static final String CERRADO = "Cerrado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;               // técnico

    private LocalDate fecha;
    private String estado;

    /** Fig. 7 - relación "contiene" (1 a *) con ItemMenu. */
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<ItemMenu> items = new ArrayList<>();

    /** Fig. 7 - relación "administra": muchos menús, un PersonalComedor. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id")
    private PersonalComedor personal;

    protected Menu() { }

    public Menu(LocalDate fecha, String estado) {
        this.fecha = fecha;
        this.estado = estado;
    }

    /** Fig. 7 - +publicarParaVenta(). Fig. 8 - mensaje 10. */
    public void publicarParaVenta() {
        this.estado = PUBLICADO;
    }

    /** Fig. 7 - +cerrarParaVenta(). */
    public void cerrarParaVenta() {
        this.estado = CERRADO;
    }

    /** Fig. 7 - +agregarItem(). Fig. 8 - mensaje 8 agregarItem(nuevoPlato). */
    public void agregarItem(ItemMenu item) {
        items.add(item);
        item.setMenu(this);
    }

    /** Fig. 7 - +obtenerItem(). */
    public List<ItemMenu> obtenerItem() {
        return items;
    }

    public Integer getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public PersonalComedor getPersonal() { return personal; }
    public void setPersonal(PersonalComedor personal) { this.personal = personal; }
}