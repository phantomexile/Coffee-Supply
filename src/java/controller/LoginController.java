package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession;
import service.AuthService;
import service.UserService;
import dao.UserDAO;
import dao.SettingDAO;
import model.AuthResponse;
import model.User;
import service.GoogleAuthService;

@WebServlet(name = "LoginController", urlPatterns = {"/login", "/profile", "/logout", "/login/google", "/google-auth", "/google-callback"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class LoginController extends HttpServlet {

    private AuthService authService;
    private UserService userService;
    private UserDAO userDAO;
    private GoogleAuthService googleAuthService;

    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthService();
        userService = new UserService();
        userDAO = new UserDAO();
        googleAuthService = new GoogleAuthService();
    }

    /**
     * Handles the HTTP <code>GET</code> method. Shows login page or logout
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set encoding for all requests
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        // Handle Google OAuth authorization
        if ("/google-auth".equals(path)) {
            try {
                String authUrl = googleAuthService.getAuthorizationUrl(request.getContextPath());
                if (authUrl != null) {
                    response.sendRedirect(authUrl);
                } else {
                    response.sendRedirect(request.getContextPath() + "/login?error=oauth_setup_failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/login?error=oauth_error");
            }
            return;
        }

        // Handle Google OAuth callback
        if ("/google-callback".equals(path)) {
            String code = request.getParameter("code");
            String error = request.getParameter("error");
            
            if (error != null) {
                response.sendRedirect(request.getContextPath() + "/login?error=oauth_denied");
                return;
            }
            
            if (code == null) {
                response.sendRedirect(request.getContextPath() + "/login?error=missing_auth_code");
                return;
            }
            
            try {
                // Exchange authorization code for access token
                String accessToken = googleAuthService.getAccessToken(code);
                if (accessToken == null) {
                    response.sendRedirect(request.getContextPath() + "/login?error=token_exchange_failed");
                    return;
                }
                
                // Get user info from Google
                GoogleAuthService.GoogleUserInfo userInfo = googleAuthService.getUserInfo(accessToken);
                if (userInfo == null || !userInfo.isVerifiedEmail()) {
                    response.sendRedirect(request.getContextPath() + "/login?error=unverified_email");
                    return;
                }
                
                String email = userInfo.getEmail();
                
                // Check if user exists in our database
                User user = userDAO.getUserByEmail(email);
                if (user == null) {
                    // User doesn't exist, redirect with error
                    response.sendRedirect(request.getContextPath() + "/login?error=account_not_found");
                    return;
                }
                
                // Verify user is active
                if (!user.isActive()) {
                    response.sendRedirect(request.getContextPath() + "/login?error=account_inactive");
                    return;
                }
                
                // Create user session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserID());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("roleId", user.getRoleID());
                
                // Get and set role name for consistency with email login
                String roleName = authService.getRoleName(user.getRoleID());
                session.setAttribute("roleName", roleName);
                
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Redirect to appropriate dashboard
                redirectToDashboard(request, response, user);
                
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/login?error=oauth_processing_failed");
            }
            return;
        }

        // Handle profile requests
        if ("/profile".equals(path)) {
            handleProfileGet(request, response);
            return;
        }

        // Handle logout
        if ("/logout".equals(path)) {
            handleLogout(request, response);
            return;
        }

        // Handle login requests
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // User is already logged in, redirect to dashboard
            redirectToDashboard(request, response, (User) session.getAttribute("user"));
            return;
        }

        // Show login page
        request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Process login form
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set encoding for Vietnamese
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        // Handle profile requests
        if ("/profile".equals(path)) {
            handleProfilePost(request, response);
            return;
        }

        // Handle Google Sign-In
        if ("/login/google".equals(path)) {
            handleGoogleLoginPost(request, response);
            return;
        }

        // Handle login requests
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // Skip reCAPTCHA verification (disabled)
        // String recaptchaToken = request.getParameter("g-recaptcha-response");
        // boolean recaptchaOk = verifyRecaptcha(recaptchaToken, request.getRemoteAddr());
        // if (!recaptchaOk) {
        //     request.setAttribute("error", "Xác thực reCAPTCHA thất bại. Vui lòng thử lại.");
        //     request.setAttribute("email", email);
        //     request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
        //     return;
        // }

        // Authenticate user
        AuthResponse authResponse = authService.login(email, password);

        if (authResponse.isSuccess()) {
            // Login successful, create session
            User user = authResponse.getUser();
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserID());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("roleId", user.getRoleID());
            session.setAttribute("roleName", authResponse.getRoleName());
            session.setMaxInactiveInterval(30 * 60);

            redirectToDashboard(request, response, user);
        } else {
            // Login failed, return to login page with error
            request.setAttribute("error", authResponse.getMessage());
            request.setAttribute("email", email); // Keep email for user convenience
            request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
        }
    }

    private boolean verifyRecaptcha(String token, String remoteIp) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        String secret = getServletContext().getInitParameter("RECAPTCHA_SECRET_KEY");
        if (secret == null || secret.trim().isEmpty()) {
            // If not configured, fail closed
            return false;
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            StringBuilder postData = new StringBuilder();
            postData.append("secret=").append(java.net.URLEncoder.encode(secret, java.nio.charset.StandardCharsets.UTF_8));
            postData.append("&response=").append(java.net.URLEncoder.encode(token, java.nio.charset.StandardCharsets.UTF_8));
            if (remoteIp != null) {
                postData.append("&remoteip=").append(java.net.URLEncoder.encode(remoteIp, java.nio.charset.StandardCharsets.UTF_8));
            }

            byte[] out = postData.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Content-Length", Integer.toString(out.length));
            try (java.io.OutputStream os = connection.getOutputStream()) {
                os.write(out);
            }

            int status = connection.getResponseCode();
            if (status != 200) {
                return false;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                String json = content.toString();
                return parseRecaptchaSuccess(json);
            }
        } catch (IOException e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private boolean parseRecaptchaSuccess(String json) {
        return json.contains("\"success\":true") || json.contains("\"success\": true");
    }

    /**
     * Redirect user to appropriate dashboard based on role
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param user Logged in user
     * @throws IOException if redirect fails
     */
    private void redirectToDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        String roleName = authService.getRoleName(user.getRoleID());
        String contextPath = request.getContextPath();

        switch (user.getRoleID()) {
            case filter.RoleConstants.ADMIN:
                response.sendRedirect(contextPath + "/admin/dashboard");
                break;
            case filter.RoleConstants.HR:
                response.sendRedirect(contextPath + "/hr/dashboard");
                break;
            case filter.RoleConstants.INVENTORY:
                response.sendRedirect(contextPath + "/inventory/dashboard");
                break;
            case filter.RoleConstants.BARISTA:
                response.sendRedirect(contextPath + "/barista/dashboard");
                break;
            case filter.RoleConstants.USER:
                response.sendRedirect(contextPath + "/user/shop?action=list");
                break;
            default:
                response.sendRedirect(contextPath + "/login");
                break;
        }
    }

    // ========== AUTH AUXILIARY METHODS ==========
    /**
     * Handle logout requests by invalidating the session and redirecting to
     * login page
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            // Clear shop access token verification when logging out
            session.removeAttribute("shopAccessTokenVerified");
            session.removeAttribute("shopAccessToken");
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    /**
     * Handle Google Sign-In POST flow
     */
    private void handleGoogleLoginPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String idToken = request.getParameter("credential");
        if (idToken == null || idToken.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing Google ID token");
            return;
        }

        String expectedClientId = getServletContext().getInitParameter("GOOGLE_CLIENT_ID");
        if (expectedClientId == null || expectedClientId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Server not configured for Google Sign-In");
            return;
        }

        GoogleTokenPayload payload = verifyIdToken(idToken);
        if (payload == null || !expectedClientId.trim().equals(payload.aud) || !payload.isEmailVerified()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Google ID token or email not verified");
            return;
        }

        String email = payload.email;
        String fullName = payload.name != null ? payload.name : payload.givenName;

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Account not found. Please contact administrator to create your account first.");
            return;
        }
        
        if (!user.isActive()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Account is inactive. Please contact administrator.");
            return;
        }

        createUserSession(request, user);
        redirectToDashboard(request, response, user);
    }

    private GoogleTokenPayload verifyIdToken(String idToken) {
        final String TOKENINFO_ENDPOINT = "https://oauth2.googleapis.com/tokeninfo?id_token=";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(TOKENINFO_ENDPOINT + idToken);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            int status = connection.getResponseCode();
            if (status != 200) {
                return null;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                String json = content.toString();
                return GoogleTokenPayload.fromJson(json);
            }
        } catch (IOException e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static class GoogleTokenPayload {

        String aud;
        String email;
        String emailVerified;
        String name;
        String givenName;

        static GoogleTokenPayload fromJson(String json) {
            GoogleTokenPayload p = new GoogleTokenPayload();
            p.aud = extract(json, "\\\"aud\\\"\\s*:\\s*\\\"", "\\\"");
            p.email = extract(json, "\\\"email\\\"\\s*:\\s*\\\"", "\\\"");
            String rawEmailVerified = extractFlexible(json, "\\\"email_verified\\\"\\s*:\\s*", ",", "}");
            p.emailVerified = rawEmailVerified != null ? rawEmailVerified.trim() : null;
            p.name = extract(json, "\\\"name\\\"\\s*:\\s*\\\"", "\\\"");
            p.givenName = extract(json, "\\\"given_name\\\"\\s*:\\s*\\\"", "\\\"");
            return p;
        }

        private static String extract(String json, String startRegex, String endMarker) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(startRegex);
            java.util.regex.Matcher matcher = pattern.matcher(json);
            if (matcher.find()) {
                int start = matcher.end();
                int end = json.indexOf(endMarker, start);
                if (end >= 0) {
                    return json.substring(start, end);
                }
                return json.substring(start);
            }
            return null;
        }

        private static String extractFlexible(String json, String startLiteralRegex, String... terminators) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(startLiteralRegex);
            java.util.regex.Matcher matcher = pattern.matcher(json);
            if (!matcher.find()) {
                return null;
            }
            int start = matcher.end();
            int end = json.length();
            for (String term : terminators) {
                int idx = json.indexOf(term, start);
                if (idx >= 0) {
                    end = Math.min(end, idx);
                }
            }
            String value = json.substring(start, end).trim();
            if (value.startsWith("\"")) {
                value = value.substring(1);
            }
            while (value.endsWith(",") || value.endsWith("}") || value.endsWith("\"")) {
                value = value.substring(0, value.length() - 1).trim();
            }
            return value;
        }

        boolean isEmailVerified() {
            if (emailVerified == null) {
                return false;
            }
            String v = emailVerified.trim();
            if (v.startsWith("\"") && v.endsWith("\"")) {
                v = v.substring(1, v.length() - 1).trim();
            }
            return "true".equalsIgnoreCase(v);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Login and Profile Controller for Coffee Shop Management System";
    }

    // ========== PROFILE MANAGEMENT METHODS ==========
    /**
     * Handle profile GET requests
     */
    private void handleProfileGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        try {
            if ("edit".equals(action)) {
                showEditProfile(request, response, currentUser);
            } else if ("change-password".equals(action)) {
                showChangePassword(request, response, currentUser);
            } else {
                showProfile(request, response, currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }

    /**
     * Handle profile POST requests
     */
    private void handleProfilePost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        try {
            if ("update-profile".equals(action)) {
                handleUpdateProfile(request, response, currentUser);
            } else if ("change-password".equals(action)) {
                handleChangePassword(request, response, currentUser);
            } else {
                showProfile(request, response, currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            showProfile(request, response, currentUser);
        }
    }

    /**
     * Show user profile view
     */
    private void showProfile(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        // Get latest user data from database
        try {
            User latestUser = userDAO.getUserById(user.getUserID());
            if (latestUser != null) {
                request.setAttribute("profileUser", latestUser);
            } else {
                request.setAttribute("profileUser", user);
            }
        } catch (Exception e) {
            request.setAttribute("profileUser", user);
            request.setAttribute("warning", "Không thể tải thông tin mới nhất từ cơ sở dữ liệu");
        }

        request.getRequestDispatcher("/WEB-INF/views/common/profile-view.jsp").forward(request, response);
    }

    /**
     * Show edit profile form
     */
    private void showEditProfile(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        // Get latest user data from database
        try {
            User latestUser = userDAO.getUserById(user.getUserID());
            if (latestUser != null) {
                request.setAttribute("profileUser", latestUser);
            } else {
                request.setAttribute("profileUser", user);
            }
        } catch (Exception e) {
            request.setAttribute("profileUser", user);
            request.setAttribute("warning", "Không thể tải thông tin mới nhất từ cơ sở dữ liệu");
        }

        request.getRequestDispatcher("/WEB-INF/views/common/profile-edit.jsp").forward(request, response);
    }

    /**
     * Show change password form
     */
    private void showChangePassword(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {

        request.setAttribute("profileUser", user);
        request.getRequestDispatcher("/WEB-INF/views/common/profile-change-password.jsp").forward(request, response);
    }

    /**
     * Handle profile update
     */
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Validate input
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Họ tên không được để trống");
            showEditProfile(request, response, currentUser);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email không được để trống");
            showEditProfile(request, response, currentUser);
            return;
        }

        // Check if email is already used by another user
        try {
            User existingUser = userDAO.getUserByEmail(email.trim());
            if (existingUser != null && existingUser.getUserID() != currentUser.getUserID()) {
                request.setAttribute("error", "Email này đã được sử dụng bởi người khác");
                showEditProfile(request, response, currentUser);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi kiểm tra email: " + e.getMessage());
            showEditProfile(request, response, currentUser);
            return;
        }

        // Handle avatar upload
        String avatarUrl = currentUser.getAvatarUrl(); // Keep current avatar by default
        String removeAvatar = request.getParameter("removeAvatar");
        
        try {
            // Check if user wants to remove avatar
            if ("true".equals(removeAvatar)) {
                // Delete current avatar file if exists
                if (avatarUrl != null && !avatarUrl.isEmpty()) {
                    deleteAvatarFile(request, avatarUrl);
                }
                avatarUrl = null;
            } else {
                // Handle new avatar upload
                Part avatarPart = request.getPart("avatarFile");
                if (avatarPart != null && avatarPart.getSize() > 0) {
                    // Delete old avatar file if exists
                    if (avatarUrl != null && !avatarUrl.isEmpty()) {
                        deleteAvatarFile(request, avatarUrl);
                    }
                    
                    // Upload new avatar
                    avatarUrl = uploadAvatarFile(request, avatarPart, currentUser.getUserID());
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi xử lý ảnh đại diện: " + e.getMessage());
            showEditProfile(request, response, currentUser);
            return;
        }

        // Update user data
        try {
            String gender = request.getParameter("gender");

            User updatedUser = new User();
            updatedUser.setUserID(currentUser.getUserID());
            updatedUser.setFullName(fullName.trim());
            updatedUser.setEmail(email.trim());
            updatedUser.setPhone(phone != null ? phone.trim() : "");
            updatedUser.setGender(gender != null ? gender.trim() : currentUser.getGender());
            updatedUser.setAddress(address != null ? address.trim() : "");
            updatedUser.setAvatarUrl(avatarUrl);
            updatedUser.setRoleID(currentUser.getRoleID());
            updatedUser.setActive(currentUser.isActive());
            updatedUser.setPasswordHash(currentUser.getPasswordHash());

            boolean success = userDAO.updateUser(updatedUser);

            if (success) {
                // Update session with new user data
                HttpSession session = request.getSession();
                session.setAttribute("user", updatedUser);

                request.setAttribute("success", "Cập nhật thông tin thành công!");
                showProfile(request, response, updatedUser);
            } else {
                request.setAttribute("error", "Cập nhật thông tin thất bại");
                showEditProfile(request, response, currentUser);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi cập nhật: " + e.getMessage());
            showEditProfile(request, response, currentUser);
        }
    }

    /**
     * Handle password change
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate input
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu hiện tại");
            showChangePassword(request, response, currentUser);
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mật khẩu mới");
            showChangePassword(request, response, currentUser);
            return;
        }

        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự");
            showChangePassword(request, response, currentUser);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Xác nhận mật khẩu không khớp");
            showChangePassword(request, response, currentUser);
            return;
        }

        try {
            // Verify current password
            boolean isCurrentPasswordValid = userService.verifyPassword(currentPassword, currentUser.getPasswordHash());

            if (!isCurrentPasswordValid) {
                request.setAttribute("error", "Mật khẩu hiện tại không đúng");
                showChangePassword(request, response, currentUser);
                return;
            }

            // Hash new password
            String newPasswordHash = userService.hashPassword(newPassword);

            // Update password in database
            boolean success = userDAO.updateUserPassword(currentUser.getUserID(), newPasswordHash);

            if (success) {
                // Update session
                currentUser.setPasswordHash(newPasswordHash);
                HttpSession session = request.getSession();
                session.setAttribute("user", currentUser);

                request.setAttribute("success", "Đổi mật khẩu thành công!");
                showProfile(request, response, currentUser);
            } else {
                request.setAttribute("error", "Đổi mật khẩu thất bại");
                showChangePassword(request, response, currentUser);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi đổi mật khẩu: " + e.getMessage());
            showChangePassword(request, response, currentUser);
        }
    }
    
    /**
     * Create user session with all necessary attributes
     */
    private void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getUserID());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("roleId", user.getRoleID());
        
        String roleName = authService.getRoleName(user.getRoleID());
        session.setAttribute("roleName", roleName);
        
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
    }
    
    /**
     * Upload avatar file and return the URL
     */
    private String uploadAvatarFile(HttpServletRequest request, Part avatarPart, int userId) throws IOException {
        // Get the upload directory - save to dist/img
        String uploadDir = request.getServletContext().getRealPath("/dist/img");
        File uploadDirFile = new File(uploadDir);
        
        // Create directory if it doesn't exist
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        
        // Get file extension
        String fileName = avatarPart.getSubmittedFileName();
        String fileExtension = "";
        if (fileName != null && fileName.contains(".")) {
            fileExtension = fileName.substring(fileName.lastIndexOf("."));
        }
        
        // Generate unique filename
        String uniqueFileName = "avatar_" + userId + "_" + System.currentTimeMillis() + fileExtension;
        
        // Save file
        File file = new File(uploadDirFile, uniqueFileName);
        try (InputStream inputStream = avatarPart.getInputStream()) {
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
        // Return the URL path
        return "/dist/img/" + uniqueFileName;
    }
    
    /**
     * Delete avatar file
     */
    private void deleteAvatarFile(HttpServletRequest request, String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            try {
                String filePath = request.getServletContext().getRealPath(avatarUrl);
                File file = new File(filePath);
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {

                    } else {
                        System.err.println("Failed to delete avatar file: " + avatarUrl);
                    }
                }
            } catch (Exception e) {
                // Log error but don't throw exception
                System.err.println("Error deleting avatar file: " + e.getMessage());
            }
        }
    }
}
