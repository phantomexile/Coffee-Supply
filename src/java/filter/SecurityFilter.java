package filter;

import model.User;

/**
 * Security filter methods for controllers
 * Static methods that can be used in any controller for authorization
 */
public class SecurityFilter {
    
    /**
     * Validate user access to resource
     * @param user Current user
     * @param requiredRoleId Required role ID for access
     * @return true if user has access, false otherwise
     */
    public static boolean hasAccess(User user, int requiredRoleId) {
        if (user == null) {
            return false;
        }
        
        if (user.getRoleID() == RoleConstants.ADMIN) {
            return true;
        }
        
        // Check specific role
        return user.getRoleID() == requiredRoleId;
    }
    
    /**
     * Check if user has any of the specified roles
     * @param user Current user
     * @param allowedRoles Array of allowed role IDs
     * @return true if user has any of the roles, false otherwise
     */
    public static boolean hasAnyRole(User user, int... allowedRoles) {
        if (user == null) {
            return false;
        }
        
        if (user.getRoleID() == RoleConstants.ADMIN) {
            return true;
        }
        
        // Check if user has any of the allowed roles
        for (int roleId : allowedRoles) {
            if (user.getRoleID() == roleId) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if user is admin
     */
    public static boolean isAdmin(User user) {
        return user != null && user.getRoleID() == RoleConstants.ADMIN;
    }
    
    /**
     * Check if user is HR
     */
    public static boolean isHR(User user) {
        return user != null && user.getRoleID() == RoleConstants.HR;
    }
    
    /**
     * Check if user is Inventory
     */
    public static boolean isInventory(User user) {
        return user != null && user.getRoleID() == RoleConstants.INVENTORY;
    }
    
    /**
     * Check if user is Barista
     */
    public static boolean isBarista(User user) {
        return user != null && user.getRoleID() == RoleConstants.BARISTA;
    }
    
    /**
     * Check if user is regular User
     */
    public static boolean isUser(User user) {
        return user != null && user.getRoleID() == RoleConstants.USER;
    }
    
    /**
     * Validate user session and role
     * @param user Current user from session
     * @param requiredRoleId Required role for access
     * @param errorMessage Error message if access denied
     * @return Validation result with success flag and message
     */
    public static ValidationResult validateAccess(User user, int requiredRoleId, String errorMessage) {
        if (user == null) {
            return new ValidationResult(false, "Vui lòng đăng nhập để tiếp tục.");
        }
        
        if (!hasAccess(user, requiredRoleId)) {
            return new ValidationResult(false, errorMessage != null ? errorMessage : 
                "Bạn không có quyền truy cập chức năng này.");
        }
        
        return new ValidationResult(true, null);
    }
    
    /**
     * Simple validation result class
     */
    public static class ValidationResult {
        private final boolean success;
        private final String message;
        
        public ValidationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
