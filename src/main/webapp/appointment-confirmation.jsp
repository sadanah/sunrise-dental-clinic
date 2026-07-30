<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Appointment" %>

<!DOCTYPE html>
<html>
<head>
    <title>Appointment Confirmed</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div>
    <h1>Appointment Registered Successfully</h1>

    <%
        Appointment appt = (Appointment) request.getAttribute("appointment");
        if (appt != null) {
    %>

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