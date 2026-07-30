<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 4:50 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Help</title>
</head>

<body>
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div>
    <h1>Help Section</h1>
    <p>New to the system? Select a topic below for step-by-step guidance.</p>

    <h2>Topics</h2>

    <ul>
        <%
            List<String> topics = (List<String>) request.getAttribute("topics");
            if (topics != null) {
                for (String topic : topics) {
        %>
        <li>
            <a href="<%= ctx %>/help?topic=<%= topic %>">
                <%= topic.replace("-", " ") %>
            </a>
        </li>
        <%
                }
            }
        %>
    </ul>

    <%
        String selectedTopic = (String) request.getAttribute("selectedTopic");
        if (selectedTopic != null) {
    %>

    <hr>

    <h2><%= selectedTopic.replace("-", " ") %></h2>

    <p><%= request.getAttribute("helpContent") %></p>

    <%
        }
    %>

    <a class="back-link" href="<%= dashboardUrl %>">Back to Dashboard</a>
</div>

</body>
</html>