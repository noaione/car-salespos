<%-- 
    Document   : index
    Created on : Mar 9, 2023, 8:22:45 AM
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
        <title>Home Page</title>
    </head>
    <body>
        <h1>Welcome back, <strong><% user.getUsername(); %></strong>!</h1>
        <p>What do you want to do today?</p>
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
    </body>
</html>
