<%-- 
    Document   : index
    Created on : Mar 9, 2023, 8:22:45 AM
    Author     : N4O
--%>

<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@page import="model.User"%>

<%
    User user = (User)request.getSession().getAttribute("userCtx");
    String username = "Unknown User";
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    } else {
        username = user.getUsername();
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
        <jsp:include page="/WEB-INF/tailwind.jsp" />
    </head>
    <body class="bg-gray-800 text-white mx-2 my-2">
        <div class="flex flex-col mx-auto items-center">
            <h1 class="mt-4 text-2xl font-light">Welcome back, <span class="font-semibold text-glow"><%= username %></span>!</h1>
            <p>What do you want to do today?</p>
            <br />
            <nav>
            <%
                System.out.println("User type " + user.getType().name());
                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                } else {
            %>
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
            <%  }
            %>
            </nav>
        </div>
    </body>
</html>
