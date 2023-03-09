<%-- 
    Document   : sales
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
        <title>Sales reports</title>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <style>
            .car-table {
                border: 1px solid black;
                border-collapse: collapse;
                border-spacing: 1rem;
            }
            
            .flex {
                display: flex;
            }
            
            .flex-row {
                flex-direction: row;
            }
            
            .flex-col {
                flex-direction: column;
            }
            
            p {
                margin-top: 0.3rem;
                margin-bottom: 0.3rem;
            }
            h3 {
                margin-top: 0.7rem;
                margin-bottom: 0.7rem;
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
        <h1>Your Car Sales</h1>
    </body>
</html>
