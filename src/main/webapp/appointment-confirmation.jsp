<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Appointment" %>
<% request.setAttribute("currentPage", "appointments"); %>
<!DOCTYPE html>
<html>
<head>
    <title>Appointment Confirmed</title>
</head>
<body>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div class="app-main">
        <div class="container">
            <h1>Appointment Registered Successfully</h1>
            <%
                Appointment appt = (Appointment) request.getAttribute("appointment");
                if (appt != null) {
            %>
            <p class="success">Appointment No: <%= appt.getAppointmentNo() %></p>
            <p>Date: <%= appt.getAppointmentDate() %></p>
            <p>Time: <%= appt.getAppointmentTime() %></p>
            <p>Status: <%= appt.getStatus() %></p>
            <%
                }
            %>
            <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>