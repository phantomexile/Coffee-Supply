package service;

import dao.ProductBOMDAO;
import dao.ProductDAO;
import model.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for Recipe (BOM) management
 */
public class RecipeService {
    private ProductBOMDAO bomDAO;
    private ProductDAO productDAO;
    
    public RecipeService() {
        this.bomDAO = new ProductBOMDAO();
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Validate recipe data
     * @param ingredientIds Array of ingredient IDs
     * @param quantities Array of quantities
     * @param unitIds Array of unit IDs
     * @return Error message if validation fails, null if valid
     */
    public String validateRecipe(String[] ingredientIds, String[] quantities, String[] unitIds) {
        // Validate: không cho phép trùng nguyên liệu
        if (ingredientIds != null && ingredientIds.length > 0) {
            List<String> selectedIngredientIds = new ArrayList<>();
            for (String id : ingredientIds) {
                if (id == null) continue;
                String trimmed = id.trim();
                if (!trimmed.isEmpty()) {
                    selectedIngredientIds.add(trimmed);
                }
            }
            Set<String> uniqueIngredientIds = new HashSet<>(selectedIngredientIds);
            if (uniqueIngredientIds.size() != selectedIngredientIds.size()) {
                return "Không được chọn trùng nguyên liệu trong một công thức.";
            }
        }
        
        // Validate: số lượng không được vượt quá 4 chữ số (tối đa 9999)
        if (quantities != null) {
            for (int i = 0; i < quantities.length; i++) {
                if (quantities[i] != null && !quantities[i].trim().isEmpty()) {
                    try {
                        BigDecimal quantity = new BigDecimal(quantities[i]);
                        int integerPart = quantity.abs().intValue();
                        if (integerPart > 9999) {
                            return "Số lượng không được vượt quá 4 chữ số (tối đa 9999).";
                        }
                    } catch (NumberFormatException e) {
                        return "Số lượng không hợp lệ.";
                    }
                }
            }
        }
        
        // Validate: unitId là bắt buộc
        if (ingredientIds != null && unitIds != null) {
            for (int i = 0; i < ingredientIds.length; i++) {
                if (ingredientIds[i] != null && !ingredientIds[i].trim().isEmpty()) {
                    if (i >= unitIds.length || unitIds[i] == null || unitIds[i].isEmpty()) {
                        return "Đơn vị (unit) là bắt buộc.";
                    }
                }
            }
        }
        
        return null; // Valid
    }
    
    /**
     * Save recipe (BOM) for a product
     * @param productId Product ID
     * @param ingredientIds Array of ingredient IDs
     * @param quantities Array of quantities
     * @param unitIds Array of unit IDs
     * @return Error message if failed, null if successful
     */
    public String saveRecipe(int productId, String[] ingredientIds, String[] quantities, String[] unitIds) {
        // Validate
        String validationError = validateRecipe(ingredientIds, quantities, unitIds);
        if (validationError != null) {
            return validationError;
        }
        
        try {
            // Xóa BOM cũ
            bomDAO.deleteBOMByProductId(productId);
            
            // Thêm BOM mới
            if (ingredientIds != null && quantities != null && unitIds != null) {
                for (int i = 0; i < ingredientIds.length; i++) {
                    if (ingredientIds[i] == null || ingredientIds[i].trim().isEmpty()) {
                        continue;
                    }
                    
                    int ingredientId = Integer.parseInt(ingredientIds[i]);
                    BigDecimal quantity = new BigDecimal(quantities[i]);
                    Integer unitId = Integer.parseInt(unitIds[i]);
                    int displayOrder = i + 1;
                    
                    bomDAO.insertBOMItem(productId, ingredientId, quantity, unitId, displayOrder);
                }
            }
            
            return null; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi lưu công thức: " + e.getMessage();
        }
    }
    
    /**
     * Check if product has recipe (BOM)
     * @param productId Product ID
     * @return true if has BOM, false otherwise
     */
    public boolean hasRecipe(int productId) {
        return bomDAO.hasBOM(productId);
    }
    
    /**
     * Get success message for recipe save
     * @param productId Product ID
     * @param isNewRecipe true if new recipe, false if update
     * @return Success message
     */
    public String getSuccessMessage(int productId, boolean isNewRecipe) {
        Product product = productDAO.getProductById(productId);
        if (isNewRecipe) {
            return "Thêm công thức thành công cho sản phẩm: " + (product != null ? product.getProductName() : "");
        } else {
            return "Cập nhật công thức thành công cho sản phẩm: " + (product != null ? product.getProductName() : "");
        }
    }
}

