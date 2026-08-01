<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/31/2026
  Time: 6:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%--
  Dentist Dashboard — shows the logged-in dentist's own upcoming appointments.
--%>
<%--
  Dentist Dashboard — shows the logged-in dentist's own upcoming appointments.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Appointment" %>
<%@ page import="java.util.List" %>
<%!
    // Maps status to a badge CSS class; unknown/future statuses fall back to plain .badge
    private String badgeClass(com.sunrisedentalclinic.domain.AppointmentStatus status) {
        switch (status) {
            case SCHEDULED: return "badge-scheduled";
            case COMPLETED: return "badge-completed";
            case CANCELLED: return "badge-cancelled";
            default: return "badge";
        }
    }
%>
<% request.setAttribute("currentPage", "dashboard"); %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Dentist Dashboard</title>
</head>
<body>

<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Dentist Dashboard</h1>
            <p class="subtitle">Your upcoming appointments</p>

            <%
                @SuppressWarnings("unchecked")
                List<Appointment> upcoming = (List<Appointment>) request.getAttribute("upcomingAppointments");
            %>

            <table class="data-table">
                <thead>
                <tr>
                    <th>Appointment No</th>
                    <th>Patient</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (upcoming == null || upcoming.isEmpty()) {
                %>
                <tr><td class="table-empty" colspan="5">No upcoming appointments.</td></tr>
                <%
                } else {
                    for (Appointment a : upcoming) {
                %>
                <tr>
                    <td><%= a.getAppointmentNo() %></td>
                    <td><%= a.getPatientID() %></td>
                    <td><%= a.getAppointmentDate() %></td>
                    <td><%= a.getAppointmentTime() %></td>
                    <td><span class="badge <%= badgeClass(a.getStatus()) %>"><%= a.getStatus() %></span></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>