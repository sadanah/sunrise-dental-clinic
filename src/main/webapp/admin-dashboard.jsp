<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 2:48 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- ADDED: sets which sidebar item gets the active pill; header.jspf reads this --%>
<% request.setAttribute("currentPage", "dashboard"); %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
<%-- CHANGED: wrap header + page content in app-shell so sidebar and main sit side by side --%>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <%-- CHANGED: app-main replaces bare body flow; container now lives inside it --%>
    <div class="app-main">
        <div class="container">
            <h1>Admin Dashboard</h1>
            <ul class="dashboard-links">
                <li><a href="<%= ctx %>/admin/staff">Manage Staff Accounts</a></li>
                <li><a href="<%= ctx %>/admin/treatments">Manage Treatment Types</a></li>
                <li><a href="<%= ctx %>/admin/reports">Generate Reports</a></li>
            </ul>
        </div>
    </div>
    <%-- ADDED: closes app-shell opened above --%>
</div>
</body>
</html>