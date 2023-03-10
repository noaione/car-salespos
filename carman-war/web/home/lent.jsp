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
        <title>Lent a car</title>
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
            <h1 class="text-center text-xl font-semibold mt-2">Lend a car?</h1>
        </div>
        <form action="<%= request.getContextPath() + "/home/Lent" %>" method="POST" class="flex flex-col gap-1">
            <div class="flex flex-col gap-1 align-middle justify-start">
                <div class="flex"><label>Car Model</label></div>
                <input type="text" name="name" class="form-input bg-gray-700 text-white rounded-md"  placeholder="Enter car model"  />
            </div>
            <div class="flex flex-col gap-1 align-middle justify-start">
                <div class="flex"><label>Price (In RM)</label></div>
                <input type="number" name="price" min="1" value="1" class="form-input bg-gray-700 text-white rounded-md"  placeholder="1" />
            </div>
            <div class="flex flex-col gap-1 align-middle justify-center mt-2">
                <input type="hidden" name="sell-action" value="create-new">
                <input type="submit" value="Lend it" class="px-2 py-2 rounded-md bg-emerald-500 text-white hover:bg-emerald-600 transition">
            </div>
        </form>
    </body>
</html>
