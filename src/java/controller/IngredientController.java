/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import model.Ingredient;
import model.Setting;
import model.Supplier;
import service.IngredientService;
import service.IngredientService.IngredientResult;
import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for Ingredient management
 * @author Hòa
 */
@WebServlet(name = "IngredientController", urlPatterns = {"/ingredient"})
public class IngredientController extends HttpServlet {
    
    private IngredientService ingredientService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        ingredientService = new IngredientService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication and authorization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has permission to access ingredient management
        if (!"Inventory".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "list":
                    showIngredientList(request, response);
                    break;
                case "create":
                    showCreateForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "view":
                    showIngredientDetails(request, response);
                    break;
                case "low-stock":
                    showLowStockIngredients(request, response);
                    break;
                case "toggle":
                    handleToggleIngredient(request, response);
                    break;
                default:
                    showIngredientList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showIngredientList(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication and authorization
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String currentUserRole = (String) session.getAttribute("roleName");
        
        // Check if user has permission to access ingredient management
        if (!"Inventory".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "create":
                    handleCreateIngredient(request, response);
                    break;
                case "update":
                    handleUpdateIngredient(request, response);
                    break;
                case "delete":
                    handleDeleteIngredient(request, response);
                    break;
                case "toggle":
                    handleToggleIngredient(request, response);
                    break;
                case "update-stock":
                    handleUpdateStock(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/ingredient");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showIngredientList(request, response);
        }
    }
    
    /**
     * Display ingredient list with pagination and filtering
     */
    private void showIngredientList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get parameters
        String pageStr = request.getParameter("page");
        String searchKeyword = request.getParameter("search");
        String supplierFilterStr = request.getParameter("supplier");
        String activeFilter = request.getParameter("active");
        String successMessage = request.getParameter("success");
        String errorMessage = request.getParameter("error");
        
        // Set default values
        int currentPage = 1;
        int pageSize = 10;
        Integer supplierFilter = null;
        Boolean activeOnly = null;
        
        try {
            if (pageStr != null && !pageStr.isEmpty()) {
                currentPage = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }
        
        try {
            if (supplierFilterStr != null && !supplierFilterStr.isEmpty()) {
                supplierFilter = Integer.parseInt(supplierFilterStr);
            }
        } catch (NumberFormatException e) {
            supplierFilter = null;
        }
        
        if (activeFilter != null && !activeFilter.isEmpty()) {
            activeOnly = "true".equals(activeFilter);
        }
        
        // Get ingredients with pagination
        List<Ingredient> ingredients = ingredientService.getAllIngredients(
            currentPage, pageSize, supplierFilter, searchKeyword, activeOnly);
        
        // Get total count for pagination
        int totalItems = ingredientService.getTotalIngredientsCount(supplierFilter, searchKeyword, activeOnly);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        
        // Get suppliers for dropdown
        List<Supplier> suppliers = ingredientService.getActiveSuppliers();
        
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalIngredients", totalItems);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("supplierFilter", supplierFilter);
        request.setAttribute("activeOnly", activeOnly);
        request.setAttribute("suppliers", suppliers);
        
        if (successMessage != null) {
            request.setAttribute("success", successMessage);
        }
        if (errorMessage != null) {
            request.setAttribute("error", errorMessage);
        }
        
        request.getRequestDispatcher("/WEB-INF/views/inventory/ingredient-list.jsp").forward(request, response);
    }
    
    /**
     * Show create form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get units and suppliers for dropdown
        List<Setting> units = ingredientService.getAvailableUnits();
        List<Supplier> suppliers = ingredientService.getActiveSuppliers();
        
        request.setAttribute("units", units);
        request.setAttribute("suppliers", suppliers);
        request.setAttribute("isEdit", false);
        
        request.getRequestDispatcher("/WEB-INF/views/inventory/ingredient-form.jsp").forward(request, response);
    }
    
    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredientIdStr = request.getParameter("id");
        
        if (ingredientIdStr == null || ingredientIdStr.isEmpty()) {
            request.setAttribute("error", "ID nguyên liệu không hợp lệ");
            showIngredientList(request, response);
            return;
        }
        
        try {
            int ingredientId = Integer.parseInt(ingredientIdStr);
            
            // Get ingredient details
            Ingredient ingredient = ingredientService.getIngredientById(ingredientId);
            List<Setting> units = ingredientService.getAvailableUnits();
            List<Supplier> suppliers = ingredientService.getActiveSuppliers();
            
            if (ingredient != null) {
                request.setAttribute("ingredient", ingredient);
                request.setAttribute("units", units);
                request.setAttribute("suppliers", suppliers);
                request.setAttribute("isEdit", true);
                
                request.getRequestDispatcher("/WEB-INF/views/inventory/ingredient-form.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không thể tải thông tin nguyên liệu");
                showIngredientList(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID nguyên liệu không hợp lệ");
            showIngredientList(request, response);
        }
    }
    
    /**
     * Show ingredient details
     */
    private void showIngredientDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredientIdStr = request.getParameter("id");
        
        if (ingredientIdStr == null || ingredientIdStr.isEmpty()) {
            request.setAttribute("error", "ID nguyên liệu không hợp lệ");
            showIngredientList(request, response);
            return;
        }
        
        try {
            int ingredientId = Integer.parseInt(ingredientIdStr);
            
            Ingredient ingredient = ingredientService.getIngredientById(ingredientId);
            
            if (ingredient != null) {
                request.setAttribute("ingredient", ingredient);
                request.getRequestDispatcher("/WEB-INF/views/inventory/ingredient-details.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Nguyên liệu không tồn tại");
                showIngredientList(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID nguyên liệu không hợp lệ");
            showIngredientList(request, response);
        }
    }
    
    /**
     * Show low stock ingredients
     */
    private void showLowStockIngredients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set default threshold (có thể lấy từ setting)
        BigDecimal threshold = new BigDecimal("10.0");
        
        List<Ingredient> ingredients = ingredientService.getLowStockIngredients(threshold);
        
        // Debug log
        System.out.println("=== LOW STOCK DEBUG ===");
        System.out.println("Threshold: " + threshold);
        System.out.println("Found ingredients: " + ingredients.size());
        for (Ingredient ing : ingredients) {
            System.out.println("- " + ing.getName() + ": " + ing.getStockQuantity() + " (Active: " + ing.isActive() + ")");
        }
        System.out.println("=======================");
        
        request.setAttribute("lowStockIngredients", ingredients);
        request.setAttribute("threshold", threshold);
        request.getRequestDispatcher("/WEB-INF/views/inventory/low-stock.jsp").forward(request, response);
    }
    
    /**
     * Handle create ingredient
     */
    private void handleCreateIngredient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");  // Thêm description
        String unitIdStr = request.getParameter("unitId");
        String stockQuantityStr = request.getParameter("stockQuantity");
        String supplierIdStr = request.getParameter("supplierId");
        String activeStr = request.getParameter("active");
        
        try {
            // Parse parameters
            int unitId = Integer.parseInt(unitIdStr);
            BigDecimal stockQuantity = new BigDecimal(stockQuantityStr);
            int supplierId = Integer.parseInt(supplierIdStr);
            boolean active = "true".equals(activeStr);
            
            // Create ingredient object
            Ingredient ingredient = new Ingredient(name, unitId, stockQuantity, supplierId, active);
            ingredient.setDescription(description);  // Set description
            
            // Create ingredient
            IngredientResult result = ingredientService.createIngredient(ingredient);
            
            if (result.isSuccess()) {
                response.sendRedirect(request.getContextPath() + "/ingredient?success=" + 
                    java.net.URLEncoder.encode("Thêm nguyên liệu thành công!", "UTF-8"));
                return;
            } else {
                request.setAttribute("error", result.getMessage());
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập không hợp lệ");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        // If error, show form again with data
        request.setAttribute("name", name);
        request.setAttribute("unitId", unitIdStr);
        request.setAttribute("stockQuantity", stockQuantityStr);
        request.setAttribute("supplierId", supplierIdStr);
        request.setAttribute("active", activeStr);
        
        showCreateForm(request, response);
    }
    
    /**
     * Handle update ingredient
     */
    private void handleUpdateIngredient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredientIdStr = request.getParameter("ingredientId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");  // Thêm description
        String unitIdStr = request.getParameter("unitId");
        String stockQuantityStr = request.getParameter("stockQuantity");
        String supplierIdStr = request.getParameter("supplierId");
        String activeStr = request.getParameter("active");
        
        try {
            // Parse parameters
            int ingredientId = Integer.parseInt(ingredientIdStr);
            int unitId = Integer.parseInt(unitIdStr);
            BigDecimal stockQuantity = new BigDecimal(stockQuantityStr);
            int supplierId = Integer.parseInt(supplierIdStr);
            boolean active = "true".equals(activeStr);
            
            // Create ingredient object
            Ingredient ingredient = new Ingredient(ingredientId, name, unitId, stockQuantity, supplierId, active, null);
            ingredient.setDescription(description);  // Set description
            
            // Update ingredient
            IngredientResult result = ingredientService.updateIngredient(ingredient);
            
            if (result.isSuccess()) {
                response.sendRedirect(request.getContextPath() + "/ingredient?success=" + 
                    java.net.URLEncoder.encode("Cập nhật nguyên liệu thành công!", "UTF-8"));
                return;
            } else {
                request.setAttribute("error", result.getMessage());
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu nhập không hợp lệ");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        // If error, show form again
        request.setAttribute("id", ingredientIdStr);
        showEditForm(request, response);
    }
    
    /**
     * Handle delete ingredient
     */
    private void handleDeleteIngredient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredientIdStr = request.getParameter("id");
        
        if (ingredientIdStr == null || ingredientIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("ID nguyên liệu không hợp lệ", "UTF-8"));
            return;
        }
        
        try {
            int ingredientId = Integer.parseInt(ingredientIdStr);
            
            IngredientResult result = ingredientService.deleteIngredient(ingredientId);
            
            if (result.isSuccess()) {
                response.sendRedirect(request.getContextPath() + "/ingredient?success=" + 
                    java.net.URLEncoder.encode("Xóa nguyên liệu thành công!", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                    java.net.URLEncoder.encode(result.getMessage(), "UTF-8"));
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("ID nguyên liệu không hợp lệ", "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("Có lỗi xảy ra: " + e.getMessage(), "UTF-8"));
        }
    }
    
    /**
     * Handle toggle ingredient active status
     */
    private void handleToggleIngredient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredientIdStr = request.getParameter("id");
        
        if (ingredientIdStr == null || ingredientIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("ID nguyên liệu không hợp lệ", "UTF-8"));
            return;
        }
        
        try {
            int ingredientId = Integer.parseInt(ingredientIdStr);
            
            IngredientResult result = ingredientService.toggleIngredient(ingredientId);
            
            if (result.isSuccess()) {
                response.sendRedirect(request.getContextPath() + "/ingredient?success=" + 
                    java.net.URLEncoder.encode(result.getMessage(), "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                    java.net.URLEncoder.encode(result.getMessage(), "UTF-8"));
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("ID nguyên liệu không hợp lệ", "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("Có lỗi xảy ra: " + e.getMessage(), "UTF-8"));
        }
    }
    
    /**
     * Handle update stock
     */
    private void handleUpdateStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ingredientIdStr = request.getParameter("ingredientId");
        String newStockStr = request.getParameter("newQuantity");
        
        if (ingredientIdStr == null || ingredientIdStr.isEmpty() || 
            newStockStr == null || newStockStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("Dữ liệu không hợp lệ", "UTF-8"));
            return;
        }
        
        try {
            int ingredientId = Integer.parseInt(ingredientIdStr);
            BigDecimal newStock = new BigDecimal(newStockStr);
            
            IngredientResult result = ingredientService.updateStockQuantity(ingredientId, newStock);
            
            if (result.isSuccess()) {
                response.sendRedirect(request.getContextPath() + "/ingredient?success=" + 
                    java.net.URLEncoder.encode("Cập nhật tồn kho thành công!", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                    java.net.URLEncoder.encode(result.getMessage(), "UTF-8"));
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("Dữ liệu số không hợp lệ", "UTF-8"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ingredient?error=" + 
                java.net.URLEncoder.encode("Có lỗi xảy ra: " + e.getMessage(), "UTF-8"));
        }
    }
}
