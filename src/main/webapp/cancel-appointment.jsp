<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:30 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cancel Appointment</title>
</head>

<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div>
    <h1>Cancel Appointment</h1>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>

    <% if (request.getAttribute("successMessage") != null) { %>
    <p class="success"><%= request.getAttribute("successMessage") %></p>
    <% } %>

    <form action="<%= ctx %>/appointments/cancel" method="post">
        <label>Appointment No</label>
        <input type="text" name="appointmentNo" required>
        <button type="submit">Cancel Appointment</button>
    </form>

    <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
</div>

</body>
</html>