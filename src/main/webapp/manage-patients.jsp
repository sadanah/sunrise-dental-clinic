<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 8/1/2026
  Time: 11:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Patient, java.util.List" %>
<% request.setAttribute("currentPage", "manage-patients"); %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Manage Patients</title>
</head>
<body>

<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Manage Patients</h1>

            <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error"><%= request.getAttribute("errorMessage") %></p>
            <% } %>

            <% if (request.getAttribute("successMessage") != null) { %>
            <p class="success"><%= request.getAttribute("successMessage") %></p>
            <% } %>

            <h2>Search Patient</h2>
            <form action="<%= ctx %>/patients" method="get">
                <label>Patient ID</label>
                <input type="text" name="patientID"
                       value="<%= request.getParameter("patientID") != null ? request.getParameter("patientID") : "" %>">
                <button type="submit">Search</button>
            </form>

            <%
                Patient found = (Patient) request.getAttribute("patient");
                if (found != null) {
            %>
            <table>
                <tr><th>Patient ID</th><td><%= found.getPatientID() %></td></tr>
                <tr><th>Name</th><td><%= found.getName() %></td></tr>
                <tr><th>Contact No</th><td><%= found.getContactNo() %></td></tr>
                <tr><th>Address</th><td><%= found.getAddress() %></td></tr>
                <tr><th>Registered</th><td><%= found.getRegisteredDate() %></td></tr>
                <tr>
                    <th></th>
                    <td>
                        <form id="deleteForm-<%= found.getPatientID() %>" action="<%= ctx %>/patients" method="post" style="display:inline">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="patientID" value="<%= found.getPatientID() %>">
                            <button type="button" class="danger"
                                    onclick="openConfirmModal('Delete patient <%= found.getPatientID() %>?', document.getElementById('deleteForm-<%= found.getPatientID() %>'))">
                                Delete
                            </button>
                        </form>
                    </td>
                </tr>
            </table>
            <% } %>

            <h2>All Patients</h2>
            <table>
                <tr>
                    <th>Patient ID</th>
                    <th>Name</th>
                    <th>Contact No</th>
                    <th>Registered</th>
                </tr>
                <%
                    List<Patient> patients = (List<Patient>) request.getAttribute("patients");
                    if (patients == null || patients.isEmpty()) {
                %>
                <tr><td class="table-empty" colspan="4">No patients registered yet.</td></tr>
                <%
                } else {
                    for (Patient p : patients) {
                %>
                <tr>
                    <td><%= p.getPatientID() %></td>
                    <td><%= p.getName() %></td>
                    <td><%= p.getContactNo() %></td>
                    <td><%= p.getRegisteredDate() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>

            <h2>Add New Patient</h2>
            <form action="<%= ctx %>/patients" method="post">
                <input type="hidden" name="action" value="create">

                <label>Patient ID</label>
                <input type="text" name="patientID" required>

                <label>Name</label>
                <input type="text" name="name" required>

                <label>Contact No</label>
                <input type="text" name="contactNo" required>

                <label>Address</label>
                <input type="text" name="address" required>

                <button type="submit">Add Patient</button>
            </form>

            <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/confirm-modal.jspf" %>

</body>
</html>
