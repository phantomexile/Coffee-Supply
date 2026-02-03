package controller;

import dao.ProductBOMDAO;
import dao.ProductDAO;
import dao.IngredientDAO;
import dao.SettingDAO;
import service.RecipeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.Ingredient;
import model.Setting;
import model.BOMItem;

@WebServlet(name = "RecipeController", urlPatterns = {"/admin/recipe"})
public class RecipeController extends HttpServlet {
    private final ProductBOMDAO bomDAO = new ProductBOMDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final IngredientDAO ingredientDAO = new IngredientDAO();
    private final SettingDAO settingDAO = new SettingDAO();
    private final RecipeService recipeService = new RecipeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        String currentUserRole = (String) session.getAttribute("roleName");
        if (!"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền truy cập chức năng này");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        try {
            switch (action) {
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response, request.getParameter("id"));
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/products");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi hệ thống");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        String currentUserRole = (String) session.getAttribute("roleName");
        if (!"Admin".equals(currentUserRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ Admin mới có quyền thêm hoặc chỉnh sửa công thức");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/products");
            return;
        }

        try {
            switch (action) {
                case "save":
                    saveRecipe(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi hệ thống");
        }
    }
    
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if productId is provided in query parameter (from product detail page)
        String productIdParam = request.getParameter("productId");
        Product selectedProduct = null;
        
        if (productIdParam != null && !productIdParam.isEmpty()) {
            try {
                int productId = Integer.parseInt(productIdParam);
                selectedProduct = productDAO.getProductById(productId);
                if (selectedProduct != null && recipeService.hasRecipe(productId)) {
                    response.sendRedirect(request.getContextPath() + "/admin/recipe?action=edit&id=" + productId);
                    return;
                }
            } catch (NumberFormatException e) {
                // Invalid productId, ignore
            }
        }
        
        List<Product> productsWithoutFormula = new ArrayList<>();
        if (selectedProduct == null) {
            List<Product> allProducts = productDAO.getAllProducts(1, 10000, null, null, true);
            for (Product p : allProducts) {
                if (!recipeService.hasRecipe(p.getProductID())) {
                    productsWithoutFormula.add(p);
                }
            }
        }
        
        List<Ingredient> ingredients = ingredientDAO.getAllIngredients(1, 100, null, null, null);
        List<Setting> units = settingDAO.getSettingsByType("Unit");
        
        HttpSession session = request.getSession(false);
        String successMessage = (String) session.getAttribute("recipeSuccessMessage");
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
            session.removeAttribute("recipeSuccessMessage");
        }
        
        request.setAttribute("products", productsWithoutFormula);
        request.setAttribute("product", selectedProduct);
        request.setAttribute("ingredients", ingredients);
        request.setAttribute("units", units);
        request.getRequestDispatcher("/WEB-INF/views/admin/recipe-form.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String productIdStr)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(productIdStr);
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                return;
            }
            
            List<BOMItem> bomItems = bomDAO.getBOMItemsByProductId(productId);
            List<Ingredient> ingredients = ingredientDAO.getAllIngredients(1, 10000, null, null, null);
            List<Setting> units = settingDAO.getSettingsByType("Unit");
            
            HttpSession session = request.getSession(false);
            String successMessage = (String) session.getAttribute("recipeSuccessMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("recipeSuccessMessage");
            }
            
            request.setAttribute("product", product);
            request.setAttribute("bomItems", bomItems);
            request.setAttribute("ingredients", ingredients);
            request.setAttribute("units", units);
            request.getRequestDispatcher("/WEB-INF/views/admin/recipe-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
        }
    }
    
    private void saveRecipe(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String[] ingredientIds = request.getParameterValues("ingredientId");
            String[] quantities = request.getParameterValues("quantity");
            String[] unitIds = request.getParameterValues("unitId");
            
            boolean isNewRecipe = !recipeService.hasRecipe(productId);
            
            // Save recipe through service (includes validation)
            String errorMessage = recipeService.saveRecipe(productId, ingredientIds, quantities, unitIds);
            
            if (errorMessage != null) {
                // Validation failed
                HttpSession session = request.getSession(false);
                session.setAttribute("recipeErrorMessage", errorMessage);
                String redirect = isNewRecipe
                        ? (request.getContextPath() + "/admin/recipe?action=add&productId=" + productId)
                        : (request.getContextPath() + "/admin/recipe?action=edit&id=" + productId);
                response.sendRedirect(redirect);
                return;
            }
            
            // Success
            String successMessage = recipeService.getSuccessMessage(productId, isNewRecipe);
            HttpSession session = request.getSession(false);
            session.setAttribute("recipeSuccessMessage", successMessage);
            
            response.sendRedirect(request.getContextPath() + "/admin/products/view?id=" + productId + "#recipe-tab");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lưu công thức");
        }
    }
}

