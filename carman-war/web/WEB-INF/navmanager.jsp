<%-- 
    Document   : navuser
    Created on : Mar 9, 2023, 8:31:42 AM
    Author     : N4O
--%>

<div class="flex flex-row gap-2">
    <a href="<%= request.getContextPath() %>/home/index.jsp" class="nav-link">Home</a>|
    <a href="<%= request.getContextPath() %>/home/Garage" class="nav-link">Garage</a>|
    <a href="<%= request.getContextPath() %>/home/Reports" class="nav-link">Reports</a>|
    <a href="<%= request.getContextPath() %>/home/Profile" class="nav-link">Profile</a>|
    <a href="<%= request.getContextPath() %>/Logout" class="nav-link">Logout</a>|
</div>
