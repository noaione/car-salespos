<%-- 
    Document   : navuser
    Created on : Mar 9, 2023, 8:31:42 AM
    Author     : N4O
--%>

<div class="flex flex-row gap-2">
    <a href="<%= request.getContextPath() %>/home/index.jsp" class="nav-link">Home</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Garage" class="nav-link">Garage</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Reports" class="nav-link">Reports</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Users" class="nav-link">User Base</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/profile.jsp" class="nav-link">Profile</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/Logout" class="nav-link">Logout</a>
</div>
