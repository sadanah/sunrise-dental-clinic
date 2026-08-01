<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:30 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Appointment" %>
<%@ page import="com.sunrisedentalclinic.domain.AppointmentStatus" %>
<% request.setAttribute("currentPage", "appointments"); %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Cancel Appointment</title>
</head>

<body>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Cancel Appointment</h1>

            <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error"><%= request.getAttribute("errorMessage") %></p>
            <% } %>

            <% if (request.getAttribute("successMessage") != null) { %>
            <p class="success"><%= request.getAttribute("successMessage") %></p>
            <% } %>

            <form action="<%= ctx %>/appointments/cancel" method="get">
                <label>Appointment No</label>
                <input type="text" name="appointmentNo" required
                       value="<%= request.getParameter("appointmentNo") != null ? request.getParameter("appointmentNo") : "" %>">
                <button type="submit">Search</button>
            </form>

            <%
                Appointment appointment = (Appointment) request.getAttribute("appointment");
                if (appointment != null) {
            %>
            <h2>Appointment Details</h2>
            <table>
                <tr><th>Appointment No</th><td><%= appointment.getAppointmentNo() %></td></tr>
                <tr><th>Patient</th><td><%= appointment.getPatientID() %></td></tr>
                <tr><th>Dentist</th><td><%= appointment.getDentistID() %></td></tr>
                <tr><th>Date</th><td><%= appointment.getAppointmentDate() %></td></tr>
                <tr><th>Time</th><td><%= appointment.getAppointmentTime() %></td></tr>
                <tr><th>Status</th><td><%= appointment.getStatus() %></td></tr>
            </table>

            <%
                if (appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            %>
            <form id="cancelForm" action="<%= ctx %>/appointments/cancel" method="post">
                <input type="hidden" name="appointmentNo" value="<%= appointment.getAppointmentNo() %>">
                <button type="button" class="danger"
                        onclick="openConfirmModal('Cancel appointment <%= appointment.getAppointmentNo() %>?', document.getElementById('cancelForm'))">
                    Cancel This Appointment
                </button>
            </form>
            <%
            } else {
            %>
            <p class="subtitle">This appointment is already <%= appointment.getStatus() %> and cannot be cancelled.</p>
            <%
                }
            %>
            <% } %>

            <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/confirm-modal.jspf" %>

</body>
</html>