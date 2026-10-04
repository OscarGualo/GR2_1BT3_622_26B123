package com.javaweb.gr2s2_1bt3_622_26b;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "pedidoServlet", value = "/pedidos")
public class PedidoServlet extends HttpServlet {
    private static final String ORDERS_ATTRIBUTE = PedidoServlet.class.getName() + ".orders";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final List<MenuItem> DISHES = Collections.unmodifiableList(Arrays.asList(
            new MenuItem("almuerzo", "Almuerzo del día", "Pollo a la plancha, arroz, ensalada y jugo natural.", 4.50, "🍗"),
            new MenuItem("vegetariano", "Bowl vegetariano", "Quinua, vegetales asados, aguacate y aderezo de la casa.", 4.00, "🥗"),
            new MenuItem("pasta", "Pasta cremosa", "Pasta con salsa de champiñones, queso y hierbas frescas.", 4.25, "🍝")
    ));
    private static final List<MenuItem> EXTRAS = Collections.unmodifiableList(Arrays.asList(
            new MenuItem("jugo", "Jugo natural", "Naranja o mora.", 0.75, "🧃"),
            new MenuItem("postre", "Postre del día", "Una porción preparada en casa.", 1.00, "🍰"),
            new MenuItem("aguacate", "Porción de aguacate", "Aguacate fresco.", 0.60, "🥑")
    ));

    @Override
    public void init() {
        if (getServletContext().getAttribute(ORDERS_ATTRIBUTE) == null) {
            getServletContext().setAttribute(ORDERS_ATTRIBUTE, new ConcurrentHashMap<String, Order>());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("dishes", DISHES);
        request.setAttribute("extras", EXTRAS);

        String code = normalizeCode(request.getParameter("codigo"));
        if (!code.isEmpty()) {
            Order order = orders().get(code);
            if (order != null) {
                request.setAttribute("createdOrder", order);
            }
        }
        if ("1".equals(request.getParameter("entregado"))) {
            request.setAttribute("deliveryMessage", "Pedido marcado como entregado.");
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("crear".equals(action)) {
            createOrder(request, response);
        } else if ("buscar".equals(action)) {
            findOrder(request, response);
        } else if ("entregar".equals(action)) {
            deliverOrder(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida.");
        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MenuItem dish = findItem(DISHES, request.getParameter("plato"));
        if (dish == null) {
            showError(request, response, "Selecciona un plato válido.");
            return;
        }

        List<MenuItem> selectedExtras = new ArrayList<MenuItem>();
        String[] extraIds = request.getParameterValues("extra");
        if (extraIds != null) {
            for (String extraId : extraIds) {
                MenuItem extra = findItem(EXTRAS, extraId);
                if (extra == null) {
                    showError(request, response, "Uno de los extras seleccionados no es válido.");
                    return;
                }
                selectedExtras.add(extra);
            }
        }

        String code;
        Order order;
        do {
            code = String.format("%06d", RANDOM.nextInt(1000000));
            order = new Order(code, dish, selectedExtras);
        } while (orders().putIfAbsent(code, order) != null);

        response.sendRedirect(request.getContextPath() + "/pedidos?codigo=" + code);
    }

    private void findOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = normalizeCode(request.getParameter("codigoPersonal"));
        Order order = orders().get(code);
        request.setAttribute("section", "personal");
        if (order == null) {
            request.setAttribute("staffError", "No encontramos un pedido con ese código.");
        } else {
            request.setAttribute("staffOrder", order);
        }
        render(request, response);
    }

    private void deliverOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String code = normalizeCode(request.getParameter("codigo"));
        Order order = orders().get(code);
        if (order == null) {
            request.setAttribute("section", "personal");
            request.setAttribute("staffError", "No encontramos un pedido con ese código.");
            render(request, response);
            return;
        }
        synchronized (order) {
            if (order.isDelivered()) {
                request.setAttribute("section", "personal");
                request.setAttribute("staffError", "Este pedido ya fue entregado.");
                request.setAttribute("staffOrder", order);
                render(request, response);
                return;
            }
            order.markDelivered();
        }
        response.sendRedirect(request.getContextPath() + "/pedidos?seccion=personal&entregado=1");
    }

    private void showError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute("studentError", message);
        render(request, response);
    }

    private void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("dishes", DISHES);
        request.setAttribute("extras", EXTRAS);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Order> orders() {
        return (Map<String, Order>) getServletContext().getAttribute(ORDERS_ATTRIBUTE);
    }

    private static MenuItem findItem(List<MenuItem> items, String id) {
        if (id == null) {
            return null;
        }
        for (MenuItem item : items) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }

    private static String normalizeCode(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }

    public static final class MenuItem {
        public final String id;
        public final String name;
        public final String description;
        public final double price;
        public final String emoji;

        private MenuItem(String id, String name, String description, double price, String emoji) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.emoji = emoji;
        }
    }

    public static final class Order {
        public final String code;
        public final MenuItem dish;
        public final List<MenuItem> extras;
        public final double total;
        private volatile boolean delivered;

        private Order(String code, MenuItem dish, List<MenuItem> extras) {
            this.code = code;
            this.dish = dish;
            this.extras = Collections.unmodifiableList(new ArrayList<MenuItem>(extras));
            double amount = dish.price;
            for (MenuItem extra : extras) {
                amount += extra.price;
            }
            this.total = amount;
        }

        public boolean isDelivered() {
            return delivered;
        }

        private void markDelivered() {
            delivered = true;
        }
    }
}
