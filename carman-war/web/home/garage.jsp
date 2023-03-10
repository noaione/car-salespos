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
        <jsp:include page="/WEB-INF/tailwind.jsp" />
    </head>
    <body class="bg-gray-800 text-white mx-2 my-2">
        <div class="flex flex-col mx-auto items-center">
            <nav class="mt-4">
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
            <h1 id="garages" class="text-center text-xl font-semibold mt-2">Garages</h1>
            <p class="mt-2"><a class="nav-link" href="#pending">Pending Approval</a></p>
        </div>
    </body>
</html>
