<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.TreatmentType, java.util.List" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head><title>Manage Treatment Types</title></head>
<body>
<h1>Manage Treatment Types</h1>

<% if (request.getAttribute("errorMessage") != null) { %>
<p class="error"><%= request.getAttribute("errorMessage") %></p>
<% } %>
<% if (request.getAttribute("successMessage") != null) { %>
<p class="success"><%= request.getAttribute("successMessage") %></p>
<% } %>

<table border="1" cellpadding="6">
    <tr><th>ID</th><th>Name</th><th>Base Cost</th><th></th></tr>
    <%
        List<TreatmentType> treatments = (List<TreatmentType>) request.getAttribute("treatments");
        for (TreatmentType t : treatments) {
    %>
    <tr>
        <td><%= t.getTreatmentID() %></td>
        <td><%= t.getTreatmentName() %></td>
        <td><%= t.getBaseCost() %></td>
        <td>
            <form action="<%= ctx %>/admin/treatments" method="post" style="display:inline">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="treatmentID" value="<%= t.getTreatmentID() %>">
                <button type="submit" onclick="return confirm('Delete this treatment type?')">Delete</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>

<h2>Add New Treatment Type</h2>
<form action="<%= ctx %>/admin/treatments" method="post">
    <input type="hidden" name="action" value="create">
    <label>Treatment ID</label>
    <input type="text" name="treatmentID" required>
    <label>Name</label>
    <input type="text" name="treatmentName" required>
    <label>Base Cost</label>
    <input type="number" name="baseCost" step="0.01" required>
    <button type="submit">Add Treatment</button>
</form>
<a href="<%= ctx %>/admin-dashboard.jsp">Back to Dashboard</a>
</body>
</html>
