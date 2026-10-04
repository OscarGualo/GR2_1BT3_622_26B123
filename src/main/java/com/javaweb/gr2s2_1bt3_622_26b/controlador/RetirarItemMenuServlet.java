package com.javaweb.gr2s2_1bt3_622_26b.controlador;

import com.javaweb.gr2s2_1bt3_622_26b.modelo.*;
import com.javaweb.gr2s2_1bt3_622_26b.persistencia.ItemMenuDAO;
import com.javaweb.gr2s2_1bt3_622_26b.persistencia.MenuDAO;
import com.javaweb.gr2s2_1bt3_622_26b.persistencia.PersonalComedorDAO;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Trazabilidad: Diagrama de robustez Fig. 5 - controlador "Retirar plato y/ producto del menú".
 * Diagrama de secuencia CU 02 (figura nueva) - participante Controlador.
 */
@WebServlet("/personal/retirar")
public class RetirarItemMenuServlet extends HttpServlet {

    private final MenuDAO menuDAO = new MenuDAO();
    private final ItemMenuDAO itemDAO = new ItemMenuDAO();
    private final PersonalComedorDAO personalDAO = new PersonalComedorDAO();

    /** Secuencia CU 02 - mensajes 1 a 4: listar ítems disponibles del menú del día. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Menu menuHoy = menuDAO.buscarMenuDelDia();
        List<ItemMenu> items = (menuHoy == null)
                ? Collections.emptyList()
                : itemDAO.listarPorMenuYEstado(menuHoy, ItemMenu.DISPONIBLE);

        req.setAttribute("menuHoy", menuHoy);
        req.setAttribute("items", items);
        req.getRequestDispatcher("/vistas/personal/retirarItem.jsp").forward(req, resp);
    }

    /** Secuencia CU 02 - mensajes 5 a 10: retirar el ítem seleccionado. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String mensaje;

        try {
            int idItem = Integer.parseInt(req.getParameter("idItem"));
            ItemMenu item = itemDAO.buscarPorId(idItem);

            if (item == null || !ItemMenu.DISPONIBLE.equals(item.getEstado())) {
                mensaje = "El ítem no existe o ya fue retirado.";
            } else {
                PersonalComedor personal = obtenerPersonal(req);
                if (item instanceof Plato) {
                    personal.eliminarPlato((Plato) item);          // mensaje 6
                } else if (item instanceof Producto) {
                    personal.eliminarProducto((Producto) item);    // mensaje 7
                }
                itemDAO.actualizar(item);                          // persiste cambiarEstado (mensajes 8 y 9)
                mensaje = "Ítem retirado del menú: " + item.getNombre();
            }
        } catch (NumberFormatException e) {
            mensaje = "Seleccione un ítem válido.";
        } catch (PersistenceException e) {
            mensaje = esConflicto(e)
                    ? "El ítem está siendo comprado en este momento. Intente de nuevo."
                    : "No se pudo retirar el ítem: " + e.getMessage();
        }

        req.getSession().setAttribute("mensaje", mensaje);
        resp.sendRedirect(req.getContextPath() + "/personal/retirar");
    }

    /** Personal en sesión (lo guarda el login de la Persona 2). */
    private PersonalComedor obtenerPersonal(HttpServletRequest req) {
        PersonalComedor personal = (PersonalComedor) req.getSession().getAttribute("personal");
        if (personal == null) {
            // TEMPORAL: hasta que la Persona 2 suba el login, se usa el usuario de datos_iniciales.sql
            personal = personalDAO.buscarPorCedula("1700000001");
        }
        return personal;
    }

    /** Condición del caso de prueba: no retirar si otra transacción modifica el ítem a la vez. */
    private boolean esConflicto(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof OptimisticLockException) {
                return true;
            }
        }
        return false;
    }
}