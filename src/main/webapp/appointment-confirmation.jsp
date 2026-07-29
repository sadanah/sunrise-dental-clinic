<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Appointment" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head><title>Appointment Confirmed</title></head>
<body>
<h1>Appointment Registered Successfully</h1>
<%
    Appointment appt = (Appointment) request.getAttribute("appointment");
%>
<p>Appointment No: <%= appt.getAppointmentNo() %></p>
<p>Date: <%= appt.getAppointmentDate() %></p>
<p>Time: <%= appt.getAppointmentTime() %></p>
<p>Status: <%= appt.getStatus() %></p>
<a href="<%= ctx %>/receptionist-dashboard.jsp">Back to Dashboard</a>
</body>
</html>