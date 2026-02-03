package controller;

import dao.ShiftDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Shift;
import model.ShiftAssignment;
import model.User;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@WebServlet(name = "ShiftController", urlPatterns = {"/shift/*", "/shift"})
public class ShiftController extends HttpServlet {

    private ShiftDAO shiftDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        shiftDAO = new ShiftDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        try {
            // Routes available for all users
            if ("/my-shifts".equals(pathInfo)) {
                viewMyShifts(request, response, currentUser);
                return;
            }
            
            // Management routes - Only HR and Admin
            if (!"HR".equalsIgnoreCase(currentUserRole) && !"Admin".equalsIgnoreCase(currentUserRole)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
                return;
            }
            
            if (pathInfo == null || pathInfo.equals("/")) {
                if (action == null || action.equals("list")) {
                    listShifts(request, response);
                } else if (action.equals("create")) {
                    showCreateShiftForm(request, response);
                } else if (action.equals("edit")) {
                    showEditShiftForm(request, response);
                } else if (action.equals("assignments")) {
                    listAssignments(request, response);
                } else if (action.equals("assign")) {
                    showAssignForm(request, response);
                } else {
                    listShifts(request, response);
                }
            } else if (pathInfo.equals("/toggle")) {
                toggleShiftStatus(request, response);
            } else if (pathInfo.equals("/delete")) {
                deleteShift(request, response);
            } else if (pathInfo.equals("/delete-assignment")) {
                deleteAssignment(request, response);
            } else {
                listShifts(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check permission
        if (!"HR".equalsIgnoreCase(currentUserRole) && !"Admin".equalsIgnoreCase(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thực hiện thao tác này");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                createShift(request, response);
            } else if ("update".equals(action)) {
                updateShift(request, response);
            } else if ("assign".equals(action)) {
                assignUserToShift(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/shift");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    // ==================== SHIFT MANAGEMENT METHODS ====================

    /**
     * Display list of all shifts
     */
    private void listShifts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Shift> shifts = shiftDAO.getAllShifts();
        request.setAttribute("shifts", shifts);
        request.getRequestDispatcher("/WEB-INF/views/shift/shift-list.jsp").forward(request, response);
    }

    /**
     * Show form to create new shift
     */
    private void showCreateShiftForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> managers = shiftDAO.getAllActiveUsers();
        request.setAttribute("managers", managers);
        request.setAttribute("mode", "create");
        request.getRequestDispatcher("/WEB-INF/views/shift/shift-form.jsp").forward(request, response);
    }

    /**
     * Show form to edit existing shift
     */
    private void showEditShiftForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int shiftID = Integer.parseInt(request.getParameter("id"));
            Shift shift = shiftDAO.getShiftById(shiftID);
            
            if (shift == null) {
                request.setAttribute("errorMessage", "Không tìm thấy ca làm việc");
                listShifts(request, response);
                return;
            }
            
            List<User> managers = shiftDAO.getAllActiveUsers();
            request.setAttribute("shift", shift);
            request.setAttribute("managers", managers);
            request.setAttribute("mode", "edit");
            request.getRequestDispatcher("/WEB-INF/views/shift/shift-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID ca làm việc không hợp lệ");
            listShifts(request, response);
        }
    }

    /**
     * Create new shift
     */
    private void createShift(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String shiftName = request.getParameter("shiftName");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            String description = request.getParameter("description");
            String managerIDStr = request.getParameter("managerID");
            boolean isActive = "on".equals(request.getParameter("isActive"));

            // Validation
            if (shiftName == null || shiftName.trim().isEmpty() ||
                startTimeStr == null || startTimeStr.trim().isEmpty() ||
                endTimeStr == null || endTimeStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc");
                showCreateShiftForm(request, response);
                return;
            }

            Time startTime = Time.valueOf(startTimeStr + ":00");
            Time endTime = Time.valueOf(endTimeStr + ":00");
            Integer managerID = (managerIDStr != null && !managerIDStr.isEmpty()) ? 
                                Integer.parseInt(managerIDStr) : null;

            Shift shift = new Shift(shiftName, startTime, endTime, description, managerID, isActive);
            
            boolean success = shiftDAO.insertShift(shift);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shift?success=create");
            } else {
                request.setAttribute("errorMessage", "Không thể tạo ca làm việc. Vui lòng thử lại.");
                showCreateShiftForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            showCreateShiftForm(request, response);
        }
    }

    /**
     * Update existing shift
     */
    private void updateShift(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int shiftID = Integer.parseInt(request.getParameter("shiftID"));
            String shiftName = request.getParameter("shiftName");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            String description = request.getParameter("description");
            String managerIDStr = request.getParameter("managerID");
            boolean isActive = "on".equals(request.getParameter("isActive"));

            // Validation
            if (shiftName == null || shiftName.trim().isEmpty() ||
                startTimeStr == null || startTimeStr.trim().isEmpty() ||
                endTimeStr == null || endTimeStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc");
                showEditShiftForm(request, response);
                return;
            }

            Time startTime = Time.valueOf(startTimeStr + ":00");
            Time endTime = Time.valueOf(endTimeStr + ":00");
            Integer managerID = (managerIDStr != null && !managerIDStr.isEmpty()) ? 
                                Integer.parseInt(managerIDStr) : null;

            Shift shift = new Shift();
            shift.setShiftID(shiftID);
            shift.setShiftName(shiftName);
            shift.setStartTime(startTime);
            shift.setEndTime(endTime);
            shift.setDescription(description);
            shift.setManagerID(managerID);
            shift.setActive(isActive);
            
            boolean success = shiftDAO.updateShift(shift);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shift?success=update");
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật ca làm việc. Vui lòng thử lại.");
                showEditShiftForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            showEditShiftForm(request, response);
        }
    }

    /**
     * Toggle shift active status
     */
    private void toggleShiftStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int shiftID = Integer.parseInt(request.getParameter("id"));
            boolean success = shiftDAO.toggleShiftStatus(shiftID);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shift?success=toggle");
            } else {
                response.sendRedirect(request.getContextPath() + "/shift?error=toggle");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/shift?error=invalid");
        }
    }

    /**
     * Delete shift
     */
    private void deleteShift(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int shiftID = Integer.parseInt(request.getParameter("id"));
            boolean success = shiftDAO.deleteShift(shiftID);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shift?success=delete");
            } else {
                response.sendRedirect(request.getContextPath() + "/shift?error=delete");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/shift?error=invalid");
        }
    }

    // ==================== SHIFT ASSIGNMENT METHODS ====================

    /**
     * Display list of all shift assignments
     */
    private void listAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ShiftAssignment> assignments = shiftDAO.getAllAssignments();
        request.setAttribute("assignments", assignments);
        request.getRequestDispatcher("/WEB-INF/views/shift/assignment-list.jsp").forward(request, response);
    }

    /**
     * Show form to assign user to shift
     */
    private void showAssignForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Shift> shifts = shiftDAO.getAllActiveShifts();
        List<User> users = shiftDAO.getAllActiveUsers();
        request.setAttribute("shifts", shifts);
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/shift/shift-assign.jsp").forward(request, response);
    }

    /**
     * Assign user to shift
     */
    private void assignUserToShift(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int shiftID = Integer.parseInt(request.getParameter("shiftID"));
            int userID = Integer.parseInt(request.getParameter("userID"));
            String assignedDateStr = request.getParameter("assignedDate");
            String notes = request.getParameter("notes");

            // Validation
            if (assignedDateStr == null || assignedDateStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng chọn ngày phân công");
                showAssignForm(request, response);
                return;
            }

            Date assignedDate = Date.valueOf(assignedDateStr);
            
            ShiftAssignment assignment = new ShiftAssignment(shiftID, userID, assignedDate, notes);
            boolean success = shiftDAO.insertAssignment(assignment);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shift?action=assignments&success=assign");
            } else {
                request.setAttribute("errorMessage", "Không thể phân công ca làm. Vui lòng thử lại.");
                showAssignForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
            showAssignForm(request, response);
        }
    }

    /**
     * Delete assignment
     */
    private void deleteAssignment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int assignmentID = Integer.parseInt(request.getParameter("id"));
            boolean success = shiftDAO.deleteAssignment(assignmentID);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shift?action=assignments&success=delete");
            } else {
                response.sendRedirect(request.getContextPath() + "/shift?action=assignments&error=delete");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/shift?action=assignments&error=invalid");
        }
    }

    // ==================== MY SHIFTS - FOR ALL USERS ====================

    /**
     * View my shifts - Available for all roles
     */
    private void viewMyShifts(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        int userID = currentUser.getUserID();
        List<ShiftAssignment> myAssignments = shiftDAO.getAssignmentsByUserId(userID);
        request.setAttribute("myAssignments", myAssignments);
        request.setAttribute("currentUser", currentUser);
        request.getRequestDispatcher("/WEB-INF/views/shift/my-shifts.jsp").forward(request, response);
    }
}
