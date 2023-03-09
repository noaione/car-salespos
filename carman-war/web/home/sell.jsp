<%-- 
    Document   : sell
    Created on : Mar 9, 2023, 9:57:57 AM
    Author     : N4O
--%>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@page import="model.User"%>
<% 
    User user = (User)request.getSession().getAttribute("userCtx");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    } else {
        if (user.isManager()) {
            response.sendRedirect(request.getContextPath() + "/home/index.jsp");
        }
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sell a car</title>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <style>
            .car-table {
                border: 1px solid black;
                border-collapse: collapse;
                border-spacing: 1rem;
            }
        </style>
    </head>
    <body>
        <nav>
        <%
            if (user.isManager()) {
        %>
            <jsp:include page="/WEB-INF/navmanager.jsp" />
        <%
            } else if (user.isUser()) {
        %>
            <jsp:include page="/WEB-INF/navuser.jsp" />
        <%
            }
        %>
        </nav>
        <h2>Sell a new car?</h2>
        <form action="<%= request.getContextPath() + "/home/Sell" %>" method="POST">
            <table>
                <tr>
                    <td>Name</td>
                    <td><input type="text" name="name" size="20" /></td>
                </tr>
                <tr>
                    <td>Price</td>
                    <td><input type="number" name="price" size="20" /></td>
                </tr>
            </table>
            <p>
                <input type="hidden" name="sell-action" value="create-new">
                <input type="submit" value="Sell!">
            </p>
        </form>
    </body>
</html>
