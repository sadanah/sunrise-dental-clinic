<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 11:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head><title>Receptionist Dashboard</title></head>
<body>
<h1>Receptionist Dashboard</h1>
<ul>
    <li><a href="<%= ctx %>/appointments/register">Register New Appointment</a></li>
    <li><a href="<%= ctx %>/appointments/cancel">Cancel Appointment</a></li>
    <li><a href="<%= ctx %>/appointments/search">Search / Display Appointment</a></li>
    <li><a href="<%= ctx %>/billing">Generate Bill</a></li>
    <li><a href="<%= ctx %>/help">Help</a></li>
</ul>
<a href="<%= ctx %>/logout">Logout</a>
</body>
</html>