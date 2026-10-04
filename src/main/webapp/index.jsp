<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.javaweb.gr2s2_1bt3_622_26b.PedidoServlet.MenuItem" %>
<%@ page import="com.javaweb.gr2s2_1bt3_622_26b.PedidoServlet.Order" %>
<%
    List<MenuItem> dishes = (List<MenuItem>) request.getAttribute("dishes");
    List<MenuItem> extras = (List<MenuItem>) request.getAttribute("extras");
    Order createdOrder = (Order) request.getAttribute("createdOrder");
    Order staffOrder = (Order) request.getAttribute("staffOrder");
    String section = request.getParameter("seccion");
    if (request.getAttribute("section") != null) {
        section = (String) request.getAttribute("section");
    }
    boolean staffSection = "personal".equals(section);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Buen Provecho | Comedor universitario</title>
    <style>
        :root {
            color-scheme: light;
            --ink: #18342c;
            --muted: #687a72;
            --green: #176b4b;
            --green-dark: #105239;
            --lime: #e8f3df;
            --cream: #faf9f5;
            --line: #e6e9e2;
            --white: #fff;
            --orange: #ef9c54;
            font-family: Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
        }
        * { box-sizing: border-box; }
        body { margin: 0; color: var(--ink); background: var(--cream); }
        a { color: inherit; text-decoration: none; }
        .topbar { height: 76px; display: flex; align-items: center; justify-content: space-between; padding: 0 max(5vw, 24px); background: var(--white); border-bottom: 1px solid var(--line); }
        .brand { display: flex; align-items: center; gap: 11px; font-size: 20px; font-weight: 800; letter-spacing: -.7px; }
        .brand-mark { display: grid; place-items: center; width: 39px; height: 39px; color: white; background: var(--green); border-radius: 13px; font-size: 21px; }
        .nav { display: flex; align-items: center; gap: 8px; padding: 5px; background: #f5f6f2; border-radius: 13px; }
        .nav a { padding: 10px 16px; border-radius: 9px; color: var(--muted); font-size: 13px; font-weight: 700; }
        .nav a.active { background: var(--white); color: var(--green); box-shadow: 0 2px 8px #17392d12; }
        .campus { color: var(--muted); font-size: 12px; font-weight: 600; }
        main { width: min(1120px, 90vw); margin: 0 auto; padding: 44px 0 72px; }
        .hero { position: relative; overflow: hidden; display: flex; align-items: center; justify-content: space-between; min-height: 250px; padding: 42px 56px; color: white; background: var(--green); border-radius: 24px; }
        .hero:after { content: ""; position: absolute; width: 310px; height: 310px; right: 9%; top: -148px; border: 1px solid #ffffff20; border-radius: 50%; box-shadow: 0 0 0 35px #ffffff09, 0 0 0 75px #ffffff08; }
        .hero-copy { z-index: 1; max-width: 610px; }
        .eyebrow { margin: 0 0 12px; color: #d5e9d5; font-size: 11px; font-weight: 800; letter-spacing: 1.7px; text-transform: uppercase; }
        h1 { margin: 0; font-size: clamp(30px, 4vw, 44px); line-height: 1.12; letter-spacing: -1.6px; }
        .hero p { margin: 13px 0 0; color: #e0eee2; font-size: 15px; line-height: 1.6; }
        .hero-plate { z-index: 1; display: grid; place-items: center; width: 145px; height: 145px; margin-right: 30px; background: #ffffff17; border: 1px solid #ffffff25; border-radius: 50%; font-size: 76px; }
        .section-heading { display: flex; justify-content: space-between; align-items: end; margin: 42px 0 18px; }
        h2 { margin: 0; font-size: 22px; letter-spacing: -.55px; }
        .section-heading p { margin: 6px 0 0; color: var(--muted); font-size: 13px; }
        .menu-layout { display: grid; grid-template-columns: minmax(0, 1fr) 325px; gap: 25px; align-items: start; }
        .cards { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 15px; }
        .dish-card, .panel { background: var(--white); border: 1px solid var(--line); border-radius: 17px; }
        .dish-card { position: relative; min-height: 217px; padding: 20px; cursor: pointer; transition: border-color .18s, transform .18s, box-shadow .18s; }
        .dish-card:hover { transform: translateY(-2px); border-color: #a9c8af; box-shadow: 0 10px 25px #18342c0c; }
        .dish-card:has(input:checked) { border: 2px solid var(--green); padding: 19px; background: #fbfef9; }
        .dish-card input { position: absolute; top: 18px; right: 18px; accent-color: var(--green); width: 18px; height: 18px; }
        .food-art { display: grid; place-items: center; width: 62px; height: 62px; margin-bottom: 15px; background: var(--lime); border-radius: 17px; font-size: 34px; }
        .dish-card h3 { margin: 0 0 6px; font-size: 16px; }
        .dish-card p { min-height: 38px; margin: 0; color: var(--muted); font-size: 12px; line-height: 1.55; }
        .price { display: block; margin-top: 14px; color: var(--green); font-size: 15px; font-weight: 800; }
        .panel { padding: 22px; }
        .panel h3 { margin: 0; font-size: 17px; }
        .panel-note { margin: 6px 0 18px; color: var(--muted); font-size: 12px; line-height: 1.5; }
        .extra-row { display: flex; align-items: center; gap: 11px; padding: 13px 0; border-top: 1px solid #eff0ec; cursor: pointer; }
        .extra-row input { accent-color: var(--green); width: 16px; height: 16px; }
        .extra-icon { display: grid; place-items: center; width: 38px; height: 38px; background: #f7f3e9; border-radius: 11px; font-size: 20px; }
        .extra-copy { flex: 1; }
        .extra-copy strong { display: block; font-size: 12px; }
        .extra-copy small { display: block; margin-top: 3px; color: var(--muted); font-size: 11px; }
        .extra-price { color: var(--green); font-size: 12px; font-weight: 800; }
        .submit-button, .action-button { display: block; width: 100%; margin-top: 17px; padding: 14px 17px; color: white; background: var(--green); border: 0; border-radius: 11px; font: inherit; font-size: 13px; font-weight: 800; cursor: pointer; transition: background .18s; }
        .submit-button:hover, .action-button:hover { background: var(--green-dark); }
        .hint { margin: 11px 0 0; color: var(--muted); text-align: center; font-size: 11px; }
        .notice { margin: 18px 0 0; padding: 14px 16px; background: #fff6e9; border: 1px solid #f3dfbf; border-radius: 12px; color: #79512e; font-size: 13px; }
        .success-card { display: flex; align-items: center; justify-content: space-between; gap: 20px; margin-top: 24px; padding: 21px 24px; background: #edf6e8; border: 1px solid #d6e7ce; border-radius: 17px; }
        .success-copy { display: flex; align-items: center; gap: 15px; }
        .success-icon { display: grid; place-items: center; flex: 0 0 42px; height: 42px; color: var(--green); background: white; border-radius: 50%; font-size: 21px; }
        .success-card h3 { margin: 0; font-size: 15px; }
        .success-card p { margin: 5px 0 0; color: var(--muted); font-size: 12px; }
        .pickup-code { padding: 9px 15px; color: var(--green-dark); background: white; border-radius: 10px; font: 800 22px ui-monospace, SFMono-Regular, Menlo, monospace; letter-spacing: 3px; }
        .staff-wrap { max-width: 760px; margin: 45px auto 0; }
        .staff-panel { padding: 30px; }
        .staff-panel h2 { font-size: 24px; }
        .staff-form { display: flex; gap: 10px; margin-top: 22px; }
        .staff-form input { flex: 1; min-width: 0; padding: 14px 15px; border: 1px solid var(--line); border-radius: 10px; font: inherit; font-size: 14px; letter-spacing: 2px; outline-color: var(--green); }
        .staff-form .submit-button { width: auto; margin: 0; padding-inline: 22px; }
        .order-detail { margin-top: 22px; padding: 20px; background: #f8faf6; border: 1px solid var(--line); border-radius: 13px; }
        .order-detail-head { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 16px; }
        .order-detail-head strong { font-size: 14px; }
        .status { padding: 6px 10px; color: #815b27; background: #fff2d9; border-radius: 99px; font-size: 10px; font-weight: 800; text-transform: uppercase; }
        .status.delivered { color: var(--green); background: #e3f1de; }
        .order-line { display: flex; justify-content: space-between; gap: 15px; padding: 10px 0; border-top: 1px solid var(--line); font-size: 13px; }
        .order-line span:last-child { font-weight: 700; white-space: nowrap; }
        .total-line { margin-top: 3px; font-size: 15px; font-weight: 800; }
        .delivery-message { margin-top: 16px; padding: 12px 14px; color: var(--green); background: #edf6e8; border-radius: 10px; font-size: 12px; font-weight: 700; }
        footer { padding: 22px; color: #8a968e; text-align: center; font-size: 11px; }
        @media (max-width: 800px) {
            .menu-layout { grid-template-columns: 1fr; }
            .hero { padding: 35px; }
            .hero-plate { margin-right: 0; }
            .campus { display: none; }
        }
        @media (max-width: 560px) {
            .topbar { height: 67px; padding: 0 18px; }
            .brand { font-size: 17px; }
            .brand-mark { width: 34px; height: 34px; }
            .nav a { padding: 9px 10px; font-size: 11px; }
            main { width: min(100% - 32px, 500px); padding-top: 23px; }
            .hero { min-height: 210px; padding: 28px 23px; }
            .hero-plate { width: 72px; height: 72px; margin-left: 10px; font-size: 39px; }
            .hero p { font-size: 13px; }
            .cards { grid-template-columns: 1fr; }
            .dish-card { min-height: 193px; }
            .success-card { align-items: flex-start; flex-direction: column; }
            .staff-panel { padding: 22px; }
            .staff-form { flex-direction: column; }
            .staff-form .submit-button { width: 100%; }
        }
    </style>
</head>
<body>
<header class="topbar">
    <a class="brand" href="<%= request.getContextPath() %>/pedidos">
        <span class="brand-mark">✳</span><span>Buen Provecho</span>
    </a>
    <nav class="nav" aria-label="Secciones">
        <a class="<%= staffSection ? "" : "active" %>" href="<%= request.getContextPath() %>/pedidos">Pedir comida</a>
        <a class="<%= staffSection ? "active" : "" %>" href="<%= request.getContextPath() %>/pedidos?seccion=personal">Personal del comedor</a>
    </nav>
    <span class="campus">Comedor universitario</span>
</header>
<main>
<% if (staffSection) { %>
    <section class="hero">
        <div class="hero-copy">
            <p class="eyebrow">Entrega de pedidos</p>
            <h1>Un código, una entrega rápida.</h1>
            <p>Busca el código del estudiante para consultar su pedido y confirmar la entrega.</p>
        </div>
        <div class="hero-plate" aria-hidden="true">🛎️</div>
    </section>
    <section class="staff-wrap">
        <div class="panel staff-panel">
            <h2>Consultar pedido</h2>
            <p class="panel-note">Ingresa el código de 6 dígitos que muestra el estudiante al retirar su comida.</p>
            <form class="staff-form" method="post" action="<%= request.getContextPath() %>/pedidos">
                <input type="hidden" name="action" value="buscar">
                <input type="text" name="codigoPersonal" placeholder="Ej. 482105" maxlength="6" inputmode="numeric" pattern="[0-9]{6}" aria-label="Código de retiro" required>
                <button class="submit-button" type="submit">Buscar pedido</button>
            </form>
            <% if (request.getAttribute("staffError") != null) { %>
                <div class="notice" role="alert"><%= request.getAttribute("staffError") %></div>
            <% } %>
            <% if (staffOrder != null) { %>
                <div class="order-detail">
                    <div class="order-detail-head">
                        <strong>Pedido #<%= staffOrder.code %></strong>
                        <span class="status <%= staffOrder.isDelivered() ? "delivered" : "" %>"><%= staffOrder.isDelivered() ? "Entregado" : "Pendiente" %></span>
                    </div>
                    <div class="order-line"><span><%= staffOrder.dish.name %></span><span>$<%= String.format(java.util.Locale.US, "%.2f", staffOrder.dish.price) %></span></div>
                    <% for (MenuItem extra : staffOrder.extras) { %>
                        <div class="order-line"><span><%= extra.name %></span><span>$<%= String.format(java.util.Locale.US, "%.2f", extra.price) %></span></div>
                    <% } %>
                    <div class="order-line total-line"><span>Total</span><span>$<%= String.format(java.util.Locale.US, "%.2f", staffOrder.total) %></span></div>
                    <% if (!staffOrder.isDelivered()) { %>
                        <form method="post" action="<%= request.getContextPath() %>/pedidos">
                            <input type="hidden" name="action" value="entregar">
                            <input type="hidden" name="codigo" value="<%= staffOrder.code %>">
                            <button class="action-button" type="submit">Confirmar entrega</button>
                        </form>
                    <% } %>
                </div>
            <% } %>
            <% if (request.getAttribute("deliveryMessage") != null) { %>
                <div class="delivery-message"><%= request.getAttribute("deliveryMessage") %></div>
            <% } %>
        </div>
    </section>
<% } else { %>
    <section class="hero">
        <div class="hero-copy">
            <p class="eyebrow">Comida rica, sin filas</p>
            <h1>Tu almuerzo universitario, listo cuando tú llegues.</h1>
            <p>Elige tu plato, agrega algo extra y recibe un código para retirar tu pedido.</p>
        </div>
        <div class="hero-plate" aria-hidden="true">🍲</div>
    </section>

    <% if (createdOrder != null) { %>
        <section class="success-card" aria-live="polite">
            <div class="success-copy">
                <span class="success-icon">✓</span>
                <div>
                    <h3>¡Pedido confirmado!</h3>
                    <p>Preséntale este código al personal del comedor para retirar tu comida.</p>
                </div>
            </div>
            <span class="pickup-code"><%= createdOrder.code %></span>
        </section>
    <% } %>
    <% if (request.getAttribute("studentError") != null) { %>
        <div class="notice" role="alert"><%= request.getAttribute("studentError") %></div>
    <% } %>

    <div class="section-heading">
        <div><h2>El menú de hoy</h2><p>Escoge un plato principal para comenzar.</p></div>
    </div>
    <form method="post" action="<%= request.getContextPath() %>/pedidos">
        <input type="hidden" name="action" value="crear">
        <div class="menu-layout">
            <section class="cards" aria-label="Platos principales">
                <% for (MenuItem dish : dishes) { %>
                    <label class="dish-card">
                        <input type="radio" name="plato" value="<%= dish.id %>" required>
                        <span class="food-art"><%= dish.emoji %></span>
                        <h3><%= dish.name %></h3>
                        <p><%= dish.description %></p>
                        <span class="price">$<%= String.format(java.util.Locale.US, "%.2f", dish.price) %></span>
                    </label>
                <% } %>
            </section>
            <aside class="panel">
                <h3>Hazlo más tuyo</h3>
                <p class="panel-note">Agrega extras a tu pedido (opcional).</p>
                <% for (MenuItem extra : extras) { %>
                    <label class="extra-row">
                        <input type="checkbox" name="extra" value="<%= extra.id %>">
                        <span class="extra-icon"><%= extra.emoji %></span>
                        <span class="extra-copy"><strong><%= extra.name %></strong><small><%= extra.description %></small></span>
                        <span class="extra-price">+$<%= String.format(java.util.Locale.US, "%.2f", extra.price) %></span>
                    </label>
                <% } %>
                <button class="submit-button" type="submit">Confirmar pedido</button>
                <p class="hint">El pago se realiza en el comedor al retirar.</p>
            </aside>
        </div>
    </form>
<% } %>
</main>
<footer>Buen Provecho · Comedor universitario</footer>
</body>
</html>
