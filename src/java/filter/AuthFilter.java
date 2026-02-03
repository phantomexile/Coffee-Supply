package filter;

import model.User;

/**
 * Filter class for authorization checks
 */
public class AuthFilter {
    
    /**
     * Check if user has permission to access a specific path
     * @param user Current user
     * @param requestPath Request path (e.g., "/admin/dashboard")
     * @return true if authorized, false otherwise
     */
    public static boolean isAuthorized(User user, String requestPath) {
        if (user == null || requestPath == null) {
            return false;
        }
        
        int roleId = user.getRoleID();
        String path = requestPath.toLowerCase();
        
        // Admin has access to everything
        if (roleId == RoleConstants.ADMIN) {
            return true;
        }
        
        // Common paths accessible to all authenticated users
        if (isCommonPath(path)) {
            return true;
        }
        
        // Role-specific authorization
        switch (roleId) {
            case RoleConstants.HR:
                return isHRAuthorized(path);
                
            case RoleConstants.INVENTORY:
                return isInventoryAuthorized(path);
                
            case RoleConstants.BARISTA:
                return isBaristaAuthorized(path);
                
            case RoleConstants.USER:
                return isUserAuthorized(path);
                
            default:
                return false;
        }
    }
    
    /**
     * Check if path is accessible to all authenticated users
     */
    private static boolean isCommonPath(String path) {
        return path.startsWith("/profile") || 
               path.startsWith("/dashboard") ||
               path.startsWith("/setting") ||
               path.startsWith("/logout") ||
               path.contains("/css/") ||
               path.contains("/js/") ||
               path.contains("/images/") ||
               path.contains("/bootstrap/") ||
               path.contains("/fonts/");
    }
    
    /**
     * Check HR authorization
     */
    private static boolean isHRAuthorized(String path) {
        return path.startsWith("/hr/") || 
               path.startsWith("/user") ||
               isCommonPath(path);
    }
    
    /**
     * Check Inventory authorization  
     */
    private static boolean isInventoryAuthorized(String path) {
        return path.startsWith("/inventory/") ||
               path.startsWith("/ingredient") ||
               path.startsWith("/purchase-order") ||
               path.startsWith("/issue") ||
               path.startsWith("/supplier") ||
               isCommonPath(path);
    }
    
    /**
     * Check Barista authorization
     */
    private static boolean isBaristaAuthorized(String path) {
        return path.startsWith("/barista/") ||
               path.startsWith("/order") ||
               isCommonPath(path);
    }
    
    /**
     * Check User authorization
     */
    private static boolean isUserAuthorized(String path) {
        return path.startsWith("/user/shop") ||
               isCommonPath(path);
    }
    
    /**
     * Get appropriate redirect URL based on user role
     * @param user Current user
     * @return Redirect URL
     */
    public static String getDefaultDashboard(User user) {
        if (user == null) {
            return "/login";
        }
        
        switch (user.getRoleID()) {
            case RoleConstants.ADMIN:
                return "/admin/dashboard";
            case RoleConstants.HR:
                return "/hr/dashboard";
            case RoleConstants.INVENTORY:
                return "/inventory/dashboard";
            case RoleConstants.BARISTA:
                return "/barista/dashboard";
            case RoleConstants.USER:
                return "/user/shop?action=list";
            default:
                return "/login";
        }
    }
    
    /**
     * Check if user has administrative privileges
     */
    public static boolean isAdmin(User user) {
        return user != null && user.getRoleID() == RoleConstants.ADMIN;
    }
    
    /**
     * Check if user has HR privileges
     */
    public static boolean isHR(User user) {
        return user != null && user.getRoleID() == RoleConstants.HR;
    }
    
    /**
     * Check if user has Inventory privileges
     */
    public static boolean isInventory(User user) {
        return user != null && user.getRoleID() == RoleConstants.INVENTORY;
    }
    
    /**
     * Check if user has Barista privileges
     */
    public static boolean isBarista(User user) {
        return user != null && user.getRoleID() == RoleConstants.BARISTA;
    }
    
    /**
     * Check if user is regular user
     */
    public static boolean isUser(User user) {
        return user != null && user.getRoleID() == RoleConstants.USER;
    }
}
