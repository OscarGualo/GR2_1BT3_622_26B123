package com.javaweb.gr2s2_1bt3_622_26b.persistencia;

import com.javaweb.gr2s2_1bt3_622_26b.modelo.PersonalComedor;

public class PersonalComedorDAO extends GenericDAO<PersonalComedor> {
    public PersonalComedorDAO() { super(PersonalComedor.class); }

    public PersonalComedor buscarPorCedula(String cedula) {
        return consultar(em -> em.createQuery(
                        "SELECT p FROM PersonalComedor p WHERE p.cedula = :cedula", PersonalComedor.class)
                .setParameter("cedula", cedula)
                .getResultStream().findFirst().orElse(null));
    }
}