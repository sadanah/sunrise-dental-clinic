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
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title>Help</title>
</head>

<body>
<div class="app-shell">
    <%@ include file="/WEB-INF/jspf/sidebar.jspf" %>

    <div class="app-main">
        <div class="container">
            <h1>Help Section</h1>
            <p class="subtitle">New to the system? Select a topic below for step-by-step guidance.</p>

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
    </div>
</div>
</body>
</html>
