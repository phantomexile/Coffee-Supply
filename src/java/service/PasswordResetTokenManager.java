/*
 * Password Reset Token Manager - In-memory token storage
 */
package service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for password reset tokens (stored in memory)
 * @author DrDYNew
 */
public class PasswordResetTokenManager {
    
    // Token storage: token -> TokenData
    private static final Map<String, TokenData> tokenStore = new ConcurrentHashMap<>();
    
    // Token expiration time in hours
    private static final int EXPIRATION_HOURS = 2;
    
    /**
     * Inner class to store token data
     */
    private static class TokenData {
        private final String email;
        private final LocalDateTime expiryTime;
        private boolean used;
        
        public TokenData(String email, LocalDateTime expiryTime) {
            this.email = email;
            this.expiryTime = expiryTime;
            this.used = false;
        }
        
        public String getEmail() {
            return email;
        }
        
        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
        
        public boolean isUsed() {
            return used;
        }
        
        public void setUsed(boolean used) {
            this.used = used;
        }
        
        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }
    
    /**
     * Generate a new password reset token
     * @param email User's email
     * @return Generated token
     */
    public static String generateToken(String email) {
        // Clean up expired tokens before generating new one
        cleanupExpiredTokens();
        
        // Generate unique token
        String token = UUID.randomUUID().toString();
        
        // Calculate expiry time (2 hours from now)
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        
        // Store token
        tokenStore.put(token, new TokenData(email, expiryTime));
        
        System.out.println("Token generated for email: " + email + ", expires at: " + expiryTime);
        
        return token;
    }
    
    /**
     * Validate token and get associated email
     * @param token Token to validate
     * @return Email if token is valid, null otherwise
     */
    public static String validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        
        TokenData tokenData = tokenStore.get(token);
        
        if (tokenData == null) {
            System.out.println("Token not found: " + token);
            return null;
        }
        
        if (tokenData.isUsed()) {
            System.out.println("Token already used: " + token);
            return null;
        }
        
        if (tokenData.isExpired()) {
            System.out.println("Token expired: " + token);
            tokenStore.remove(token); // Remove expired token
            return null;
        }
        
        return tokenData.getEmail();
    }
    
    /**
     * Mark token as used
     * @param token Token to mark as used
     * @return true if successful
     */
    public static boolean markTokenAsUsed(String token) {
        TokenData tokenData = tokenStore.get(token);
        
        if (tokenData != null && !tokenData.isUsed() && !tokenData.isExpired()) {
            tokenData.setUsed(true);
            System.out.println("Token marked as used: " + token);
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove a token from storage
     * @param token Token to remove
     */
    public static void removeToken(String token) {
        tokenStore.remove(token);
        System.out.println("Token removed: " + token);
    }
    
    /**
     * Clean up expired tokens
     */
    private static void cleanupExpiredTokens() {
        tokenStore.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    /**
     * Get token count (for debugging)
     * @return Number of tokens in storage
     */
    public static int getTokenCount() {
        cleanupExpiredTokens();
        return tokenStore.size();
    }
}
