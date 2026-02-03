package controller;

import model.User;
import service.ShopAccessTokenManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for Token Management
 * Handles token operations for User role (RoleID = 5)
 */
@WebServlet(name = "TokenController", urlPatterns = {"/user/token"})
public class TokenController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check authentication
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user is User (RoleID = 5)
        if (user.getRoleID() != filter.RoleConstants.USER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "Chỉ tài khoản User mới có quyền truy cập chức năng quản lý token.");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "management";
        }

        switch (action) {
            case "management":
                showTokenManagement(request, response);
                break;
            case "generate":
                generateToken(request, response);
                break;
            case "clear":
                clearTokenVerification(request, response);
                break;
            case "status":
                showTokenStatus(request, response);
                break;
            default:
                showTokenManagement(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check authentication
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check if user is User (RoleID = 5)
        if (user.getRoleID() != filter.RoleConstants.USER) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "Chỉ tài khoản User mới có quyền truy cập chức năng quản lý token.");
            return;
        }

        String action = request.getParameter("action");

        switch (action) {
            case "verify":
                verifyToken(request, response);
                break;
            default:
                showTokenManagement(request, response);
                break;
        }
    }

    /**
     * Show token management page
     */
    private void showTokenManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        try {
            // Get user's shops and their token status
            service.ShopService shopService = new service.ShopService();
            java.util.List<Object[]> userShopsWithOwner = shopService.getShopsByOwnerWithOwner(currentUser.getUserID());
            
            // Create a map to store token information for each shop
            java.util.Map<Integer, java.util.Map<String, Object>> shopTokenData = new java.util.HashMap<>();
            
            for (Object[] shopData : userShopsWithOwner) {
                model.Shop shop = (model.Shop) shopData[0]; // Shop object
                // shopData[1] would be User object (owner)
                
                java.util.Map<String, Object> tokenInfo = new java.util.HashMap<>();
                
                // Check if this shop has a token in session
                Boolean shopValidated = (Boolean) session.getAttribute("validatedShopId_" + shop.getShopID());
                String shopToken = (String) session.getAttribute("validatedToken_" + shop.getShopID());
                
                tokenInfo.put("hasToken", shopValidated != null && shopValidated);
                tokenInfo.put("token", shopToken != null ? shopToken : "");
                tokenInfo.put("validated", shopValidated != null && shopValidated);
                
                shopTokenData.put(shop.getShopID(), tokenInfo);
            }
            
            request.setAttribute("userShopsWithOwner", userShopsWithOwner);
            request.setAttribute("shopTokenData", shopTokenData);
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải danh sách shop: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/WEB-INF/views/user/token-management.jsp").forward(request, response);
    }

    /**
     * Generate new token for specific shop
     */
    private void generateToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        String shopIdParam = request.getParameter("shopId");
        
        try {
            if (shopIdParam != null && !shopIdParam.isEmpty()) {
                // Generate token for specific shop
                int shopId = Integer.parseInt(shopIdParam);
                
                // Verify that this shop belongs to the current user
                service.ShopService shopService = new service.ShopService();
                model.Shop shop = shopService.getShopById(shopId);
                
                if (shop == null || !shop.getOwnerID().equals(currentUser.getUserID())) {
                    session.setAttribute("message", "Bạn không có quyền tạo token cho shop này!");
                    session.setAttribute("messageType", "error");
                } else {
                    String token = ShopAccessTokenManager.generateToken(currentUser.getUserID(), shopId);
                    
                    // Store token in session for this specific shop
                    session.setAttribute("validatedShopId_" + shopId, true);
                    session.setAttribute("validatedToken_" + shopId, token);
                    
                    session.setAttribute("message", "Token cho Shop \"" + shop.getShopName() + "\" đã được tạo thành công!");
                    session.setAttribute("messageType", "success");
                    session.setAttribute("generatedToken", token);
                    session.setAttribute("generatedTokenShopId", shopId);
                }
            } else {
                // For backward compatibility - generate general token (but this won't work with new system)
                session.setAttribute("message", "Vui lòng chỉ định shop cụ thể để tạo token!");
                session.setAttribute("messageType", "error");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("message", "ID shop không hợp lệ!");
            session.setAttribute("messageType", "error");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi khi tạo token: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/user/token?action=management");
    }

    /**
     * Verify token
     */
    private void verifyToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            session.setAttribute("message", "Vui lòng nhập mã token!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/token?action=management");
            return;
        }
        
        try {
            if (ShopAccessTokenManager.validateToken(token, currentUser.getUserID())) {
                // Token is valid
                session.setAttribute("shopAccessTokenVerified", true);
                session.setAttribute("shopAccessToken", token);
                session.setAttribute("message", "Xác thực token thành công! Bạn có thể truy cập danh sách shop.");
                session.setAttribute("messageType", "success");
            } else {
                // Token is invalid - show popup
                session.setAttribute("showTokenErrorPopup", true);
                session.setAttribute("tokenErrorMessage", "Mã token không hợp lệ hoặc đã hết hạn!");
                session.removeAttribute("shopAccessTokenVerified");
                session.removeAttribute("shopAccessToken");
            }
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi khi xác thực token: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }
        
        response.sendRedirect(request.getContextPath() + "/user/token?action=management");
    }

    /**
     * Clear token verification
     */
    private void clearTokenVerification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Clear token verification from session
        session.removeAttribute("shopAccessTokenVerified");
        session.removeAttribute("shopAccessToken");
        
        session.setAttribute("message", "Đã hủy xác thực token. Bạn cần xác thực lại để truy cập shop.");
        session.setAttribute("messageType", "warning");
        
        response.sendRedirect(request.getContextPath() + "/user/token?action=management");
    }

    /**
     * Show token status (AJAX endpoint)
     */
    private void showTokenStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Boolean tokenVerified = (Boolean) session.getAttribute("shopAccessTokenVerified");
        String currentToken = (String) session.getAttribute("shopAccessToken");
        
        // Check if user has any active tokens in the system
        boolean hasActiveToken = false;
        if (currentUser != null) {
            hasActiveToken = ShopAccessTokenManager.hasActiveToken(currentUser.getUserID());
        }
        
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"verified\": ").append(tokenVerified != null && tokenVerified);
        json.append(", \"currentToken\": \"").append(currentToken != null ? currentToken : "").append("\"");
        json.append(", \"hasActiveToken\": ").append(hasActiveToken);
        json.append(", \"userId\": ").append(currentUser != null ? currentUser.getUserID() : "null");
        json.append("}");
        
        response.getWriter().write(json.toString());
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Token Management Controller - Handles shop access token operations for User role";
    }
}
