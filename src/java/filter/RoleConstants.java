package filter;

/**
 * Constants for user roles in the system
 * These correspond to SettingID values in the Setting table where Type='Role'
 */
public class RoleConstants {
    
    // Role IDs from database
    public static final int HR = 1;
    public static final int ADMIN = 2;
    public static final int INVENTORY = 3;
    public static final int BARISTA = 4;
    public static final int USER = 5;
    
    // Role names (case-insensitive)
    public static final String HR_NAME = "HR";
    public static final String ADMIN_NAME = "Admin";
    public static final String INVENTORY_NAME = "Inventory";
    public static final String BARISTA_NAME = "Barista";
    public static final String USER_NAME = "User";
    
    /**
     * Check if role ID is valid
     * @param roleId Role ID to check
     * @return true if valid, false otherwise
     */
    public static boolean isValidRole(int roleId) {
        return roleId == HR || roleId == ADMIN || roleId == INVENTORY || 
               roleId == BARISTA || roleId == USER;
    }
    
    /**
     * Get role name by ID
     * @param roleId Role ID
     * @return Role name or null if invalid
     */
    public static String getRoleName(int roleId) {
        switch (roleId) {
            case HR: return HR_NAME;
            case ADMIN: return ADMIN_NAME;
            case INVENTORY: return INVENTORY_NAME;
            case BARISTA: return BARISTA_NAME;
            case USER: return USER_NAME;
            default: return null;
        }
    }
    
    /**
     * Get role ID by name (case-insensitive)
     * @param roleName Role name
     * @return Role ID or -1 if invalid
     */
    public static int getRoleId(String roleName) {
        if (roleName == null) return -1;
        
        switch (roleName.toLowerCase()) {
            case "hr": return HR;
            case "admin": return ADMIN;
            case "inventory": return INVENTORY;
            case "barista": return BARISTA;
            case "user": return USER;
            default: return -1;
        }
    }
}
