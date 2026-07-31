<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:20 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.*, java.util.List" %>
<% request.setAttribute("currentPage", "appointments"); %>
<!DOCTYPE html>
<html>
<head>
    <title>Register Appointment</title>
</head>
<body>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Register New Appointment</h1>

            <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error"><%= request.getAttribute("errorMessage") %></p>
            <% } %>

            <form action="<%= ctx %>/appointments/register" method="post" onsubmit="return validateForm()">
                <label>Patient</label>
                <select name="patientID" required>
                    <option value="">-- Select Patient --</option>
                    <%
                        List<Patient> patients = (List<Patient>) request.getAttribute("patients");
                        if (patients != null) {
                            for (Patient p : patients) {
                    %>
                    <option value="<%= p.getPatientID() %>"><%= p.getName() %> (<%= p.getPatientID() %>)</option>
                    <%
                            }
                        }
                    %>
                </select>

                <label>Dentist</label>
                <select name="dentistID" required>
                    <option value="">-- Select Dentist --</option>
                    <%
                        List<Dentist> dentists = (List<Dentist>) request.getAttribute("dentists");
                        if (dentists != null) {
                            for (Dentist d : dentists) {
                    %>
                    <option value="<%= d.getStaffID() %>">Dr. <%= d.getName() %> (<%= d.getSpecialization() %>)</option>
                    <%
                            }
                        }
                    %>
                </select>

                <label>Treatment</label>
                <select name="treatmentID" required>
                    <option value="">-- Select Treatment --</option>
                    <%
                        List<TreatmentType> treatments = (List<TreatmentType>) request.getAttribute("treatments");
                        if (treatments != null) {
                            for (TreatmentType t : treatments) {
                    %>
                    <option value="<%= t.getTreatmentID() %>"><%= t.getTreatmentName() %> (Rs. <%= t.getBaseCost() %>)</option>
                    <%
                            }
                        }
                    %>
                </select>

                <label>Date</label>
                <input type="date" id="date" name="date" required>

                <label>Time</label>
                <input type="time" id="time" name="time" required>

                <button type="submit">Register</button>
            </form>

            <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>

            <script>
                function validateForm() {
                    const date = document.getElementById('date').value;
                    const selectedDate = new Date(date);
                    const today = new Date();
                    today.setHours(0, 0, 0, 0);

                    if (selectedDate < today) {
                        alert('Appointment date cannot be in the past.');
                        return false;
                    }
                    return true;
                }
            </script>

        </div>
    </div>
</div>
</body>
</html>