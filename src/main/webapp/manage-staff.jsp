<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 2:49 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.*, java.util.List" %>
<% request.setAttribute("currentPage", "admin"); %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Manage Staff</title>
</head>
<body>

<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Manage Staff Accounts</h1>

            <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error"><%= request.getAttribute("errorMessage") %></p>
            <% } %>

            <% if (request.getAttribute("successMessage") != null) { %>
            <p class="success"><%= request.getAttribute("successMessage") %></p>
            <% } %>

            <h2>Existing Staff</h2>
            <table>
                <tr>
                    <th>Staff ID</th>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Username</th>
                    <th></th>
                </tr>

                <%
                    List<Staff> staffList = (List<Staff>) request.getAttribute("staffList");
                    for (Staff s : staffList) {
                %>
                <tr>
                    <td><%= s.getStaffID() %></td>
                    <td><%= s.getName() %></td>
                    <td><span class="badge"><%= s.getRole() %></span></td>
                    <td><%= s.getUsername() %></td>
                    <td>
                        <form action="<%= ctx %>/admin/staff" method="post" style="display:inline">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="staffID" value="<%= s.getStaffID() %>">
                            <button type="submit" class="danger"
                                    onclick="return confirm('Delete this staff member?')">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>

            <h2>Add New Staff</h2>

            <form action="<%= ctx %>/admin/staff" method="post">
                <input type="hidden" name="action" value="create">

                <label>Role</label>
                <select name="role" required>
                    <option value="RECEPTIONIST">Receptionist</option>
                    <option value="DENTIST">Dentist</option>
                    <option value="ADMIN">Admin</option>
                </select>

                <label>Staff ID</label>
                <input type="text" name="staffID" required>

                <label>Name</label>
                <input type="text" name="name" required>

                <label>Contact No</label>
                <input type="text" name="contactNo" required>

                <label>Address</label>
                <input type="text" name="address" required>

                <label>Username</label>
                <input type="text" name="username" required>

                <label>Password</label>
                <input type="password" name="password" required minlength="8">

                <label>Specialization (Dentist only)</label>
                <input type="text" name="specialization">

                <label>Consultation Fee (Dentist only)</label>
                <input type="number" name="consultationFee" step="0.01">

                <button type="submit">Add Staff</button>
            </form>

            <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>
