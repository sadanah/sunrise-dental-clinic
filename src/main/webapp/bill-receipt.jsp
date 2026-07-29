<%--
  Created by IntelliJ IDEA.
  User: Sadana
  Date: 7/29/2026
  Time: 12:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedentalclinic.domain.Bill" %>
<% String ctx = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head><title>Bill Receipt</title></head>
<body>
<h1>Bill Receipt</h1>
<%
    Bill bill = (Bill) request.getAttribute("bill");
%>
<table border="1" cellpadding="8">
    <tr><td>Bill ID</td><td><%= bill.getBillID() %></td></tr>
    <tr><td>Appointment No</td><td><%= bill.getAppointmentNo() %></td></tr>
    <tr><td>Consultation Fee</td><td><%= bill.getConsultationFee() %></td></tr>
    <tr><td>Treatment Cost</td><td><%= bill.getTreatmentCost() %></td></tr>
    <tr><td><strong>Total Amount</strong></td><td><strong><%= bill.getTotalAmount() %></strong></td></tr>
    <tr><td>Generated Date</td><td><%= bill.getGeneratedDate() %></td></tr>
</table>
<a href="<%= ctx %>/receptionist-dashboard.jsp">Back to Dashboard</a>
</body>
</html>
