<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- Trazabilidad: Diagrama de robustez Fig. 5 - frontera "Interfaz del sistema de comida" (CU 02) --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Retirar plato o producto</title>
</head>
<body>
<h1>Retirar plato y/o producto del menú</h1>

<c:if test="${not empty sessionScope.mensaje}">
    <p><strong>${sessionScope.mensaje}</strong></p>
    <c:remove var="mensaje" scope="session"/>
</c:if>

<c:choose>
    <c:when test="${empty menuHoy}">
        <p>Todavía no hay menú creado para hoy.</p>
    </c:when>
    <c:when test="${empty items}">
        <p>No quedan ítems disponibles en el menú de hoy.</p>
    </c:when>
    <c:otherwise>
        <table border="1" cellpadding="6">
            <tr><th>Nombre</th><th>Descripción</th><th>Precio</th><th>Estado</th><th>Acción</th></tr>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.nombre}</td>
                    <td>${item.descripcion}</td>
                    <td>$<fmt:formatNumber value="${item.precio}" minFractionDigits="2" maxFractionDigits="2"/></td>
                    <td>${item.estado}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/personal/retirar"
                              onsubmit="return confirm('¿Retirar ${item.nombre} del menú?');">
                            <input type="hidden" name="idItem" value="${item.id}">
                            <button type="submit">Retirar</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<p><a href="${pageContext.request.contextPath}/">Volver al inicio</a></p>
</body>
</html>