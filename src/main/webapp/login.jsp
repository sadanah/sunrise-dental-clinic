<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 11:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Sunrise Dental Clinic — Login</title>
</head>
<body class="login-page">

<div class="login-overlay">

    <div class="hero-section">
        <h1>Sunrise Dental Clinic</h1>
        <p>Where Every Smile Shines.</p>
        <br>
        <br>
    </div>

    <div class="login-card">

        <h2>Staff Login</h2>

        <% if (request.getAttribute("errorMessage") != null) { %>
        <p class="error"><%= request.getAttribute("errorMessage") %></p>
        <% } %>

        <form action="<%= ctx %>/login" method="post" onsubmit="return validateLoginForm()">

            <label for="username">Username</label>
            <input type="text"
                   id="username"
                   name="username"
                   required>

            <label for="password">Password</label>
            <input type="password"
                   id="password"
                   name="password"
                   required>

            <button type="submit">
                Login
            </button>

        </form>

    </div>

</div>

<footer class="login-footer">
    © 2026 Sunrise Dental Clinic. All Rights Reserved.
</footer>

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
