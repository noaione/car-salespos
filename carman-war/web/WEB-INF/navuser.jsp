<%-- 
    Document   : navuser
    Created on : Mar 9, 2023, 8:31:42 AM
    Author     : N4O
--%>

<div class="flex flex-row gap-1">
    <a href="<%= request.getContextPath() %>/home/index.jsp" class="nav-link">Home</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Rent" class="nav-link">Rent a Car</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Lent" class="nav-link">Lent a Car</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Rentals" class="nav-link">Rentals</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Sales" class="nav-link">Sales</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/home/Profile" class="nav-link">Profile</a>
    <span>|</span>
    <a href="<%= request.getContextPath() %>/Logout" class="nav-link">Logout</a>
</div>
