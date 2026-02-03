package model;


public class AuthResponse {
    private boolean success;
    private String message;
    private User user;
    private String roleName;
    
    // Default constructor
    public AuthResponse() {
    }
    
    // Constructor for success response
    public AuthResponse(boolean success, String message, User user, String roleName) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.roleName = roleName;
    }
    
    // Constructor for error response
    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.user = null;
        this.roleName = null;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}