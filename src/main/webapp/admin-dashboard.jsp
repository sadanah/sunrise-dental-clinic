<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 2:48 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div class="container">
    <h1>Admin Dashboard</h1>
    <ul class="dashboard-links">
        <li><a href="<%= ctx %>/admin/staff">Manage Staff Accounts</a></li>
        <li><a href="<%= ctx %>/admin/treatments">Manage Treatment Types</a></li>
        <li><a href="<%= ctx %>/admin/reports">Generate Reports</a></li>
    </ul>
</div>
</body>
</html>