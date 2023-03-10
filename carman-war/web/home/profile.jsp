<%-- 
    Document   : profile
    Created on : Mar 10, 2023, 3:49:03 PM
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
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
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
        </div>
        <h1 class="text-left text-xl font-semibold mt-2">Profile</h1>
        <div class="flex flex-col mt-2">
            <p class="font-light"><span class="font-semibold">Username</span>: <%= user.getUsername() %></p>
            <p class="font-light"><span class="font-semibold">Gender</span>: <%= user.getGender()%></p>
            <p class="font-light"><span class="font-semibold">Type</span>: <%= user.getType().asName() %></p>
        </div>
        <h1 class="text-left text-xl font-semibold mt-2 mt-4">Edit Profile</h1>
        <form action="<%= request.getContextPath() %>/home/Profile" method="POST" class="flex flex-col gap-1">
            <div class="flex flex-col gap-1 align-middle justify-start">
                <div class="flex"><label>Username</label></div>
                <input type="text" name="username" class="form-input bg-gray-700 text-white rounded-md" placeholder="New username" />
            </div>
            <div class="flex flex-col gap-1 align-middle justify-start">
                <div class="flex"><label>Current Password</label></div>
                <input type="password" name="oldpassword" class="form-input bg-gray-700 text-white rounded-md" placeholder="Current password" />
            </div>
            <div class="flex flex-col gap-1 align-middle justify-start">
                <div class="flex"><label>New Password</label></div>
                <input type="password" name="newpassword" class="form-input bg-gray-700 text-white rounded-md" placeholder="New password" />
            </div>
            <div class="flex flex-col gap-1 align-middle justify-start">
                <div class="flex"><label>Gender</label></div>
                <select class="form-select bg-gray-700 text-white rounded-md" name="gender">
                    <option value="M">Male</option>
                    <option value="F">Female</option>
                    <option value="O">Other</option>
                </select>
            </div>
            <div class="flex flex-col gap-1 align-middle justify-center mt-2">
                <input type="hidden" name="uuid" value="<%= user.getId() %>" />
                <input type="submit" value="Update Profile" class="px-2 py-2 rounded-md bg-emerald-500 text-white hover:bg-emerald-600 transition">
            </div>
        </form>
    </body>
</html>
