/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import model.Ingredient;
import service.IngredientService;
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
 * Controller for Inventory Staff Dashboard
 * @author Hòa
 */
@WebServlet(name = "InventoryDashboardController", urlPatterns = {"/inventory/dashboard"})
public class InventoryDashboardController extends HttpServlet {
    
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
        
        // Check if user has permission to access inventory dashboard
        if (!"Inventory".equals(currentUserRole) && !"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }
        
        try {
            // Get dashboard data
            Map<String, Object> dashboardData = getDashboardData();
            
            // Set attributes for JSP
            for (Map.Entry<String, Object> entry : dashboardData.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
            
            // Forward to centralized dashboard JSP
            request.getRequestDispatcher("/WEB-INF/views/dashboard/inventory.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải dashboard: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/dashboard/inventory.jsp").forward(request, response);
        }
    }
    
    /**
     * Get all dashboard data
     */
    private Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // Get all ingredients for analysis - use large page size to get all
            List<Ingredient> allIngredients = ingredientService.getAllIngredients(1, 1000, null, null, true);
            
            // Calculate statistics
            int totalIngredients = allIngredients.size();
            int stockGoodCount = 0;
            int stockLowCount = 0;
            int stockOutCount = 0;
            
            // For this demo, let's use simple logic with stockQuantity
            // Categorize ingredients by stock status
            for (Ingredient ingredient : allIngredients) {
                BigDecimal stock = ingredient.getStockQuantity();
                if (stock.compareTo(BigDecimal.ZERO) <= 0) {
                    stockOutCount++;
                } else if (stock.compareTo(new BigDecimal("10")) <= 0) { // Consider stock <= 10 as low
                    stockLowCount++;
                } else {
                    stockGoodCount++;
                }
            }
            
            // Get low stock ingredients for alerts
            List<Ingredient> lowStockIngredients = getLowStockItems(allIngredients);
            
            // Get ingredients for chart data (limit to top 10 for readability)
            List<Ingredient> chartIngredients = allIngredients;
            if (chartIngredients.size() > 10) {
                chartIngredients = chartIngredients.subList(0, 10);
            }
            
            // Set data
            data.put("totalIngredients", totalIngredients);
            data.put("stockGoodCount", stockGoodCount);
            data.put("stockLowCount", stockLowCount);
            data.put("stockOutCount", stockOutCount);
            data.put("ingredients", chartIngredients);
            data.put("lowStockIngredients", lowStockIngredients);
            
            // Additional statistics
            data.put("totalValue", calculateTotalInventoryValue(allIngredients));
            data.put("criticalItemsCount", getCriticalItemsCount(allIngredients));
            
        } catch (Exception e) {
            e.printStackTrace();
            // Set default values in case of error
            data.put("totalIngredients", 0);
            data.put("stockGoodCount", 0);
            data.put("stockLowCount", 0);
            data.put("stockOutCount", 0);
            data.put("ingredients", List.of());
            data.put("lowStockIngredients", List.of());
            data.put("totalValue", 0.0);
            data.put("criticalItemsCount", 0);
        }
        
        return data;
    }
    
    /**
     * Get low stock items from ingredients list
     */
    private List<Ingredient> getLowStockItems(List<Ingredient> ingredients) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getStockQuantity().compareTo(new BigDecimal("10")) <= 0)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Calculate total inventory value (simplified - using stockQuantity as value)
     */
    private double calculateTotalInventoryValue(List<Ingredient> ingredients) {
        double totalValue = 0.0;
        for (Ingredient ingredient : ingredients) {
            // For demo purposes, assume each unit costs $1
            totalValue += ingredient.getStockQuantity().doubleValue();
        }
        return totalValue;
    }
    
    /**
     * Count critical items (out of stock or very low)
     */
    private int getCriticalItemsCount(List<Ingredient> ingredients) {
        int criticalCount = 0;
        for (Ingredient ingredient : ingredients) {
            BigDecimal stock = ingredient.getStockQuantity();
            if (stock.compareTo(BigDecimal.ZERO) <= 0 || 
                stock.compareTo(new BigDecimal("5")) <= 0) { // Consider stock <= 5 as critical
                criticalCount++;
            }
        }
        return criticalCount;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle AJAX requests for dashboard refresh
        doGet(request, response);
    }
}