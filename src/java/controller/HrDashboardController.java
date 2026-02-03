package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.User;

@WebServlet(name = "HrDashboardController", urlPatterns = {"/hr/dashboard"})
public class HrDashboardController extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");

        // Check if user has admin permission
        if (!"HR".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/dashboard";
        }

        try {
            switch (pathInfo) {
                case "/dashboard":
                    showDashboard(request, response);
                    break;
                default:
                    showDashboard(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get real statistics from database
            int totalUser = userDAO.getTotalUserCount(null, null, null);
            List<User> allUser = userDAO.getAllUser(1, Integer.MAX_VALUE, null, null, null);

            // Count users by role (excluding Admin from active/inactive count)
            int hrCount = 0;
            int adminCount = 0;
            int inventoryCount = 0;
            int baristaCount = 0;
            int activeUser = 0;
            int inactiveUser = 0;

            for (User user : allUser) {
                int roleID = user.getRoleID();

                // Count by role
                switch (roleID) {
                    case 1:
                        hrCount++;
                        break;
                    case 2:
                        adminCount++;
                        break;
                    case 3:
                        inventoryCount++;
                        break;
                    case 4:
                        baristaCount++;
                        break;
                }

                // Only count active/inactive for non-Admin users (roleID != 2)
                if (roleID != 2) {
                    if (user.isActive()) {
                        activeUser++;
                    } else {
                        inactiveUser++;
                    }
                }
            }

            // Calculate employee statistics (excluding Admin and HR roles for employee count)
            int totalEmployees = baristaCount + inventoryCount;

            // Get active employees for today's status (only Barista and Inventory staff)
            List<User> activeEmployeesToday = userDAO.getAllUser(1, 10, null, null, true);
            List<User> employeesForStatus = new ArrayList<>();
            for (User user : activeEmployeesToday) {
                if (user.getRoleID() == 3 || user.getRoleID() == 4) { // Inventory or Barista
                    employeesForStatus.add(user);
                }
            }

            // Set real dashboard data
            request.setAttribute("totalUser", totalUser);
            request.setAttribute("activeUser", activeUser);
            request.setAttribute("inactiveUser", inactiveUser);
            request.setAttribute("hrCount", hrCount);
            request.setAttribute("adminCount", adminCount);
            request.setAttribute("inventoryCount", inventoryCount);
            request.setAttribute("baristaCount", baristaCount);
            request.setAttribute("totalEmployees", totalEmployees);

            // Employee status for today
            request.setAttribute("employeesStatus", employeesForStatus);

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to basic info if database error
            request.setAttribute("totalUser", 0);
            request.setAttribute("activeUser", 0);
            request.setAttribute("totalEmployees", 0);
            request.setAttribute("newCandidates", 0);
            request.setAttribute("attendanceRate", "0%");
            request.setAttribute("avgWorkingHours", 0);
            request.setAttribute("error", "Không thể tải dữ liệu thống kê");
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard/hr.jsp").forward(request, response);
    }

}
