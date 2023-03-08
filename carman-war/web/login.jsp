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
        <title>Login Page</title>
    </head>
    <body>
        <p>
            <a href="register.jsp">Register</a>
        </p>
        <br/>
        <div>
            <form action="Login" method="POST">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" size="20" /></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" size="20" /></td>
                    </tr>
                </table>
                <p>
                    <input type="submit" value="Login">
                </p>
            </form>
        </div>
    </body>
</html>
