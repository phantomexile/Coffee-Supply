<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Check if user is already logged in
    if (session.getAttribute("user") != null) {
        // User is logged in, redirect to appropriate dashboard
        String roleName = (String) session.getAttribute("roleName");
        if (roleName != null) {
            switch (roleName.toLowerCase()) {
                case "admin":
                    request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
                    break;
                case "hr":
                    request.getRequestDispatcher("/WEB-INF/views/hr/dashboard.jsp").forward(request, response);
                    break;
                case "inventory":
                    request.getRequestDispatcher("/WEB-INF/views/inventory/dashboard.jsp").forward(request, response);
                    break;
                case "barista":
                    request.getRequestDispatcher("/WEB-INF/views/barista/dashboard.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/login");
                    break;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    } else {
        // User is not logged in, redirect to login page
        response.sendRedirect(request.getContextPath() + "/login");
    }
%>