<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head><title>Admin Dashboard</title></head>
<body>
<h1>Admin Dashboard</h1>
<ul>
    <li><a href="<%= ctx %>/admin/staff">Manage Staff Accounts</a></li>
    <li><a href="<%= ctx %>/admin/treatments">Manage Treatment Types</a></li>
    <li><a href="<%= ctx %>/admin/reports">Generate Reports</a></li>
    <li><a href="<%= ctx %>/help">Help</a></li>
</ul>
<a href="logout">Logout</a>
</body>
</html>