/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import model.Setting;
import service.UserService;
import service.EmailService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    private UserService userService;
    private EmailService emailService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
        emailService = new EmailService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication and authorization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");

        // Check if user has permission to access user management
        if (!"HR".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listUser(request, response, currentUserRole);
                    break;
                case "create":
                    showCreateForm(request, response, currentUserRole);
                    break;
                case "edit":
                    showEditForm(request, response, currentUserRole);
                    break;
                case "view":
                    viewUser(request, response);
                    break;
                case "changePassword":
                    showChangePasswordForm(request, response);
                    break;
                case "toggleActive":
                    toggleActive(request, response, currentUserRole);
                    break;
                default:
                    listUser(request, response, currentUserRole);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication and authorization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");

        // Check if user has permission to access user management
        if (!"HR".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "create":
                    createUser(request, response, currentUserRole);
                    break;
                case "update":
                    updateUser(request, response, currentUserRole);
                    break;
                case "delete":
                    deleteUser(request, response, currentUserRole);
                    break;
                case "changePassword":
                    changePassword(request, response);
                    break;
                case "toggle-status":
                    toggleUserStatus(request, response, currentUserRole);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/user");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/views/common/error.jsp").forward(request, response);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws ServletException, IOException {
        String activeFilter = request.getParameter("active");

        // Get pagination parameters
        int page = 1;
        int pageSize = 10;
        Boolean activeOnly = null;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
//
//        String pageSizeParam = request.getParameter("pageSize");
//        if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
//            try {
//                pageSize = Integer.parseInt(pageSizeParam);
//                if (pageSize < 5) {
//                    pageSize = 5;
//                }
//                if (pageSize > 50) {
//                    pageSize = 50;
//                }
//            } catch (NumberFormatException e) {
//                pageSize = 10;
//            }
//        }

        if (activeFilter != null && !activeFilter.isEmpty()) {
            activeOnly = "true".equals(activeFilter);
        }

        // Get filter parameters
        Integer roleFilter = null;
        String roleFilterParam = request.getParameter("roleFilter");
        if (roleFilterParam != null && !roleFilterParam.isEmpty() && !"all".equals(roleFilterParam)) {
            try {
                roleFilter = Integer.parseInt(roleFilterParam);
            } catch (NumberFormatException e) {
                // Ignore invalid role filter
            }
        }

        String searchKeyword = request.getParameter("search");
        if (searchKeyword != null && searchKeyword.trim().isEmpty()) {
            searchKeyword = null;
        }
        
        // Get sort parameters
        String sortBy = request.getParameter("sortBy");
        if (sortBy != null && sortBy.trim().isEmpty()) {
            sortBy = null;
        }
        
        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder != null && sortOrder.trim().isEmpty()) {
            sortOrder = null;
        }

        // Get users and pagination info with sorting
        List<User> users = userService.getUser(page, pageSize, roleFilter, searchKeyword, activeOnly, sortBy, sortOrder);
        int totalUser = userService.getTotalUserCount(roleFilter, searchKeyword, activeOnly);

        // Filter out Admin users for HR
        if ("HR".equals(currentUserRole)) {
            users = users.stream()
                    .filter(user -> user.getRoleID() != 2) // Exclude Admin (roleID = 2)
                    .collect(java.util.stream.Collectors.toList());
        }
        int totalPages = (int) Math.ceil((double) totalUser / pageSize);

        // Get available roles for filtering
        List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);

        // Set attributes
        request.setAttribute("users", users);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalUser", totalUser);
        request.setAttribute("roleFilter", roleFilter);
        request.setAttribute("activeOnly", activeOnly);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("availableRoles", availableRoles);
        request.setAttribute("currentUserRole", currentUserRole);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/hr/user-list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws ServletException, IOException {

        // Get available roles for current user
        List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);

        request.setAttribute("availableRoles", availableRoles);
        request.setAttribute("action", "create");
        // Use formUser to avoid clashing with sessionScope.user (logged-in user)
        request.setAttribute("formUser", null);

        request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("id");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            User user = userService.getUserById(userId);

            if (user == null) {
                request.setAttribute("error", "Không tìm thấy người dùng");
                request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
                return;
            }

            // Get available roles for current user
            List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);

            request.setAttribute("formUser", user);
            request.setAttribute("availableRoles", availableRoles);
            request.setAttribute("action", "edit");

            request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }

    private void viewUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("id");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            User user = userService.getUserById(userId);

            if (user == null) {
                request.setAttribute("error", "Không tìm thấy người dùng");
                request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/hr/user-view.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }

    private void showChangePasswordForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("id");
        if (userIdParam == null || userIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            User user = userService.getUserById(userId);

            if (user == null) {
                request.setAttribute("error", "Không tìm thấy người dùng");
                request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("user", user);
            request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws ServletException, IOException {

        // Get form data
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String roleIdParam = request.getParameter("roleId");
        String isActiveParam = request.getParameter("isActive");

        // Validate full name length
        if (fullName == null || fullName.trim().length() < 3) {
            List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
            request.setAttribute("availableRoles", availableRoles);
            request.setAttribute("action", "create");
            request.setAttribute("error", "Họ và tên phải có ít nhất 3 ký tự");
            request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
            return;
        }
        
        if (fullName.trim().length() > 100) {
            List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
            request.setAttribute("availableRoles", availableRoles);
            request.setAttribute("action", "create");
            request.setAttribute("error", "Họ và tên không được vượt quá 100 ký tự");
            request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
            return;
        }

        try {
            int roleId = Integer.parseInt(roleIdParam);
            boolean isActive = Boolean.parseBoolean(isActiveParam);
            
            // Validate email format
            if (email == null || !email.contains("@")) {
                List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
                request.setAttribute("availableRoles", availableRoles);
                request.setAttribute("action", "create");
                request.setAttribute("error", "Email không hợp lệ");
                request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
                return;
            }
            
            // Auto-generate username from email (part before @)
            
            // Auto-generate random password (8 characters: letters + numbers)
            String password = generateRandomPassword();

            // Create user object
            User user = new User();
            user.setFullName(fullName.trim());
            user.setEmail(email.trim());
            user.setRoleID(roleId);
            user.setActive(isActive);
            user.setGender("Nam"); // Default gender
            user.setPhone(""); // Empty phone
            user.setAddress(""); // Empty address

            // Create user
            String error = userService.createUser(user, password, currentUserRole);

            if (error == null) {
                // Send email with login credentials (don't block if email fails)
                try {
                    boolean emailSent = emailService.sendWelcomeEmail(email, fullName, password);
                    
                    if (emailSent) {
                        request.getSession().setAttribute("successMessage", 
                            "Tạo tài khoản thành công! Thông tin đăng nhập đã được gửi đến email: " + email);
                    } else {
                        request.getSession().setAttribute("successMessage", 
                            "Tạo tài khoản thành công! Mật khẩu: " + password);
                    }
                } catch (Exception emailEx) {
                    // Email failed but user created - show credentials in message
                    System.err.println("Failed to send welcome email: " + emailEx.getMessage());
                    request.getSession().setAttribute("successMessage", 
                        "Tạo tài khoản thành công!  Mật khẩu: " + password + " (Không thể gửi email)");
                }
                
                response.sendRedirect(request.getContextPath() + "/user");
            } else {
                // Error
                List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
                request.setAttribute("formUser", user);
                request.setAttribute("availableRoles", availableRoles);
                request.setAttribute("action", "create");
                request.setAttribute("error", error);
                request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
            request.setAttribute("availableRoles", availableRoles);
            request.setAttribute("action", "create");
            request.setAttribute("error", "Vui lòng chọn vai trò hợp lệ");
            request.setAttribute("formUser", null);
            request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
            request.setAttribute("availableRoles", availableRoles);
            request.setAttribute("action", "create");
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.setAttribute("formUser", null);
            request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
        }
    }
    
    /**
     * Generate random password with 8 characters (letters + numbers)
     */
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        // Ensure at least 1 uppercase, 1 lowercase, 1 number
        password.append(chars.charAt(random.nextInt(26))); // Uppercase
        password.append(chars.charAt(26 + random.nextInt(26))); // Lowercase
        password.append(chars.charAt(52 + random.nextInt(10))); // Number
        
        // Fill remaining 5 characters randomly
        for (int i = 0; i < 5; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String roleIdParam = request.getParameter("roleId");
        String isActiveParam = request.getParameter("isActive");

        try {
            int userId = Integer.parseInt(userIdParam);
            int roleId = Integer.parseInt(roleIdParam);
            boolean isActive = Boolean.parseBoolean(isActiveParam);

            //Update user object
            User user = new User();
            user.setUserID(userId);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setGender(gender);
            user.setPhone(phone);
            user.setAddress(address);
            user.setRoleID(roleId);
            user.setActive(isActive);

            // Update user
            String error = userService.updateUser(user, currentUserRole);

            if (error == null) {
                // Success
                request.getSession().setAttribute("successMessage", "Cập nhật thông tin thành công");
                response.sendRedirect(request.getContextPath() + "/user");
            } else {
                // Error
                List<Setting> availableRoles = userService.getAvailableRoles(currentUserRole);
                request.setAttribute("formUser", user);
                request.setAttribute("availableRoles", availableRoles);
                request.setAttribute("action", "edit");
                request.setAttribute("error", error);

                request.getRequestDispatcher("/WEB-INF/views/hr/user-form.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");

        try {
            int userId = Integer.parseInt(userIdParam);

            String error = userService.deleteUser(userId, currentUserRole);

            if (error == null) {
                request.getSession().setAttribute("successMessage", "Xóa tài khoản thành công");
            } else {
                request.getSession().setAttribute("errorMessage", error);
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ");
        }

        response.sendRedirect(request.getContextPath() + "/user");
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdParam = request.getParameter("userId");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        try {
            int userId = Integer.parseInt(userIdParam);

            // Check if passwords match
            if (!newPassword.equals(confirmPassword)) {
                User user = userService.getUserById(userId);
                request.setAttribute("user", user);
                request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                request.getRequestDispatcher("/WEB-INF/views/hr/change-password.jsp").forward(request, response);
                return;
            }

            String error = userService.changePassword(userId, newPassword);

            if (error == null) {
                request.getSession().setAttribute("successMessage", "Đổi mật khẩu thành công");
                response.sendRedirect(request.getContextPath() + "/user");
            } else {
                User user = userService.getUserById(userId);
                request.setAttribute("user", user);
                request.setAttribute("error", error);
                request.getRequestDispatcher("/WEB-INF/views/hr/change-password.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }
    
    private void toggleUserStatus(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws IOException {
        String idParam = request.getParameter("id");
        String newStatusParam = request.getParameter("newStatus");
        
        if (idParam == null || idParam.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/user");
            return;
        }
        
        try {
            int userId = Integer.parseInt(idParam);
            boolean newStatus = Boolean.parseBoolean(newStatusParam);
            
            User user = userService.getUserById(userId);
            if (user == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy người dùng");
                response.sendRedirect(request.getContextPath() + "/user");
                return;
            }
            
            // Permission check: Admin can toggle anyone except Admin; HR can toggle Inventory/Barista
            boolean allow = false;
            if ("Admin".equals(currentUserRole)) {
                allow = user.getRoleID() != 2; // Cannot toggle other Admin users
            } else if ("HR".equals(currentUserRole)) {
                allow = user.getRoleID() == 3 || user.getRoleID() == 4 || user.getRoleID() == 5; // Only Inventory/Barista
            }
            
            if (!allow) {
                request.getSession().setAttribute("errorMessage", "Bạn không có quyền thay đổi trạng thái người dùng này");
                response.sendRedirect(request.getContextPath() + "/user");
                return;
            }
            
            // Update status
            user.setActive(newStatus);
            String err = userService.updateUser(user, currentUserRole);
            
            if (err == null) {
                String statusMessage = newStatus ? 
                    "Đã kích hoạt tài khoản người dùng: " + user.getFullName() : 
                    "Đã ngưng hoạt động tài khoản người dùng: " + user.getFullName();
                request.getSession().setAttribute("successMessage", statusMessage);
            } else {
                request.getSession().setAttribute("errorMessage", "Lỗi: " + err);
            }
            
            response.sendRedirect(request.getContextPath() + "/user");
        } catch (NumberFormatException ex) {
            request.getSession().setAttribute("errorMessage", "ID người dùng không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }

    private void toggleActive(HttpServletRequest request, HttpServletResponse response, String currentUserRole)
            throws IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user");
            return;
        }
        try {
            int userId = Integer.parseInt(idParam);
            User user = userService.getUserById(userId);
            if (user == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy người dùng");
                response.sendRedirect(request.getContextPath() + "/user");
                return;
            }
            // Permission: Admin can toggle anyone except Admin; HR can toggle Inventory/Barista
            boolean allow;
            if ("Admin".equals(currentUserRole)) {
                allow = user.getRoleID() != 2;
            } else if ("HR".equals(currentUserRole)) {
                allow = user.getRoleID() == 3 || user.getRoleID() == 4 || user.getRoleID() == 5;
            } else {
                allow = false;
            }
            if (!allow) {
                request.getSession().setAttribute("errorMessage", "Bạn không có quyền thay đổi trạng thái tài khoản này");
                response.sendRedirect(request.getContextPath() + "/user");
                return;
            }
            user.setActive(!user.isActive());
            String err = userService.updateUser(user, currentUserRole);
            if (err == null) {
                request.getSession().setAttribute("successMessage", user.isActive() ? "Đã kích hoạt tài khoản" : "Đã vô hiệu hóa tài khoản");
            } else {
                request.getSession().setAttribute("errorMessage", err);
            }
            response.sendRedirect(request.getContextPath() + "/user");
        } catch (NumberFormatException ex) {
            response.sendRedirect(request.getContextPath() + "/user");
        }
    }
    
   

}
