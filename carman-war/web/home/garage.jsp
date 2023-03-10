<%-- 
    Document   : buy
    Created on : Mar 9, 2023, 9:57:57 AM
    Author     : N4O
--%>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@page import="model.User"%>
<% 
    User user = (User)request.getSession().getAttribute("userCtx");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Garage cars</title>
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
        <br/>
        <nav>
            <a href="#approval-pending">Approve pending car</a>
        </nav>
        <h1 id="available">Garage</h1>
    </body>
</html>
