package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class GoogleAuthService {
    
    // Google OAuth2 credentials
    private static final String GOOGLE_CLIENT_ID = "799302298655-gkrcm1ldkir2obf2tuecgu6vjd4618ks.apps.googleusercontent.com";
    private static final String GOOGLE_CLIENT_SECRET = "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";
    private static final String GOOGLE_REDIRECT_URI = "http://localhost:8080/Coffee/google-callback";
    
    // Google OAuth2 endpoints
    private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    /**
     * Generate Google OAuth2 authorization URL
     * @param contextPath Application context path
     * @return Authorization URL
     */
    public String getAuthorizationUrl(String contextPath) {
        try {
            String redirectUri = GOOGLE_REDIRECT_URI;
            
            return GOOGLE_AUTH_URL +
                    "?client_id=" + URLEncoder.encode(GOOGLE_CLIENT_ID, "UTF-8") +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode("email profile", "UTF-8") +
                    "&access_type=offline" +
                    "&prompt=select_account";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Exchange authorization code for access token
     * @param code Authorization code from Google
     * @return Access token
     */
    public String getAccessToken(String code) {
        try {
            URL url = new URL(GOOGLE_TOKEN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            
            String data = "code=" + URLEncoder.encode(code, "UTF-8") +
                         "&client_id=" + URLEncoder.encode(GOOGLE_CLIENT_ID, "UTF-8") +
                         "&client_secret=" + URLEncoder.encode(GOOGLE_CLIENT_SECRET, "UTF-8") +
                         "&redirect_uri=" + URLEncoder.encode(GOOGLE_REDIRECT_URI, "UTF-8") +
                         "&grant_type=authorization_code";
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = data.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parse JSON response
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                return jsonObject.get("access_token").getAsString();
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get user info from Google using access token
     * @param accessToken Access token
     * @return GoogleUserInfo object
     */
    public GoogleUserInfo getUserInfo(String accessToken) {
        try {
            URL url = new URL(GOOGLE_USERINFO_URL + "?access_token=" + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                // Parse JSON response
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                
                GoogleUserInfo userInfo = new GoogleUserInfo();
                userInfo.setEmail(jsonObject.get("email").getAsString());
                userInfo.setName(jsonObject.get("name").getAsString());
                userInfo.setPicture(jsonObject.has("picture") ? jsonObject.get("picture").getAsString() : null);
                userInfo.setVerifiedEmail(jsonObject.get("verified_email").getAsBoolean());
                
                return userInfo;
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Inner class to hold Google user info
     */
    public static class GoogleUserInfo {
        private String email;
        private String name;
        private String picture;
        private boolean verifiedEmail;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getPicture() {
            return picture;
        }
        
        public void setPicture(String picture) {
            this.picture = picture;
        }
        
        public boolean isVerifiedEmail() {
            return verifiedEmail;
        }
        
        public void setVerifiedEmail(boolean verifiedEmail) {
            this.verifiedEmail = verifiedEmail;
        }
    }
}
