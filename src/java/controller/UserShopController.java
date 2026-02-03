package controller;

import model.Shop;
import model.User;
import service.ShopService;
import service.CloudinaryService;
import service.ShopAccessTokenManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;

/**
 * Controller cho User quản lý thông tin Shop
 * Chỉ cho phép User (RoleID = 5) truy cập
 * Hỗ trợ upload ảnh shop lên Cloudinary
 */
@WebServlet(name = "UserShopController", urlPatterns = {"/user/shop"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1 MB
    maxFileSize = 1024 * 1024 * 5,      // 5 MB  
    maxRequestSize = 1024 * 1024 * 25   // 25 MB
)
public class UserShopController extends HttpServlet {

    private final ShopService shopService = new ShopService();

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
                "Chỉ tài khoản User mới có quyền truy cập chức năng quản lý shop.");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                viewShopList(request, response);
                break;
            case "details":
            case "view":
            case "viewDetails":
                viewShopDetails(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteShop(request, response);
                break;
            case "toggleStatus":
                toggleShopStatus(request, response);
                break;
            case "generateToken":
                generateAccessToken(request, response);
                break;
            case "verifyToken":
                verifyAccessToken(request, response);
                break;
            case "clearToken":
                clearTokenVerification(request, response);
                break;
            default:
                viewShopList(request, response);
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
                "Chỉ tài khoản User mới có quyền truy cập chức năng quản lý shop.");
            return;
        }

        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addShop(request, response);
                break;
            case "edit":
                editShop(request, response);
                break;
            case "viewDetails":
                viewShopDetails(request, response);
                break;
            case "verifyToken":
                verifyAccessToken(request, response);
                break;
            default:
                viewShopList(request, response);
                break;
        }
    }

    /**
     * Xem danh sách shops 
     */
    private void viewShopList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Lấy thông tin user hiện tại từ session
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                request.setAttribute("error", "Bạn cần đăng nhập để xem danh sách shops!");
                request.getRequestDispatcher("/WEB-INF/views/user/shop-list.jsp").forward(request, response);
                return;
            }
            
            // Token verification removed - direct access to shop list
            
            // Get pagination parameters
            int page = 1;
            int pageSize = 10;
            
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            
            String pageSizeParam = request.getParameter("pageSize");
            if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                try {
                    pageSize = Integer.parseInt(pageSizeParam);
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }
            
            // Get filter parameters
            String searchName = request.getParameter("searchName");
            String searchAddress = request.getParameter("searchAddress");
            String statusFilter = request.getParameter("status");
            
            // Get shops with pagination and filters
            ShopService.ShopResult result = shopService.getShopsByOwnerWithOwnerPaginated(
                currentUser.getUserID(), page, pageSize, searchName, searchAddress, statusFilter);
            
            // Set attributes for JSP
            request.setAttribute("shopsWithOwner", result.getShopsWithOwner());
            request.setAttribute("currentPage", result.getCurrentPage());
            request.setAttribute("totalPages", result.getTotalPages());
            request.setAttribute("totalCount", result.getTotalCount());
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("searchName", searchName);
            request.setAttribute("searchAddress", searchAddress);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("authenticated", true);
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi lấy danh sách shops: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/WEB-INF/views/user/shop-list.jsp").forward(request, response);
    }

    /**
     * Xem chi tiết 1 shop theo ID - Requires token verification
     */
    private void viewShopDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            request.setAttribute("error", "Bạn cần đăng nhập để xem chi tiết shop!");
            request.getRequestDispatcher("/WEB-INF/views/user/shop-details.jsp").forward(request, response);
            return;
        }
        
        String shopIdParam = request.getParameter("shopId");
        
        if (shopIdParam == null || shopIdParam.isEmpty()) {
            request.setAttribute("error", "Không tìm thấy thông tin shop!");
            request.getRequestDispatcher("/WEB-INF/views/user/shop-details.jsp").forward(request, response);
            return;
        }
        
        try {
            int shopId = Integer.parseInt(shopIdParam);
            
            // Debug logging
            System.out.println("=== DEBUG viewShopDetails ===");
            System.out.println("Shop ID: " + shopId);
            System.out.println("Token parameter: " + request.getParameter("token"));
            
            // Check if this shop has already been validated in the session
            Boolean shopValidated = (Boolean) request.getSession().getAttribute("validatedShopId_" + shopId);
            System.out.println("Shop validated in session: " + shopValidated);
            
            if (shopValidated == null || !shopValidated) {
                // Check if user has provided access token for this specific shop
                String accessToken = request.getParameter("token");
            
                if (accessToken == null || accessToken.trim().isEmpty()) {
                    // Show token verification form for this specific shop
                    request.setAttribute("needToken", true);
                    request.setAttribute("shopDetailsRequested", true);
                    request.setAttribute("shopId", shopId);
                    request.getRequestDispatcher("/WEB-INF/views/user/shop-details.jsp").forward(request, response);
                    return;
                }
                
                // Verify the provided token for this specific shop
                if (!ShopAccessTokenManager.validateToken(accessToken, currentUser.getUserID(), shopId)) {
                    request.setAttribute("needToken", true);
                    request.setAttribute("showTokenErrorPopup", true);
                    request.setAttribute("tokenErrorMessage", "Mã token không hợp lệ hoặc không dành cho shop này!");
                    request.setAttribute("shopDetailsRequested", true);
                    request.setAttribute("shopId", shopId);
                    request.getRequestDispatcher("/WEB-INF/views/user/shop-details.jsp").forward(request, response);
                    return;
                }
                
                // Token is valid, store in session for this shop
                request.getSession().setAttribute("validatedShopId_" + shopId, true);
                request.getSession().setAttribute("validatedToken_" + shopId, accessToken);
                System.out.println("Token validated successfully, proceeding to show shop details");
                
                // Don't redirect, just continue to show shop details
            }
            
            // At this point, shop was already validated in session
            // Proceed to show shop details
            Shop shop = shopService.getShopById(shopId);
            
            if (shop == null) {
                request.setAttribute("error", "Shop không tồn tại!");
            } else {
                request.setAttribute("shop", shop);
                request.setAttribute("authenticated", true);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID shop không hợp lệ!");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi lấy thông tin shop: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/WEB-INF/views/user/shop-details.jsp").forward(request, response);
    }

    /**
     * Hiển thị form thêm shop
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/user/shop-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị form sửa shop
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String shopIdParam = request.getParameter("id");
        if (shopIdParam == null || shopIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
            return;
        }
        
        try {
            int shopId = Integer.parseInt(shopIdParam);
            Shop shop = shopService.getShopById(shopId);
            
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            
            // Chỉ cho phép chủ shop chỉnh sửa
            if (shop == null || !shop.getOwnerID().equals(currentUser.getUserID())) {
                session.setAttribute("message", "Bạn chỉ có thể chỉnh sửa shop của mình!");
                session.setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
                return;
            }
            
            request.setAttribute("shop", shop);
            request.getRequestDispatcher("/WEB-INF/views/user/shop-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
        }
    }

    /**
     * Thêm shop mới
     */
    private void addShop(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        String shopName = request.getParameter("shopName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        boolean isActive = request.getParameter("isActive") != null;

        // Validate input
        if (shopName == null || shopName.trim().isEmpty()) {
            session.setAttribute("message", "Tên shop không được để trống!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=add");
            return;
        }
        
        if (address == null || address.trim().isEmpty()) {
            session.setAttribute("message", "Địa chỉ không được để trống!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=add");
            return;
        }
        
        if (phone == null || phone.trim().isEmpty()) {
            session.setAttribute("message", "Số điện thoại không được để trống!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=add");
            return;
        }

        // Trim whitespace
        shopName = shopName.trim();
        address = address.trim();
        phone = phone.trim();

        // Validate phone number format
        if (!isValidPhoneNumber(phone)) {
            session.setAttribute("message", "Số điện thoại không đúng định dạng! Vui lòng nhập theo định dạng: 0123456789 hoặc +84123456789");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=add");
            return;
        }

        // Tự động lấy OwnerID từ user đang đăng nhập
        Integer ownerId = currentUser.getUserID();

        try {
            // Handle file upload
            String shopImagePath = null;
            Part imagePart = request.getPart("shopImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                try {
                    shopImagePath = CloudinaryService.uploadShopImage(imagePart.getInputStream(), imagePart.getSubmittedFileName(), -1); // Use -1 as temp ID
                } catch (Exception e) {
                    session.setAttribute("message", "Lỗi khi upload ảnh: " + e.getMessage());
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/user/shop?action=add");
                    return;
                }
            }
            
            // Use default image if no image uploaded
            if (shopImagePath == null) {
                shopImagePath = CloudinaryService.getDefaultShopImage();
            }

            int newShopId = shopService.createShop(shopName, address, phone, ownerId, isActive, shopImagePath);
            
            // If we uploaded a temporary image, upload again with the actual shop ID
            if (imagePart != null && imagePart.getSize() > 0 && shopImagePath != null && !shopImagePath.equals(CloudinaryService.getDefaultShopImage())) {
                try {
                    // Delete the temp image and upload with correct shop ID
                    CloudinaryService.deleteShopImage(shopImagePath);
                    String finalImagePath = CloudinaryService.uploadShopImage(imagePart.getInputStream(), imagePart.getSubmittedFileName(), newShopId);
                    // Update shop with correct image path
                    shopService.updateShop(newShopId, shopName, address, phone, ownerId, isActive, finalImagePath);
                } catch (Exception e) {
                    // Log error but don't fail the shop creation
                    System.err.println("Warning: Could not rename shop image file: " + e.getMessage());
                }
            }

            if (newShopId > 0) {
                session.setAttribute("message", "Thêm shop thành công!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Thêm shop thất bại! Vui lòng thử lại.");
                session.setAttribute("messageType", "error");
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("message", "Lỗi validation: " + e.getMessage());
            session.setAttribute("messageType", "error");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
    }

    /**
     * Sửa thông tin shop
     */
    private void editShop(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        String shopIdParam = request.getParameter("shopId");
        String shopName = request.getParameter("shopName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        boolean isActive = request.getParameter("isActive") != null;

        // Validate phone number format
        if (phone != null && !phone.trim().isEmpty() && !isValidPhoneNumber(phone.trim())) {
            session.setAttribute("message", "Số điện thoại không đúng định dạng! Vui lòng nhập theo định dạng: 0123456789 hoặc +84123456789");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=edit&id=" + shopIdParam);
            return;
        }

        try {
            int shopId = Integer.parseInt(shopIdParam);
            Shop existingShop = shopService.getShopById(shopId);
            
            // Chỉ cho phép chủ shop chỉnh sửa
            if (existingShop == null || !existingShop.getOwnerID().equals(currentUser.getUserID())) {
                session.setAttribute("message", "Bạn chỉ có thể chỉnh sửa shop của mình!");
                session.setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
                return;
            }

            // Handle image upload
            String shopImagePath = existingShop.getShopImage(); // Keep current image by default
            
            // Handle new image upload
            Part imagePart = request.getPart("shopImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                try {
                    // Delete old image file if exists
                    if (shopImagePath != null && !shopImagePath.isEmpty() && !shopImagePath.equals(CloudinaryService.getDefaultShopImage())) {
                        CloudinaryService.deleteShopImage(shopImagePath);
                    }
                    
                    // Upload new image
                    shopImagePath = CloudinaryService.uploadShopImage(imagePart.getInputStream(), imagePart.getSubmittedFileName(), shopId);
                } catch (Exception e) {
                    session.setAttribute("message", "Lỗi khi upload ảnh: " + e.getMessage());
                    session.setAttribute("messageType", "error");
                    response.sendRedirect(request.getContextPath() + "/user/shop?action=edit&id=" + shopIdParam);
                    return;
                }
            }

            boolean success = shopService.updateShop(shopId, shopName, address, phone, currentUser.getUserID(), isActive, shopImagePath);

            if (success) {
                session.setAttribute("message", "Cập nhật shop thành công!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Cập nhật shop thất bại!");
                session.setAttribute("messageType", "error");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("message", "ID shop không hợp lệ!");
            session.setAttribute("messageType", "error");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
    }

    /**
     * Xóa shop 
     */
    private void deleteShop(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        String shopIdParam = request.getParameter("id");
        
        try {
            int shopId = Integer.parseInt(shopIdParam);
            Shop existingShop = shopService.getShopById(shopId);
            
            // Chỉ cho phép chủ shop xóa
            if (existingShop == null || !existingShop.getOwnerID().equals(currentUser.getUserID())) {
                session.setAttribute("message", "Bạn chỉ có thể xóa shop của mình!");
                session.setAttribute("messageType", "error");
                response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
                return;
            }
            
            boolean success = shopService.deleteShop(shopId);

            if (success) {
                session.setAttribute("message", "Xóa shop thành công!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Xóa shop thất bại!");
                session.setAttribute("messageType", "error");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("message", "ID shop không hợp lệ!");
            session.setAttribute("messageType", "error");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi: " + e.getMessage());
            session.setAttribute("messageType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
    }
    
    /**
     * Validate Vietnamese phone number format
     * @param phone The phone number to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        
        // Remove all spaces, dashes, parentheses for validation
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        
        // Vietnamese phone number patterns:
        // 1. 10 digits starting with 0 (domestic format): 0xxxxxxxxx
        // 2. 11 digits starting with +84 (international format): +84xxxxxxxxx
        // 3. 10 digits starting with 84 (alternative international): 84xxxxxxxxx
        
        return cleanPhone.matches("^0[0-9]{9}$") ||           // 0123456789
               cleanPhone.matches("^\\+84[0-9]{9}$") ||       // +84123456789
               cleanPhone.matches("^84[0-9]{9}$");            // 84123456789
    }
    
    /**
     * Toggle shop status (active/inactive)
     */
    private void toggleShopStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String shopIdParam = request.getParameter("id");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (shopIdParam == null || shopIdParam.isEmpty()) {
            session.setAttribute("message", "Không tìm thấy thông tin shop!");
            session.setAttribute("messageType", "danger");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
            return;
        }
        
        try {
            int shopId = Integer.parseInt(shopIdParam);
            
            // Get the shop to verify ownership
            Shop shop = shopService.getShopById(shopId);
            if (shop == null) {
                session.setAttribute("message", "Không tìm thấy shop!");
                session.setAttribute("messageType", "danger");
                response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
                return;
            }
            
            // Check if user owns this shop
            if (shop.getOwnerID() == null || !shop.getOwnerID().equals(user.getUserID())) {
                session.setAttribute("message", "Bạn không có quyền thay đổi trạng thái shop này!");
                session.setAttribute("messageType", "danger");
                response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
                return;
            }
            
            // Toggle the status
            boolean newStatus = !shop.isActive();
            boolean success = shopService.updateShop(shopId, shop.getShopName(), shop.getAddress(), 
                                                   shop.getPhone(), shop.getOwnerID(), newStatus, shop.getShopImage());
            
            if (success) {
                String statusText = newStatus ? "kích hoạt" : "ngừng hoạt động";
                session.setAttribute("message", "Đã " + statusText + " shop thành công!");
                session.setAttribute("messageType", "success");
            } else {
                session.setAttribute("message", "Có lỗi xảy ra khi thay đổi trạng thái shop!");
                session.setAttribute("messageType", "danger");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("message", "ID shop không hợp lệ!");
            session.setAttribute("messageType", "danger");
        } catch (Exception e) {
            session.setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            session.setAttribute("messageType", "danger");
        }
        
        response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
    }
    
    /**
     * Generate shop access token for specific shop
     */
    private void generateAccessToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String shopIdParam = request.getParameter("shopId");
        String ajaxParam = request.getParameter("ajax");
        
        if (shopIdParam == null || shopIdParam.isEmpty()) {
            if ("true".equals(ajaxParam)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"error\": \"Cần chỉ định shop để tạo token!\"}");
                return;
            }
            session.setAttribute("message", "Cần chỉ định shop để tạo token!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
            return;
        }
        
        try {
            int shopId = Integer.parseInt(shopIdParam);
            
            // Generate new token for specific shop
            String token = ShopAccessTokenManager.generateToken(currentUser.getUserID(), shopId);
            
            // Store in session for this shop
            session.setAttribute("validatedShopId_" + shopId, true);
            session.setAttribute("validatedToken_" + shopId, token);
            
            // For AJAX requests, return JSON response
            if ("true".equals(ajaxParam)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"token\": \"" + token + "\", \"shopId\": " + shopId + "}");
                return;
            }
            
            // Set success popup message with token (for non-AJAX requests)
            session.setAttribute("showTokenSuccessPopup", true);
            session.setAttribute("tokenSuccessMessage", "Token cho Shop ID " + shopId + " đã được tạo thành công!");
            session.setAttribute("generatedToken", token);
            session.setAttribute("generatedTokenShopId", shopId);
            session.setAttribute("tokenGeneratedFor", "Shop ID: " + shopId);
            
        } catch (Exception e) {
            if ("true".equals(ajaxParam)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
                return;
            }
            session.setAttribute("showTokenErrorPopup", true);
            session.setAttribute("tokenErrorMessage", "Lỗi khi tạo token: " + e.getMessage());
        }
        
        // Redirect back to the referring page or shop list
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/user/token")) {
            response.sendRedirect(request.getContextPath() + "/user/token?action=management");
        } else {
            response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
        }
    }
    
    /**
     * Verify shop access token
     */
    private void verifyAccessToken(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            session.setAttribute("message", "Vui lòng nhập mã token!");
            session.setAttribute("messageType", "error");
            response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
            return;
        }
        
        try {
            if (ShopAccessTokenManager.validateToken(token, currentUser.getUserID())) {
                // Token is valid
                session.setAttribute("shopAccessTokenVerified", true);
                session.setAttribute("shopAccessToken", token);
                session.setAttribute("message", "Xác thực token thành công!");
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
        
        response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
    }
    
    /**
     * Clear token verification from session
     */
    private void clearTokenVerification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Clear token verification from session
        session.removeAttribute("shopAccessTokenVerified");
        session.removeAttribute("shopAccessToken");
        
        session.setAttribute("message", "Đã hủy xác thực token. Bạn cần xác thực lại để truy cập shop.");
        session.setAttribute("messageType", "warning");
        
        response.sendRedirect(request.getContextPath() + "/user/shop?action=list");
    }
}
