<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 11:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
    <title>Sunrise Dental Clinic — Login</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body class="login-page">
<div class="login-container">
    <h1>Sunrise Dental Clinic</h1>
    <h2>Staff Login</h2>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <p class="error"><%= request.getAttribute("errorMessage") %></p>
    <% } %>

    <form action="<%= ctx %>/login" method="post" onsubmit="return validateLoginForm()">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>
</div>

<script>
    function validateLoginForm() {
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        if (username === '' || password === '') {
            alert('Please enter both username and password.');
            return false;
        }
        return true;
    }
</script>
</body>
</html>