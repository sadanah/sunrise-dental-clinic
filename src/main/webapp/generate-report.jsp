<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Generate Report</title>
    <link rel="stylesheet" href="./css/style.css">
</head>
<body>
<%@ include file="./WEB-INF/jspf/header.jspf" %>

<div>
    <h1>Generate Report</h1>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>

    <form action="<%= ctx %>/admin/reports" method="post">
        <label>Report Type</label>
        <select name="type" required>
            <option value="REVENUE">Revenue Report</option>
            <option value="DAILY_APPOINTMENTS">Daily Appointment Report</option>
            <option value="DENTIST_SCHEDULE">Dentist Schedule Report</option>
        </select>

        <label>Start Date</label>
        <input type="date" name="startDate">

        <label>End Date</label>
        <input type="date" name="endDate">

        <label>Dentist ID (for Dentist Schedule Report)</label>
        <input type="text" name="dentistID">

        <button type="submit">Generate</button>
    </form>

    <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
</div>

</body>
</html>