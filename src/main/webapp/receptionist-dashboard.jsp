<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 11:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Dashboard</title>
    <link rel="stylesheet" href="./css/style.css">
</head>
<body><%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 11:50 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <% request.setAttribute("currentPage", "dashboard"); %>
<!DOCTYPE html>
<html>
<head>
    <title>Receptionist Dashboard</title>
</head>
<body>

<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Receptionist Dashboard</h1>
            <p class="subtitle">Manage appointments and billing</p>

            <ul class="dashboard-links">
                <li><a href="<%= ctx %>/appointments/register">Register New Appointment</a></li>
                <li><a href="<%= ctx %>/appointments/cancel">Cancel Appointment</a></li>
                <li><a href="<%= ctx %>/appointments/search">Search / Display Appointment</a></li>
                <li><a href="<%= ctx %>/billing">Generate Bill</a></li>
            </ul>
        </div>
    </div>
</div>

</body>
</html>