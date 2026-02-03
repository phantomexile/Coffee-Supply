/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import model.Issue;
import model.Ingredient;
import model.BOMItem;
import model.Supplier;
import service.IssueService;
import service.IngredientService;
import service.SettingService;
import service.OrderService;
import service.SupplierService;
import dao.ProductBOMDAO;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * BaristaController handles barista-specific dashboard and functionalities
 */
@WebServlet(name = "BaristaController", urlPatterns = {"/barista/*"})
public class BaristaController extends HttpServlet {

    private IssueService issueService;
    private IngredientService ingredientService;
    private SettingService settingService;
    private OrderService orderService;
    private SupplierService supplierService;
    private ProductBOMDAO productBOMDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        issueService = new IssueService();
        ingredientService = new IngredientService();
        settingService = new SettingService();
        orderService = new OrderService();
        supplierService = new SupplierService();
        productBOMDAO = new ProductBOMDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has barista permission
        if (!"Barista".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/dashboard";
        }

        try {
            switch (pathInfo) {                                              
                case "/issues":
                    showIssueList(request, response);
                    break;
                case "/issue-details":
                    showIssueDetails(request, response);
                    break;
                case "/create-issue":
                    showCreateIssueForm(request, response);
                    break;
                case "/orders":
                    showOrderList(request, response);
                    break;
                case "/order-details":
                    showOrderDetails(request, response);
                    break;
                case "/menu":
                    showMenu(request, response);
                    break;
                case "/edit-issue":
                    showEditIssueForm(request, response);
                    break;     
                case "/schedule":
                    showSchedule(request, response);
                    break;
                default:
                    showDashboard(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Only forward to error page if response not committed
            if (!response.isCommitted()) {
                request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has barista permission
        if (!"Barista".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập khu vực này");
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = request.getParameter("action");

        try {
            switch (pathInfo) {                 
                case "/issues":
                    handleIssueAction(request, response, action, currentUser);
                    break;
                case "/create-issue":
                    handleCreateIssue(request, response, currentUser);
                    break;
                case "/edit-issue":
                    handleEditIssue(request, response, currentUser);
                    break;
                case "/orders":
                    handleOrderAction(request, response, action, currentUser);
                    break;
                default:
                    showDashboard(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    } 
    
    
    /**
     * Handle issue actions (review, agree)
     */
    private void handleIssueAction(HttpServletRequest request, HttpServletResponse response,
                                   String action, User currentUser)
            throws ServletException, IOException {
        
        String issueIdParam = request.getParameter("id");
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID sự cố không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/barista/issues");
            return;
        }
        
        try {
            int issueID = Integer.parseInt(issueIdParam);
            
            Issue issue = issueService.getIssueById(issueID);
            if (issue == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy sự cố");
                response.sendRedirect(request.getContextPath() + "/barista/issues");
                return;
            }
            
            String error = null;
            switch (action) {
                case "updateStatus":
                    String statusIdParam = request.getParameter("statusId");
                    if (statusIdParam != null) {
                        int statusID = Integer.parseInt(statusIdParam);
                        error = issueService.updateIssueStatus(issueID, statusID);
                        if (error == null) {
                            request.getSession().setAttribute("successMessage", 
                                "Cập nhật trạng thái sự cố thành công");
                        }
                    }
                    break;
                case "resolve":
                    error = issueService.resolveIssue(issueID);
                    if (error == null) {
                        request.getSession().setAttribute("successMessage", "Đã giải quyết sự cố thành công");
                    }
                    break;
                case "reject":
                    String rejectionReason = request.getParameter("rejectionReason");
                    if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                        error = "Vui lòng nhập lý do từ chối";
                    } else {
                        error = issueService.rejectIssue(issueID, rejectionReason.trim());
                        if (error == null) {
                            request.getSession().setAttribute("successMessage", "Đã từ chối xử lý sự cố");
                        }
                    }
                    break;
                default:
                    error = "Hành động không hợp lệ";
                    break;
            }
            
            if (error != null) {
                request.getSession().setAttribute("errorMessage", error);
            }
            
            // Redirect back to issue details
            response.sendRedirect(request.getContextPath() + "/barista/issue-details?id=" + issueID);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/barista/issues");
        }
    }
    
    /**
     * Handle order actions (update status, cancel)
     */
    private void handleOrderAction(HttpServletRequest request, HttpServletResponse response,
                                   String action, User currentUser)
            throws ServletException, IOException {
        
        String orderIdParam = request.getParameter("id");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID đơn hàng không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/barista/orders");
            return;
        }
        
        try {
            int orderID = Integer.parseInt(orderIdParam);
            
            model.Order order = orderService.getOrderById(orderID);
            if (order == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng");
                response.sendRedirect(request.getContextPath() + "/barista/orders");
                return;
            }
            
            String error = null;
            switch (action) {
                case "updateStatus":
                    String statusIdParam = request.getParameter("statusId");
                    if (statusIdParam != null && !statusIdParam.isEmpty()) {
                        try {
                            int statusID = Integer.parseInt(statusIdParam);
                            System.out.println("=== DEBUG handleOrderAction ===");
                            System.out.println("Order ID: " + orderID);
                            System.out.println("New Status ID: " + statusID);
                            System.out.println("Current Status: " + order.getStatusName());
                            
                            error = orderService.updateOrderStatus(orderID, statusID);
                            
                            if (error == null) {
                                request.getSession().setAttribute("successMessage", 
                                    "Cập nhật trạng thái đơn hàng thành công");
                                System.out.println("SUCCESS: Status updated!");
                            } else {
                                System.out.println("ERROR: " + error);
                            }
                        } catch (NumberFormatException e) {
                            error = "Status ID không hợp lệ";
                            System.out.println("ERROR: Invalid status ID format");
                        }
                    } else {
                        error = "Vui lòng chọn trạng thái";
                    }
                    break;
                    
                case "cancel":
                    String cancelReason = request.getParameter("cancelReason");
                    if (cancelReason == null || cancelReason.trim().isEmpty()) {
                        error = "Vui lòng nhập lý do hủy";
                    } else if (cancelReason.trim().length() < 10) {
                        error = "Lý do hủy phải có ít nhất 10 ký tự";
                    } else if (cancelReason.trim().length() > 200) {
                        error = "Lý do hủy không được vượt quá 200 ký tự";
                    } else {
                        // Find "Cancelled" status ID (typically 33)
                        List<model.Setting> statuses = settingService.getSettingsByType("OrderStatus");
                        Integer cancelledStatusId = null;
                        for (model.Setting status : statuses) {
                            if ("Cancelled".equals(status.getValue())) {
                                cancelledStatusId = status.getSettingID();
                                break;
                            }
                        }
                        
                        if (cancelledStatusId != null) {
                            error = orderService.updateOrderStatus(orderID, cancelledStatusId);
                            if (error == null) {
                                request.getSession().setAttribute("successMessage", "Đã hủy đơn hàng thành công");
                            }
                        } else {
                            error = "Không tìm thấy trạng thái Cancelled trong hệ thống";
                        }
                    }
                    break;
                    
                default:
                    error = "Hành động không hợp lệ";
                    break;
            }
            
            if (error != null) {
                request.getSession().setAttribute("errorMessage", error);
            }
            
            // Redirect back to order details
            response.sendRedirect(request.getContextPath() + "/barista/order-details?id=" + orderID);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/barista/orders");
        }
    }
    
    /**
     * Handle create issue request from Barista
     * Now supports multiple ingredients for one supplier
     */
    private void handleCreateIssue(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        
        try {
            // Get form parameters
            String supplierIdParam = request.getParameter("supplierId");
            String description = request.getParameter("description");
            String issueType = request.getParameter("issueType");
            
            // Validate supplier selection
            if (supplierIdParam == null || supplierIdParam.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Vui lòng chọn nhà cung cấp");
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }
            
            // Get selected ingredient IDs and quantities
            String[] ingredientIds = request.getParameterValues("selectedIngredients");
            System.out.println("=== DEBUG handleCreateIssue ===");
            System.out.println("Selected ingredients count: " + (ingredientIds != null ? ingredientIds.length : 0));
            if (ingredientIds != null) {
                for (String id : ingredientIds) {
                    String qty = request.getParameter("quantity_" + id);
                    System.out.println("Ingredient ID: " + id + ", Quantity: " + qty);
                }
            }
            
            if (ingredientIds == null || ingredientIds.length == 0) {
                request.getSession().setAttribute("errorMessage", "Vui lòng chọn ít nhất một nguyên liệu");
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }
            
            // Validate quantities for each selected ingredient
            boolean hasError = false;
            String errorMsg = "";
            for (String ingredientId : ingredientIds) {
                String quantityParam = request.getParameter("quantity_" + ingredientId);
                if (quantityParam == null || quantityParam.trim().isEmpty()) {
                    hasError = true;
                    errorMsg = "Vui lòng nhập số lượng cho tất cả nguyên liệu đã chọn";
                    break;
                }
                try {
                    BigDecimal quantity = new BigDecimal(quantityParam);
                    if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                        hasError = true;
                        errorMsg = "Số lượng phải lớn hơn 0";
                        break;
                    }
                } catch (NumberFormatException e) {
                    hasError = true;
                    errorMsg = "Số lượng không hợp lệ";
                    break;
                }
            }
            
            if (hasError) {
                request.getSession().setAttribute("errorMessage", errorMsg);
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }
            
            if (description == null || description.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Vui lòng nhập ghi chú");
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }
            
            if (description.trim().length() > 500) {
                request.getSession().setAttribute("errorMessage", "Ghi chú không được vượt quá 500 ký tự");
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }
            
            if (issueType == null || issueType.trim().isEmpty()) {
                issueType = "RESTOCK_REQUEST"; // Default to restock request for warehouse input
            }
            
            int supplierID = Integer.parseInt(supplierIdParam);
            
            Supplier selectedSupplier = supplierService.getSupplierById(supplierID);
            if (selectedSupplier == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy thông tin nhà cung cấp đã chọn");
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }

            // Add supplier information to description
            String fullDescription = description.trim();
            fullDescription = fullDescription;

            // Validate that each ingredient belongs to the selected supplier
            Map<Integer, Ingredient> ingredientCache = new HashMap<>();
            for (String ingredientId : ingredientIds) {
                try {
                    int ingredientID = Integer.parseInt(ingredientId);
                    Ingredient ingredientDetail = ingredientService.getIngredientById(ingredientID);
                    if (ingredientDetail == null) {
                        hasError = true;
                        errorMsg = "Không tìm thấy thông tin nguyên liệu được chọn";
                        break;
                    }
                    if (ingredientDetail.getSupplierID() != supplierID) {
                        hasError = true;
                        errorMsg = "Nguyên liệu " + ingredientDetail.getName() + " không thuộc nhà cung cấp đã chọn";
                        break;
                    }
                    ingredientCache.put(ingredientID, ingredientDetail);
                } catch (NumberFormatException ex) {
                    hasError = true;
                    errorMsg = "ID nguyên liệu không hợp lệ";
                    break;
                }
            }

            if (hasError) {
                request.getSession().setAttribute("errorMessage", errorMsg);
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
                return;
            }
            
            // Create issue for each selected ingredient
            int successCount = 0;
            int failCount = 0;
            String lastError = "";
            
            for (String ingredientIdStr : ingredientIds) {
                try {
                    int ingredientID = Integer.parseInt(ingredientIdStr);
                    String quantityParam = request.getParameter("quantity_" + ingredientIdStr);
                    BigDecimal quantity = new BigDecimal(quantityParam);

                    Ingredient ingredientDetail = ingredientCache.get(ingredientID);
                    if (ingredientDetail == null) {
                        failCount++;
                        lastError = "Nguyên liệu không hợp lệ trong yêu cầu";
                        continue;
                    }
                    
                    // Create issue with "Pending" status (StatusID = 25)
                    Issue newIssue = new Issue();
                    newIssue.setIngredientID(ingredientID);
                    newIssue.setQuantity(quantity);
                    newIssue.setDescription(fullDescription);
                    newIssue.setIssueType(issueType);
                    newIssue.setStatusID(25); // Pending status
                    newIssue.setCreatedBy(currentUser.getUserID());
                    
                    System.out.println("=== DEBUG: Creating issue ===");
                    System.out.println("IngredientID: " + ingredientID);
                    System.out.println("Quantity: " + quantity);
                    System.out.println("IssueType: " + issueType);
                    System.out.println("StatusID: 25");
                    System.out.println("CreatedBy: " + currentUser.getUserID());
                    System.out.println("Description: " + fullDescription);
                    
                    // Save issue
                    int newIssueID = issueService.createIssue(newIssue);
                    System.out.println("Created issue ID: " + newIssueID);
                    
                    if (newIssueID > 0) {
                        successCount++;
                        System.out.println("Success! Issue ID: " + newIssueID);
                    } else {
                        failCount++;
                        lastError = "Không thể tạo issue (ID trả về: " + newIssueID + ")";
                        System.out.println("Failed to create issue. Returned ID: " + newIssueID);
                    }
                } catch (Exception e) {
                    failCount++;
                    lastError = "Lỗi: " + e.getMessage();
                    System.err.println("=== ERROR creating issue ===");
                    e.printStackTrace();
                }
            }
            
            if (successCount > 0) {
                String successMsg = "Tạo thành công " + successCount + " yêu cầu sự cố";
                if (failCount > 0) {
                    successMsg += " (" + failCount + " yêu cầu thất bại)";
                }
                successMsg += ". Đang chờ Inventory Staff phê duyệt.";
                request.getSession().setAttribute("successMessage", successMsg);
                response.sendRedirect(request.getContextPath() + "/barista/issues");
            } else {
                errorMsg = "Lỗi khi tạo yêu cầu";
                if (!lastError.isEmpty()) {
                    errorMsg += ": " + lastError;
                }
                request.getSession().setAttribute("errorMessage", errorMsg);
                response.sendRedirect(request.getContextPath() + "/barista/create-issue");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/barista/create-issue");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/barista/create-issue");
        }
    }
    
    /**
     * Show edit issue form (only for Pending status issues)
     */
    private void showEditIssueForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String issueIdParam = request.getParameter("id");
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/barista/issues");
            return;
        }
        
        try {
            int issueID = Integer.parseInt(issueIdParam);
            
            // Get issue by ID
            Issue issue = issueService.getIssueById(issueID);
            if (issue == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy sự cố");
                response.sendRedirect(request.getContextPath() + "/barista/issues");
                return;
            }
            
            // Only allow editing Pending status (StatusID = 25)
            if (issue.getStatusID() != 25) {
                request.getSession().setAttribute("errorMessage", 
                    "Chỉ có thể chỉnh sửa sự cố đang ở trạng thái Chờ xử lý");
                response.sendRedirect(request.getContextPath() + "/barista/issue-details?id=" + issueID);
                return;
            }
            
            // Get all ingredients for selection
            List<Ingredient> ingredients = ingredientService.getAllIngredients(1, 1000, null, null, true);
            
            // Parse supplier name and notes for display
            String supplierName = null;
            String issueNotes = issue.getDescription() != null ? issue.getDescription().trim() : "";
            if (issueNotes.startsWith("Nhà cung cấp:")) {
                int newlineIdx = issueNotes.indexOf('\n');
                if (newlineIdx > 0) {
                    supplierName = issueNotes.substring("Nhà cung cấp:".length(), newlineIdx).trim();
                    issueNotes = issueNotes.substring(newlineIdx + 1).trim();
                } else {
                    supplierName = issueNotes.substring("Nhà cung cấp:".length()).trim();
                    issueNotes = "";
                }
            }

            String originalDescription = issue.getDescription() != null ? issue.getDescription().trim() : "";
            String fullDescriptionForLookup = originalDescription;
            if (issue.getIssueType() != null && !issue.getIssueType().isEmpty()) {
                if (!fullDescriptionForLookup.isEmpty()) {
                    fullDescriptionForLookup = "Loại: " + issue.getIssueType().trim() + "\n" + fullDescriptionForLookup;
                } else {
                    fullDescriptionForLookup = "Loại: " + issue.getIssueType().trim();
                }
            }

            List<Issue> relatedIssues = new java.util.ArrayList<>();
            Map<Integer, Ingredient> relatedIngredientMap = new HashMap<>();
            try {
                relatedIssues = issueService.getIssuesByDescriptionAndCreator(fullDescriptionForLookup, issue.getCreatedBy());
                if (relatedIssues == null || relatedIssues.isEmpty()) {
                    relatedIssues = new java.util.ArrayList<>();
                    relatedIssues.add(issue);
                }

                for (Issue relatedIssue : relatedIssues) {
                    if (!relatedIngredientMap.containsKey(relatedIssue.getIngredientID())) {
                        try {
                            Ingredient detail = ingredientService.getIngredientById(relatedIssue.getIngredientID());
                            if (detail != null) {
                                relatedIngredientMap.put(relatedIssue.getIngredientID(), detail);
                            }
                        } catch (Exception ignore) {
                            // Continue even if ingredient lookup fails
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                relatedIssues.clear();
                relatedIssues.add(issue);
            }

            // Set attributes
            request.setAttribute("issue", issue);
            request.setAttribute("ingredients", ingredients);
            request.setAttribute("supplierName", supplierName);
            request.setAttribute("issueNotes", issueNotes);
            request.setAttribute("relatedIssues", relatedIssues);
            request.setAttribute("relatedIngredientMap", relatedIngredientMap);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/views/barista/edit-issue.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/barista/issues");
        }
    }
    
    /**
     * Handle edit issue request from Barista (only for Pending status)
     */
    private void handleEditIssue(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        
        try {
            // Get form parameters
            String issueIdParam = request.getParameter("id");
            String ingredientIdParam = request.getParameter("ingredientId");
            String quantityParam = request.getParameter("quantity");
            String description = request.getParameter("description");
            
            // Validate parameters
            if (issueIdParam == null || issueIdParam.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "ID s? c? kh�ng h?p l?");
                response.sendRedirect(request.getContextPath() + "/barista/issues");
                return;
            }
            
            int issueID = Integer.parseInt(issueIdParam);
            
            // Get existing issue
            Issue existingIssue = issueService.getIssueById(issueID);
            if (existingIssue == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy sự cố");
                response.sendRedirect(request.getContextPath() + "/barista/issues");
                return;
            }
            
            // Only allow editing Pending status (StatusID = 25)
            if (existingIssue.getStatusID() != 25) {
                request.getSession().setAttribute("errorMessage", 
                    "Ch? c� th? ch?nh s?a s? c? dang ? tr?ng th�i Ch? x? l�");
                response.sendRedirect(request.getContextPath() + "/barista/issue-details?id=" + issueID);
                return;
            }
            
            if (ingredientIdParam == null || ingredientIdParam.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Vui l�ng ch?n nguy�n li?u");
                response.sendRedirect(request.getContextPath() + "/barista/edit-issue?id=" + issueID);
                return;
            }
            
            if (quantityParam == null || quantityParam.isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Vui l�ng nh?p s? lu?ng");
                response.sendRedirect(request.getContextPath() + "/barista/edit-issue?id=" + issueID);
                return;
            }
            
            if (description == null || description.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Vui lòng nhập mô tả sự cố");
                response.sendRedirect(request.getContextPath() + "/barista/edit-issue?id=" + issueID);
                return;
            }
            
            if (description.trim().length() > 500) {
                request.getSession().setAttribute("errorMessage", "Ghi chú không được vượt quá 500 ký tự");
                response.sendRedirect(request.getContextPath() + "/barista/edit-issue?id=" + issueID);
                return;
            }
            
            int ingredientID = Integer.parseInt(ingredientIdParam);
            BigDecimal quantity = new BigDecimal(quantityParam);
            
            // Validate quantity
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                request.getSession().setAttribute("errorMessage", "Số lượng phải lớn hơn 0");
                response.sendRedirect(request.getContextPath() + "/barista/edit-issue?id=" + issueID);
                return;
            }

            // Preserve original description for finding related issues later
            String originalDescriptionSection = existingIssue.getDescription() != null ? existingIssue.getDescription().trim() : "";
            String oldFullDescription = originalDescriptionSection;
            if (existingIssue.getIssueType() != null && !existingIssue.getIssueType().isEmpty()) {
                oldFullDescription = "Loại: " + existingIssue.getIssueType().trim() + "\n" + originalDescriptionSection;
            }

            // Extract supplier line if available
            String supplierLine = null;
            if (originalDescriptionSection.startsWith("Nhà cung cấp:")) {
                int newlineIdx = originalDescriptionSection.indexOf('\n');
                if (newlineIdx > 0) {
                    supplierLine = originalDescriptionSection.substring(0, newlineIdx).trim();
                    originalDescriptionSection = originalDescriptionSection.substring(newlineIdx + 1).trim();
                } else {
                    supplierLine = originalDescriptionSection.trim();
                    originalDescriptionSection = "";
                }
            }

            // Update issue - keep status as Pending (25)
            existingIssue.setIngredientID(ingredientID);
            existingIssue.setQuantity(quantity);
            StringBuilder descriptionBuilder = new StringBuilder();
            if (existingIssue.getIssueType() != null && !existingIssue.getIssueType().isEmpty()) {
                descriptionBuilder.append("Loại: ").append(existingIssue.getIssueType().trim()).append('\n');
            }
            if (supplierLine != null && !supplierLine.isEmpty()) {
                descriptionBuilder.append(supplierLine).append('\n');
            }
            if (!description.trim().isEmpty()) {
                descriptionBuilder.append(description.trim());
            }
            String finalDescription = descriptionBuilder.toString().trim();
            existingIssue.setDescription(finalDescription);
            existingIssue.setStatusID(25); // Keep Pending status
            
            // Save changes
            boolean success = issueService.updateIssue(existingIssue);
            
            if (success) {
                // Update related issues (other ingredients in same request) to keep description in sync
                List<Issue> relatedIssues = issueService.getIssuesByDescriptionAndCreator(oldFullDescription, existingIssue.getCreatedBy());
                if (relatedIssues != null) {
                    for (Issue related : relatedIssues) {
                        if (related.getIssueID() == existingIssue.getIssueID()) {
                            continue;
                        }
                        related.setDescription(finalDescription);
                        related.setStatusID(related.getStatusID());
                        issueService.updateIssue(related);
                    }
                }

                request.getSession().setAttribute("successMessage", 
                    "Cập nhật yêu cầu sự cố thành công!");
                response.sendRedirect(request.getContextPath() + "/barista/issue-details?id=" + issueID);
            } else {
                request.getSession().setAttribute("errorMessage", "Lỗi khi cập nhật yêu cầu sự cố");
                response.sendRedirect(request.getContextPath() + "/barista/edit-issue?id=" + issueID);
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Dữ liệu không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/barista/issues");
        }
    }
//Show Dashboard
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get issue statistics from database
            model.IssueStatistics issueStats = issueService.getIssueStatistics();
            
            // Get recent issues (last 10)
            IssueService.IssueResult issueResult = issueService.getAllIssues(1, 10, null, null, null);
            List<Issue> recentIssues = issueResult.getIssues();
            
            // Get order statistics from database
            model.OrderStatistics orderStats = orderService.getOrderStatistics();
            
            // Get recent orders (last 10)
            List<model.Order> allOrders = orderService.getAllOrders();
            List<model.Order> recentOrders = allOrders.size() > 10 ? allOrders.subList(0, 10) : allOrders;
            
           //  Set Issue attributes
            request.setAttribute("totalIssues", issueStats.getTotalIssues());
            request.setAttribute("issuePendingCount", issueStats.getReportedCount());
            request.setAttribute("issueProcessingCount", issueStats.getUnderInvestigationCount());
            request.setAttribute("issueCompletedCount", issueStats.getResolvedCount());
            request.setAttribute("issueRejectedCount", issueStats.getRejectedCount());
                        request.setAttribute("recentIssues", recentIssues);
            
            // Set Order attributes
            request.setAttribute("totalOrders", orderStats.getTotalOrders());
            request.setAttribute("orderPendingCount", orderStats.getPendingOrders());
            request.setAttribute("orderProcessingCount", orderStats.getProcessingOrders());
            request.setAttribute("orderReadyCount", orderStats.getReadyOrders());
            request.setAttribute("orderCompletedCount", orderStats.getCompletedOrders());
            request.setAttribute("orderCancelledCount", orderStats.getCancelledOrders());
            request.setAttribute("recentOrders", recentOrders);
            
            // Forward to dashboard
            request.getRequestDispatcher("/WEB-INF/views/dashboard/barista.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                request.setAttribute("error", "Không thể tải dashboard: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
            }
        }
    }

    private void showSchedule(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // TODO: Implement schedule view when schedule feature is added
        request.setAttribute("message", "Tính năng lịch làm việc đang được phát triển");
        request.getRequestDispatcher("/WEB-INF/views/dashboard/barista.jsp").forward(request, response);
    }
    
    /**
     * Show menu page listing products with their recipes for barista
     */
    private void showMenu(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String searchTerm = request.getParameter("search");
            String categoryParam = request.getParameter("category");
            Integer categoryFilter = null;
            if (categoryParam != null && !categoryParam.trim().isEmpty()) {
                try {
                    categoryFilter = Integer.parseInt(categoryParam.trim());
                } catch (NumberFormatException ignored) {
                    categoryFilter = null;
                }
            }

            String pageParam = request.getParameter("page");
            int currentPage = 1;
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException ignored) {
                    currentPage = 1;
                }
            }

            int pageSize = 3;
            int totalCount = productBOMDAO.getTotalProductsWithFormulaCount(searchTerm, categoryFilter);
            int totalPages = totalCount > 0 ? (int) Math.ceil((double) totalCount / pageSize) : 0;

            if (totalPages > 0 && currentPage > totalPages) {
                currentPage = totalPages;
            }
            if (totalPages == 0) {
                currentPage = 1;
            }

            List<model.Product> products = productBOMDAO.getProductsWithFormula(currentPage, pageSize, searchTerm, categoryFilter);
            Map<Integer, List<BOMItem>> productBOMMap = new HashMap<>();
            for (model.Product product : products) {
                List<BOMItem> bomItems = productBOMDAO.getBOMItemsByProductId(product.getProductID());
                productBOMMap.put(product.getProductID(), bomItems);
            }
            List<model.Setting> categories = settingService.getSettingsByType("Category");

            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.setAttribute("productBOMMap", productBOMMap);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("categoryFilter", categoryFilter);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("pageSize", pageSize);

            request.getRequestDispatcher("/WEB-INF/views/barista/menu.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách menu: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show issue list page
     */
    private void showIssueList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get filter parameters
            String statusFilterParam = request.getParameter("status");
            String ingredientFilter = request.getParameter("ingredient");
            
            Integer statusFilter = null;
            if (statusFilterParam != null && !statusFilterParam.isEmpty()) {
                try {
                    statusFilter = Integer.parseInt(statusFilterParam);
                } catch (NumberFormatException e) {
                    // Ignore invalid status filter
                }
            }
            
            // Get pagination parameters
            String pageParam = request.getParameter("page");
            int currentPage = 1;
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            
            // Get page size parameter
            String pageSizeParam = request.getParameter("pageSize");
            int pageSize = 10; // Default items per page
            if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
                try {
                    pageSize = Integer.parseInt(pageSizeParam);
                    // Validate page size
                    if (pageSize < 5) {
                        pageSize = 5;
                    } else if (pageSize > 100) {
                        pageSize = 100;
                    }
                } catch (NumberFormatException e) {
                    pageSize = 10;
                }
            }
            
            // Get issues with pagination and filters
            IssueService.IssueResult result = issueService.getAllIssues(
                currentPage, 
                pageSize, 
                statusFilter,
                ingredientFilter != null ? Integer.parseInt(ingredientFilter) : null,
                null
            );
            
            List<Issue> issues = result.getIssues();
            
            // Calculate pagination info
            int totalItems = result.getTotalCount();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            
            // Get issue statuses for filter dropdown
            List<model.Setting> issueStatuses = settingService.getSettingsByType("IssueStatus");
            
            // Set attributes
            request.setAttribute("issues", issues);
            request.setAttribute("totalIssues", totalItems);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("issueStatuses", issueStatuses);
            request.setAttribute("statusFilter", statusFilter);
            
            // Forward to issue list page
            request.getRequestDispatcher("/WEB-INF/views/barista/issue-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách issue: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show issue details page
     */
    private void showIssueDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String issueIdParam = request.getParameter("id");
        if (issueIdParam == null || issueIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/barista/issues");
            return;
        }
        
        try {
            int issueID = Integer.parseInt(issueIdParam);
            
            // Get issue by ID
            Issue issue = issueService.getIssueById(issueID);
            if (issue == null) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy sự cố");
                response.sendRedirect(request.getContextPath() + "/barista/issues");
                return;
            }

            // Parse supplier name and notes from description
            String supplierName = null;
            String issueNotes = "";
            if (issue.getDescription() != null) {
                String description = issue.getDescription().trim();
                if (!description.isEmpty()) {
                    if (description.startsWith("Nhà cung cấp:")) {
                        int newlineIdx = description.indexOf('\n');
                        if (newlineIdx > 0) {
                            supplierName = description.substring("Nhà cung cấp:".length(), newlineIdx).trim();
                            issueNotes = description.substring(newlineIdx + 1).trim();
                        } else {
                            supplierName = description.substring("Nhà cung cấp:".length()).trim();
                            issueNotes = "";
                        }
                    } else {
                        issueNotes = description;
                    }
                }
            }
            if (issueNotes == null) {
                issueNotes = "";
            }

            // Retrieve all issues belonging to the same request (multi-ingredient)
            String originalDescriptionForLookup = issueNotes;
            if (issue.getIssueType() != null && !issue.getIssueType().isEmpty()) {
                originalDescriptionForLookup = "Loại: " + issue.getIssueType().trim() + "\n" + (issue.getDescription() != null ? issue.getDescription().trim() : "");
            } else if (issue.getDescription() != null) {
                originalDescriptionForLookup = issue.getDescription().trim();
            }

            List<Issue> relatedIssues = new java.util.ArrayList<>();
            Map<Integer, Ingredient> relatedIngredientMap = new HashMap<>();
            try {
                relatedIssues = issueService.getIssuesByDescriptionAndCreator(originalDescriptionForLookup, issue.getCreatedBy());
                if (relatedIssues == null || relatedIssues.isEmpty()) {
                    relatedIssues = new java.util.ArrayList<>();
                    relatedIssues.add(issue);
                }

                for (Issue relatedIssue : relatedIssues) {
                    if (!relatedIngredientMap.containsKey(relatedIssue.getIngredientID())) {
                        try {
                            Ingredient ingredientDetail = ingredientService.getIngredientById(relatedIssue.getIngredientID());
                            if (ingredientDetail != null) {
                                relatedIngredientMap.put(relatedIssue.getIngredientID(), ingredientDetail);
                            }
                        } catch (Exception ignore) {
                            // Continue even if ingredient lookup fails
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                relatedIssues.clear();
                relatedIssues.add(issue);
            }

            // Get ingredient details for better display (stock, unit, etc.)
            Ingredient issueIngredient = null;
            try {
                issueIngredient = ingredientService.getIngredientById(issue.getIngredientID());
            } catch (Exception ex) {
                // Ignore and continue with available info
            }

            // Get all statuses for dropdown
            List<model.Setting> issueStatuses = settingService.getSettingsByType("IssueStatus");
            
            // Set attributes
            request.setAttribute("issue", issue);
            request.setAttribute("issueStatuses", issueStatuses);
            request.setAttribute("supplierName", supplierName);
            request.setAttribute("issueNotes", issueNotes);
            request.setAttribute("issueIngredient", issueIngredient);
            request.setAttribute("relatedIssues", relatedIssues);
            request.setAttribute("relatedIngredientMap", relatedIngredientMap);

            // Forward to issue details page
            request.getRequestDispatcher("/WEB-INF/views/barista/issue-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/barista/issues");
        }
    }
    
    /**
     * Show create issue form
     */
    private void showCreateIssueForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all ingredients with their stock quantity and unit
        List<Ingredient> ingredients = ingredientService.getAllIngredients(1, 1000, null, null, true);
        
        // Get all suppliers for dropdown
        List<Supplier> suppliers = supplierService.getActiveSuppliers();
        
        // Get current user for requester display
        User currentUser = (User) request.getSession().getAttribute("user");
        
        // Set current time for display
        request.setAttribute("currentTime", new java.util.Date());
        
        // Set attributes
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("currentUser", currentUser);
        
        // Forward to create issue page
        request.getRequestDispatcher("/WEB-INF/views/barista/create-issue.jsp").forward(request, response);
    }
    
    /**
     * Show order list page
     */
    private void showOrderList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Get pagination parameters
            String pageParam = request.getParameter("page");
            int currentPage = 1;
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            
            // Page size - default to 10
            int pageSize = 10;
            
            // Get filter parameter
            String statusParam = request.getParameter("status");
            Integer statusFilter = null;
            if (statusParam != null && !statusParam.isEmpty()) {
                try {
                    statusFilter = Integer.parseInt(statusParam);
                } catch (NumberFormatException e) {
                    // Ignore invalid status
                }
            }
            
            // Get total count for pagination first
            int totalCount = orderService.getTotalOrderCount(statusFilter, null);
            int totalPages = totalCount > 0 ? (int) Math.ceil((double) totalCount / pageSize) : 0;
            if (totalPages < 1 && totalCount > 0) {
                totalPages = 1;
            }
            
            // Validate currentPage - ensure it doesn't exceed totalPages
            if (totalPages > 0 && currentPage > totalPages) {
                currentPage = totalPages;
            }
            
            // Get orders with pagination
            List<model.Order> orders = orderService.getOrdersWithPagination(currentPage, pageSize, statusFilter);
            
            // Get order statistics
            model.OrderStatistics stats = orderService.getOrderStatistics();
            
            // Get all order statuses for filter dropdown
            List<model.Setting> orderStatuses = settingService.getSettingsByType("OrderStatus");
            
            // Set attributes
            request.setAttribute("orders", orders);
            request.setAttribute("orderStatuses", orderStatuses);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalOrders", stats.getTotalOrders());
            request.setAttribute("pendingOrders", stats.getPendingOrders());
            request.setAttribute("processingOrders", stats.getProcessingOrders());
            request.setAttribute("readyOrders", stats.getReadyOrders());
            request.setAttribute("completedOrders", stats.getCompletedOrders());
            
            // Forward to order list page
            request.getRequestDispatcher("/WEB-INF/views/barista/order-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("=== ERROR in showOrderList ===");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getName());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show order details page
     */
    private void showOrderDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String orderIdParam = request.getParameter("id");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/barista/orders");
            return;
        }
        
        try {
            int orderID = Integer.parseInt(orderIdParam);
            System.out.println("=== DEBUG showOrderDetails ===");
            System.out.println("Order ID: " + orderID);
            
            // Get order by ID
            model.Order order = orderService.getOrderById(orderID);
            if (order == null) {
                System.out.println("ERROR: Order not found!");
                request.getSession().setAttribute("errorMessage", "Không tìm thấy đơn hàng");
                response.sendRedirect(request.getContextPath() + "/barista/orders");
                return;
            }
            
            System.out.println("Order found: " + order.getOrderID() + ", Shop: " + order.getShopName());
            
            // Get order details with product information
            List<Map<String, Object>> orderDetails = orderService.getOrderDetailsWithProduct(orderID);
            System.out.println("OrderDetails count: " + (orderDetails != null ? orderDetails.size() : "NULL"));
            
            if (orderDetails != null && !orderDetails.isEmpty()) {
                System.out.println("First detail: " + orderDetails.get(0));
            } else {
                System.out.println("WARNING: No OrderDetails found for Order #" + orderID);
            }
            
            // Calculate total
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;
            for (Map<String, Object> detail : orderDetails) {
                java.math.BigDecimal subtotal = (java.math.BigDecimal) detail.get("subtotal");
                if (subtotal != null) {
                    total = total.add(subtotal);
                }
            }
            System.out.println("Total: " + total);
            
            // Get order statuses for status change
            List<model.Setting> orderStatuses = settingService.getSettingsByType("OrderStatus");
            System.out.println("OrderStatuses count: " + (orderStatuses != null ? orderStatuses.size() : "NULL"));
            
            // Set attributes
            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.setAttribute("total", total);
            request.setAttribute("orderStatuses", orderStatuses);
            
            System.out.println("=== END DEBUG ===");
            
            // Forward to order details page
            request.getRequestDispatcher("/WEB-INF/views/barista/order-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid order ID format");
            response.sendRedirect(request.getContextPath() + "/barista/orders");
        }
    }

}