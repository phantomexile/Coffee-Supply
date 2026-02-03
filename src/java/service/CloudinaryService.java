package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * Service for uploading images to Cloudinary
 */
public class CloudinaryService {
    
    // Cloudinary configuration - replace with your actual values
    private static final String CLOUD_NAME = "dw6sqkfxo";
    private static final String API_KEY = "744626196532751";
    private static final String API_SECRET = "JXZGbtuR6qlPMVgpNprmzCwo5yU";
    private static final String UPLOAD_URL = "https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/upload";
    
    // Maximum file size (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    
    // Allowed image extensions
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
    
        /**
     * Upload shop image to Cloudinary
     * @param inputStream Input stream of the image file
     * @param fileName Original filename of the image
     * @param shopId ID of the shop
     * @return URL of the uploaded image or null if failed
     */
    public static String uploadShopImage(InputStream inputStream, String fileName, int shopId) throws IOException {
        if (inputStream == null) {
            throw new IOException("Input stream cannot be null");
        }
        
        // Validate file type from filename
        if (fileName != null && !isValidImageFile(fileName)) {
            System.err.println("Invalid file type detected. FileName: '" + fileName + "'");
            throw new IOException("Invalid file type. Only JPG, PNG, GIF are allowed.");
        }
        
        try {
            // Convert to base64
            String base64Image = encodeImageToBase64(inputStream);
            
            // Create unique public ID for the image
            String publicId = "shop_" + shopId + "_" + System.currentTimeMillis();
            
            // Upload to Cloudinary
            return uploadToCloudinary(base64Image, publicId);
            
        } catch (Exception e) {
            System.err.println("Error uploading to Cloudinary: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to upload image to Cloudinary: " + e.getMessage());
        }
    }
    
    /**
     * Delete image from Cloudinary
     * @param imageUrl The Cloudinary URL to extract public_id from
     * @return true if deleted successfully, false otherwise
     */
    public static boolean deleteShopImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty() || !imageUrl.contains("cloudinary.com")) {
            return false;
        }
        
        try {
            // Extract public_id from Cloudinary URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId == null) {
                return false;
            }
            
            // Create deletion request
            URL url = new URL("https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/destroy");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            // Create authentication
            String auth = API_KEY + ":" + API_SECRET;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            
            // Prepare parameters
            String params = "public_id=" + URLEncoder.encode(publicId, StandardCharsets.UTF_8.toString()) +
                           "&timestamp=" + (System.currentTimeMillis() / 1000);
            
            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(params.getBytes(StandardCharsets.UTF_8));
            }
            
            // Check response
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Successfully deleted image from Cloudinary: " + publicId);
                return true;
            } else {
                System.err.println("Failed to delete image from Cloudinary. Response code: " + responseCode);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error deleting image from Cloudinary: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get default shop image URL
     */
    public static String getDefaultShopImage() {
        return "https://res.cloudinary.com/" + CLOUD_NAME + "/image/upload/v1/shops/default-shop.png";
    }
    
        /**
     * Convert image to base64 string
     */
    private static String encodeImageToBase64(InputStream inputStream) throws IOException {
        try {
            byte[] imageBytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
    
    /**
     * Upload image to Cloudinary with signed upload
     */
    private static String uploadToCloudinary(String base64Image, String publicId) throws IOException {
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        
        // Create upload parameters (without file for signature)
        long timestamp = System.currentTimeMillis() / 1000;
        Map<String, String> signatureParams = new TreeMap<>(); // TreeMap for consistent ordering
        signatureParams.put("folder", "shops");
        signatureParams.put("public_id", publicId);
        signatureParams.put("timestamp", String.valueOf(timestamp));
        
        // Generate signature
        String signature = generateSignature(signatureParams, API_SECRET);
        
        // Create all parameters including file
        Map<String, String> allParams = new TreeMap<>(signatureParams);
        allParams.put("api_key", API_KEY);
        allParams.put("signature", signature);
        allParams.put("file", "data:image/jpeg;base64," + base64Image);
        
        // Build parameters string
        StringBuilder paramBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            if (paramBuilder.length() > 0) {
                paramBuilder.append("&");
            }
            paramBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                       .append("=")
                       .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
        }
        
        // Send request
        try (OutputStream os = connection.getOutputStream()) {
            os.write(paramBuilder.toString().getBytes(StandardCharsets.UTF_8));
        }
        
        // Read response
        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                
                // Extract secure_url from JSON response
                String jsonResponse = response.toString();
                return extractSecureUrlFromResponse(jsonResponse);
            }
        } else {
            // Read error response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                throw new IOException("Cloudinary upload failed: " + errorResponse.toString());
            }
        }
    }
    
    /**
     * Extract secure_url from Cloudinary JSON response
     */
    private static String extractSecureUrlFromResponse(String jsonResponse) {
        // Simple JSON parsing to extract secure_url
        String searchFor = "\"secure_url\":\"";
        int startIndex = jsonResponse.indexOf(searchFor);
        if (startIndex != -1) {
            startIndex += searchFor.length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return jsonResponse.substring(startIndex, endIndex).replace("\\/", "/");
            }
        }
        throw new RuntimeException("Could not extract secure_url from Cloudinary response");
    }
    
    /**
     * Extract public_id from Cloudinary URL
     */
    private static String extractPublicIdFromUrl(String imageUrl) {
        try {
            // Cloudinary URL format: https://res.cloudinary.com/{cloud_name}/image/upload/{version}/{public_id}.{format}
            if (imageUrl.contains("/upload/")) {
                String afterUpload = imageUrl.substring(imageUrl.indexOf("/upload/") + 8);
                // Remove version if exists (starts with v followed by digits)
                if (afterUpload.matches("^v\\d+/.*")) {
                    afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
                }
                // Remove file extension
                int lastDotIndex = afterUpload.lastIndexOf(".");
                if (lastDotIndex != -1) {
                    return afterUpload.substring(0, lastDotIndex);
                }
                return afterUpload;
            }
        } catch (Exception e) {
            System.err.println("Error extracting public_id from URL: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Check if file is a valid image type
     */
    private static boolean isValidImageFile(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        
        // Extract just the filename from path (handle Windows and Unix paths)
        String baseName = fileName;
        int lastSlash = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (lastSlash != -1) {
            baseName = fileName.substring(lastSlash + 1);
        }
        
        int dotIndex = baseName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == baseName.length() - 1) {
            return false; // No extension or extension is empty
        }
        
        String extension = baseName.substring(dotIndex + 1).toLowerCase();
        return isValidImageExtension(extension);
    }
    
    /**
     * Check if file extension is valid for images
     */
    private static boolean isValidImageExtension(String extension) {
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Generate Cloudinary API signature
     */
    private static String generateSignature(Map<String, String> params, String apiSecret) {
        try {
            // Build parameters string (exclude api_key, signature, and file)
            StringBuilder paramString = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                if (!"api_key".equals(key) && !"signature".equals(key) && !"file".equals(key)) {
                    if (paramString.length() > 0) {
                        paramString.append("&");
                    }
                    paramString.append(key).append("=").append(entry.getValue());
                }
            }
            
            // Add API secret at the end (no & before secret)
            paramString.append(apiSecret);
            
            // Generate SHA-1 hash
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(paramString.toString().getBytes(StandardCharsets.UTF_8));
            
            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }
}