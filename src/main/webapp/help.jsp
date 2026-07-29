<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 4:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.sunrisedentalclinic.domain.Session" %>
<%@ include file="/WEB-INF/jspf/dashboard-url.jspf" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
    <title>Help</title>
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>
<h1>Help Section</h1>
<p>New to the system? Select a topic below for step-by-step guidance.</p>

<h2>Topics</h2>
<ul>
    <%
        List<String> topics = (List<String>) request.getAttribute("topics");
        for (String topic : topics) {
    %>
    <li><a href="<%= ctx %>/help?topic=<%= topic %>"><%= topic.replace("-", " ") %></a></li>
    <% } %>
</ul>

<%
    String selectedTopic = (String) request.getAttribute("selectedTopic");
    if (selectedTopic != null) {
%>
<hr>
<h2><%= selectedTopic.replace("-", " ") %></h2>
<p><%= request.getAttribute("helpContent") %></p>
<% } %>

<a href="<%= dashboardUrl %>">Back to Dashboard</a>
</body>
</html>