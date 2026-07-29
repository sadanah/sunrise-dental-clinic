<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register Appointment</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h1>Register New Appointment</h1>

<% if (request.getAttribute("errorMessage") != null) { %>
<p class="error"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<form action="appointments/register" method="post" onsubmit="return validateForm()">
    <label>Patient ID</label>
    <input type="text" id="patientID" name="patientID" required>

    <label>Dentist ID</label>
    <input type="text" id="dentistID" name="dentistID" required>

    <label>Treatment ID</label>
    <input type="text" id="treatmentID" name="treatmentID" required>

    <label>Date</label>
    <input type="date" id="date" name="date" required>

    <label>Time</label>
    <input type="time" id="time" name="time" required>

    <button type="submit">Register</button>
</form>
<a href="receptionist-dashboard.jsp">Back to Dashboard</a>

<script>
    function validateForm() {
        const date = document.getElementById('date').value;
        const selectedDate = new Date(date);
        const today = new Date();
        today.setHours(0,0,0,0);
        if (selectedDate < today) {
            alert('Appointment date cannot be in the past.');
            return false;
        }
        return true;
    }
</script>
</body>
</html>