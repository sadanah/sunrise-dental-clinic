<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Appointment" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search Appointment</title>
</head>
<body>
<%@ include file="./WEB-INF/jspf/header.jspf" %>

<div>
    <h1>Search Appointment</h1>

    <form action="<%= ctx %>/appointments/search" method="get">
        <label>Appointment No</label>
        <input type="text" name="appointmentNo" required>
        <button type="submit">Search</button>
    </form>

    <%
        if (request.getAttribute("errorMessage") != null) {
    %>
    <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <%
        }

        Appointment appt = (Appointment) request.getAttribute("appointment");
        if (appt != null) {
    %>
    <h2>Appointment Details</h2>
    <p>Appointment No: <%= appt.getAppointmentNo() %></p>
    <p>Date: <%= appt.getAppointmentDate() %></p>
    <p>Time: <%= appt.getAppointmentTime() %></p>
    <p>Status: <%= appt.getStatus() %></p>
    <%
        }
    %>

    <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>

</div>
</body>
</html>