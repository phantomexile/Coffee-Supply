package service;

import dao.AuthDAO;
import model.AuthResponse;
import model.User;

public class AuthService {

    private AuthDAO authDAO;

    public AuthService() {
        this.authDAO = new AuthDAO();
    }

    /**
     * Login user with email and password
     *
     * @param email User email
     * @param password Plain text password
     * @return AuthResponse with result
     */
    public AuthResponse login(String email, String password) {
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            return new AuthResponse(false, "Email không được để trống");
        }

        if (password == null || password.trim().isEmpty()) {
            return new AuthResponse(false, "Mật khẩu không được để trống");
        }

        // Validate email format
        if (!isValidEmail(email)) {
            return new AuthResponse(false, "Email không đúng định dạng");
        }

        // Authenticate user
        return authDAO.authenticateUser(email.trim(), password);
    }

    /**
     * Get user by ID with role information
     *
     * @param userID User ID
     * @return User object, null if not found
     */
    public User getUserById(int userID) {
        return authDAO.getUserByIdWithRole(userID);
    }

    /**
     * Get role name by role ID
     *
     * @param roleID Role ID
     * @return Role name, null if not found
     */
    public String getRoleName(int roleID) {
        return authDAO.getRoleName(roleID);
    }

    /**
     * Hash password using bcrypt
     *
     * @param password Plain text password
     * @return Hashed password
     */
    public String hashPassword(String password) {
        return authDAO.hashPassword(password);
    }

    /**
     * Verify if password matches the hash
     *
     * @param password Plain text password
     * @param hash Stored password hash
     * @return true if password matches, false otherwise
     */
    public boolean verifyPassword(String password, String hash) {
        return authDAO.verifyPassword(password, hash);
    }

    /**
     * Validate email format
     *
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Check if user has specific role
     *
     * @param user User object
     * @param roleName Role name to check
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(User user, String roleName) {
        if (user == null || roleName == null) {
            return false;
        }

        String userRoleName = getRoleName(user.getRoleID());
        return roleName.equalsIgnoreCase(userRoleName);
    }

    /**
     * Check if user is admin
     *
     * @param user User object
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin(User user) {
        return hasRole(user, "Admin");
    }

    /**
     * Check if user is HR
     *
     * @param user User object
     * @return true if user is HR, false otherwise
     */
    public boolean isHR(User user) {
        return hasRole(user, "HR");
    }

    /**
     * Check if user is Inventory staff
     *
     * @param user User object
     * @return true if user is Inventory staff, false otherwise
     */
    public boolean isInventoryStaff(User user) {
        return hasRole(user, "Inventory");
    }

    /**
     * Check if user is Barista
     *
     * @param user User object
     * @return true if user is Barista, false otherwise
     */
    public boolean isBarista(User user) {
        return hasRole(user, "Barista");
    }

    public boolean isUser(User user) {
        return hasRole(user, "User");
    }
}
