<%-- 
    Document   : login
    Created on : Mar 8, 2023, 8:31:30 PM
    Author     : N4O
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
        <jsp:include page="/WEB-INF/tailwind.jsp" />
    </head>
        <body class="bg-gray-800 text-white mx-2 my-2">
        <div class="flex flex-col mx-auto items-center">
            <h1 class="mt-4 text-2xl font-light">Car Rental System</h1>
            <h3 class="text-lg mt-2 font-semibold">Register Manager</h3>
            <br />
            <form action="Register" method="POST" class="flex flex-col gap-1">
                <div class="flex flex-col gap-1 align-middle justify-start">
                    <div class="flex"><label>Username</label></div>
                    <input type="text" name="username" class="form-input bg-gray-700 text-white rounded-md" />
                </div>
                <div class="flex flex-col gap-1 align-middle justify-start">
                    <div class="flex"><label>Password</label></div>
                    <input type="password" name="password" class="form-input bg-gray-700 text-white rounded-md" />
                </div>
                <div class="flex flex-col gap-1 align-middle justify-start">
                    <div class="flex"><label>Gender</label></div>
                    <select class="form-select bg-gray-700 text-white rounded-md" name="gender">
                        <option value="M" selected>Male</option>
                        <option value="F">Female</option>
                        <option value="O">Other</option>
                    </select>
                </div>
                <input type="hidden" name="utype" value="admin" />
                <div class="flex flex-col gap-1 align-middle justify-center mt-2">
                    <input type="submit" value="Register" class="px-2 py-2 rounded-md bg-emerald-500 text-white hover:bg-emerald-600 transition">
                </div>
            </form>
            <br />
            <div class="flex flex-col gap-1 justify-center text-center">
                <a href="login.jsp" class="nav-link">[Login]</a>
                <a href="register.jsp" class="nav-link">[Register User]</a>
            </div>
        </div>
    </body>
</html>
