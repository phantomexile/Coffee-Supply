/*
 * Shop Access Token Manager - Quản lý token xác thực để xem shop
 */
package service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager cho shop access tokens (lưu trữ trong memory)
 * User cần nhập token để xem được danh sách shop của mình
 */
public class ShopAccessTokenManager {
    
    // Token storage: token -> TokenData
    private static final Map<String, TokenData> tokenStore = new ConcurrentHashMap<>();
    
    // Token expiration time in hours
    private static final int EXPIRATION_HOURS = 24; // Token hết hạn sau 24 giờ
    
    /**
     * Inner class to store token data
     */
    private static class TokenData {
        private final Integer userId;
        private final Integer shopId;
        private final LocalDateTime expiryTime;
        private final LocalDateTime createdTime;
        
        public TokenData(Integer userId, Integer shopId, LocalDateTime expiryTime) {
            this.userId = userId;
            this.shopId = shopId;
            this.expiryTime = expiryTime;
            this.createdTime = LocalDateTime.now();
        }
        
        public Integer getUserId() {
            return userId;
        }
        
        public Integer getShopId() {
            return shopId;
        }
        
        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
        
        public LocalDateTime getCreatedTime() {
            return createdTime;
        }
        
        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiryTime);
        }
    }
    
    /**
     * Generate a new shop access token for specific shop
     * @param userId User's ID
     * @param shopId Shop's ID
     * @return Generated token
     */
    public static String generateToken(Integer userId, Integer shopId) {
        // Clean up expired tokens before generating new one
        cleanupExpiredTokens();
        
        // Revoke existing token for this specific shop (one token per shop)
        revokeShopToken(userId, shopId);
        
        // Generate unique token (8 characters for easier input)
        String token = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Make sure token is unique
        while (tokenStore.containsKey(token)) {
            token = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        
        // Calculate expiry time
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        
        // Store token with shopId
        tokenStore.put(token, new TokenData(userId, shopId, expiryTime));
        

        
        return token;
    }
    
    /**
     * Validate token for specific shop access
     * @param token Token to validate
     * @param userId User ID to match against token
     * @param shopId Shop ID to match against token
     * @return true if token is valid for this user and shop, false otherwise
     */
    public static boolean validateToken(String token, Integer userId, Integer shopId) {
        if (token == null || token.trim().isEmpty() || userId == null || shopId == null) {
            return false;
        }
        
        // Convert to uppercase for case-insensitive comparison
        token = token.trim().toUpperCase();
        
        TokenData tokenData = tokenStore.get(token);
        
        if (tokenData == null) {
            return false;
        }
        
        if (tokenData.isExpired()) {
            tokenStore.remove(token); // Remove expired token
            return false;
        }
        
        if (!tokenData.getUserId().equals(userId)) {
            return false;
        }
        
        if (!tokenData.getShopId().equals(shopId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validate token (backward compatibility - checks user only)
     * @param token Token to validate
     * @param userId User ID to match against token
     * @return true if token is valid for this user, false otherwise
     */
    public static boolean validateToken(String token, Integer userId) {
        if (token == null || token.trim().isEmpty() || userId == null) {
            return false;
        }
        
        // Convert to uppercase for case-insensitive comparison
        token = token.trim().toUpperCase();
        
        TokenData tokenData = tokenStore.get(token);
        
        if (tokenData == null) {
            return false;
        }
        
        if (tokenData.isExpired()) {
            tokenStore.remove(token); // Remove expired token
            return false;
        }
        
        if (!tokenData.getUserId().equals(userId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if user has any active tokens
     * @param userId User ID to check
     * @return true if user has active tokens
     */
    public static boolean hasActiveToken(Integer userId) {
        if (userId == null) {
            return false;
        }
        
        cleanupExpiredTokens();
        
        return tokenStore.values().stream()
                .anyMatch(tokenData -> userId.equals(tokenData.getUserId()) && !tokenData.isExpired());
    }
    
    /**
     * Revoke all tokens for a specific user
     * @param userId User ID
     */
    public static void revokeUserTokens(Integer userId) {
        if (userId == null) {
            return;
        }
        
        tokenStore.entrySet().removeIf(entry -> 
            userId.equals(entry.getValue().getUserId()));
        

    }
    
    /**
     * Revoke token for a specific shop
     * @param userId User ID
     * @param shopId Shop ID
     */
    public static void revokeShopToken(Integer userId, Integer shopId) {
        if (userId == null || shopId == null) {
            return;
        }
        
        tokenStore.entrySet().removeIf(entry -> {
            TokenData tokenData = entry.getValue();
            return userId.equals(tokenData.getUserId()) && shopId.equals(tokenData.getShopId());
        });
    }
    
    /**
     * Remove a specific token from storage
     * @param token Token to remove
     */
    public static void removeToken(String token) {
        if (token != null) {
            token = token.trim().toUpperCase();
            tokenStore.remove(token);

        }
    }
    
    /**
     * Clean up expired tokens
     */
    private static void cleanupExpiredTokens() {
        int removedCount = 0;
        
        var iterator = tokenStore.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                removedCount++;
            }
        }
        
        if (removedCount > 0) {

        }
    }
    
    /**
     * Get token count
     * @return Number of tokens in storage
     */
    public static int getTokenCount() {
        cleanupExpiredTokens();
        return tokenStore.size();
    }
    
    /**
     * Get token info
     * @param token Token to check
     * @return Token info string
     */
    public static String getTokenInfo(String token) {
        if (token == null) {
            return "Token is null";
        }
        
        token = token.trim().toUpperCase();
        TokenData tokenData = tokenStore.get(token);
        
        if (tokenData == null) {
            return "Token not found: " + token;
        }
        
        return "Token: " + token + 
               ", User ID: " + tokenData.getUserId() + 
               ", Created: " + tokenData.getCreatedTime() + 
               ", Expires: " + tokenData.getExpiryTime() + 
               ", Expired: " + tokenData.isExpired();
    }
}