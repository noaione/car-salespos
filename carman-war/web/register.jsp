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
    </head>
    <body>
        <p>
            <a href="login.jsp">Login</a>
        </p>
        <br/>
        <div>
            <form action="Register" method="POST">
                <table>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="username" size="20" /></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" size="20" /></td>
                    </tr>
                    <tr>
                        <td>Gender:</td>
                        <td>
                            <select name="gender">
                                <option value="M" selected>Male</option>
                                <option value="F">Female</option>
                                <option value="O">Other</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <input type="hidden" value="customer" name="utype" />
                <p>
                    <input type="submit" value="Register">
                </p>
            </form>
        </div>
    </body>
</html>
