/*
 * Forgot Password Controller
 */
package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.UserDAO;
import model.User;
import service.EmailService;
import service.PasswordResetTokenManager;
import service.UserService;

/**
 * Controller for forgot password functionality
 */
@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/forgot-password", "/reset-password"})
public class ForgotPasswordController extends HttpServlet {
    
    private UserDAO userDAO;
    private EmailService emailService;
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        emailService = new EmailService();
        userService = new UserService();
    }
   
    /** 
     * Handles GET requests
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String path = request.getServletPath();
        
        if ("/reset-password".equals(path)) {
            // Show reset password form
            String token = request.getParameter("token");
            
            if (token == null || token.trim().isEmpty()) {
                request.setAttribute("error", "Token không hợp lệ");
                request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Validate token
            String email = PasswordResetTokenManager.validateToken(token);
            
            if (email == null) {
                request.setAttribute("error", "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn. Vui lòng yêu cầu lại.");
                request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Token is valid, show reset form
            request.setAttribute("token", token);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/common/reset-password.jsp").forward(request, response);
            
        } else {
            // Show forgot password form
            request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
        }
    }

    /** 
     * Handles POST requests
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String path = request.getServletPath();
        
        if ("/reset-password".equals(path)) {
            // Handle password reset
            handlePasswordReset(request, response);
        } else {
            // Handle forgot password request
            handleForgotPassword(request, response);
        }
    }
    
    /**
     * Handle forgot password request (send email)
     */
    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String email = request.getParameter("email");
        
        // Validate email
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập địa chỉ email");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
            return;
        }
        
        // Check if email exists in database
        User user = userDAO.getUserByEmail(email.trim());
        
        if (user == null) {
            // Email không tồn tại trong hệ thống
            request.setAttribute("error", "Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại địa chỉ email.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
            return;
        }
        
        // Check if user is active
        if (!user.isActive()) {
            request.setAttribute("error", "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
            return;
        }
        
        // Generate reset token
        String token = PasswordResetTokenManager.generateToken(email.trim());
        
        // Create reset link
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        String resetLink = baseUrl + contextPath + "/reset-password?token=" + token;
        
        // Send email
        boolean emailSent = emailService.sendPasswordResetEmail(email.trim(), resetLink);
        
        if (emailSent) {
            request.setAttribute("success", "Link đặt lại mật khẩu đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư (và cả thư mục spam). Link có hiệu lực trong 2 giờ.");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau.");
        }
        
        request.setAttribute("email", email);
        request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
    }
    
    /**
     * Handle password reset (update password)
     */
    private void handlePasswordReset(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String token = request.getParameter("token");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            System.out.println("=== RESET PASSWORD DEBUG ===");
            System.out.println("Token: " + token);
            System.out.println("New Password length: " + (newPassword != null ? newPassword.length() : "null"));
            System.out.println("Confirm Password length: " + (confirmPassword != null ? confirmPassword.length() : "null"));
            
            // Validate inputs
            if (token == null || token.trim().isEmpty()) {
                System.out.println("ERROR: Token is empty");
                request.setAttribute("error", "Token không hợp lệ");
                request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
                return;
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                System.out.println("ERROR: New password is empty");
                request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/common/reset-password.jsp").forward(request, response);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("ERROR: Passwords don't match");
                request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/common/reset-password.jsp").forward(request, response);
                return;
            }
            
            // Validate password strength
            if (newPassword.length() < 6) {
                System.out.println("ERROR: Password too short");
                request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/common/reset-password.jsp").forward(request, response);
                return;
            }
            
            // Validate token
            String email = PasswordResetTokenManager.validateToken(token);
            System.out.println("Validated email: " + email);
            
            if (email == null) {
                System.out.println("ERROR: Token validation failed");
                request.setAttribute("error", "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn. Vui lòng yêu cầu lại.");
                request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Get user
            User user = userDAO.getUserByEmail(email);
            System.out.println("User found: " + (user != null ? user.getUserID() : "null"));
            
            if (user == null) {
                System.out.println("ERROR: User not found");
                request.setAttribute("error", "Không tìm thấy người dùng");
                request.getRequestDispatcher("/WEB-INF/views/common/forgot-password.jsp").forward(request, response);
                return;
            }
            
            // Hash new password
            System.out.println("Hashing password...");
            String hashedPassword = userService.hashPassword(newPassword);
            System.out.println("Password hashed successfully");
            
            // Update password
            System.out.println("Updating password in database...");
            boolean updated = userDAO.updateUserPassword(user.getUserID(), hashedPassword);
            System.out.println("Update result: " + updated);
            
            if (updated) {
                // Mark token as used and remove it
                PasswordResetTokenManager.markTokenAsUsed(token);
                PasswordResetTokenManager.removeToken(token);
                
                System.out.println("SUCCESS: Redirecting to login");
                // Redirect to login with success message
                String successMessage = java.net.URLEncoder.encode("Mật khẩu đã được đặt lại thành công. Vui lòng đăng nhập với mật khẩu mới.", "UTF-8");
                response.sendRedirect(request.getContextPath() + "/login?message=" + successMessage);
            } else {
                System.out.println("ERROR: Database update failed");
                request.setAttribute("error", "Có lỗi xảy ra khi đặt lại mật khẩu. Vui lòng thử lại.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/WEB-INF/views/common/reset-password.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION in handlePasswordReset: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi hệ thống xảy ra: " + e.getMessage());
            request.setAttribute("token", request.getParameter("token"));
            request.getRequestDispatcher("/WEB-INF/views/common/reset-password.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Forgot Password Controller";
    }
}
