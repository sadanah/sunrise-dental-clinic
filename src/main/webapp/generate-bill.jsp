<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:49 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% request.setAttribute("currentPage", "billing"); %>
<!DOCTYPE html>
<html>
<head>
    <title>Generate Bill</title>
</head>
<body>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Generate Bill</h1>

            <% if (request.getAttribute("errorMessage") != null) { %>
            <p class="error"><%= request.getAttribute("errorMessage") %></p>
            <% } %>

            <form action="<%= ctx %>/billing" method="post" onsubmit="return validateDiscount()">
                <label>Appointment No</label>
                <input type="text" name="appointmentNo" required>

                <label>Discount % (optional)</label>
                <input type="number"
                       id="discountPercent"
                       name="discountPercent"
                       min="0"
                       max="100"
                       step="0.01">

                <div>
                    <button type="submit" name="action" value="generate">Generate Bill</button>
                    <button type="submit" name="action" value="print" class="secondary">Generate & Print</button>
                </div>
            </form>

            <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
        </div>
    </div>
</div>

<script>
    function validateDiscount() {
        const discount = document.getElementById('discountPercent').value;
        if (discount !== '' && (discount < 0 || discount > 100)) {
            alert('Discount must be between 0 and 100.');
            return false;
        }
        return true;
    }
</script>

</body>
</html>