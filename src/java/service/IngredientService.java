/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.IngredientDAO;
import model.Ingredient;
import model.Setting;
import model.Supplier;
import java.util.List;
import java.math.BigDecimal;

/**
 * Service class for Ingredient management
 *
 * @author Hòa
 */
public class IngredientService {

    private IngredientDAO ingredientDAO;
    private SupplierService supplierService;

    public IngredientService() {
        this.ingredientDAO = new IngredientDAO();
        this.supplierService = new SupplierService();
    }

    /**
     * Get all ingredients with pagination and filtering
     *
     * @param page Page number
     * @param pageSize Page size
     * @param supplierFilter Supplier filter
     * @param searchKeyword Search keyword
     * @param activeOnly Show only active ingredients
     * @return List of ingredients
     */
    public List<Ingredient> getAllIngredients(int page, int pageSize, Integer supplierFilter,
            String searchKeyword, Boolean activeOnly) {
        return ingredientDAO.getAllIngredients(page, pageSize, supplierFilter, searchKeyword, activeOnly);
    }

    /**
     * Get total count of ingredients with filters
     *
     * @param supplierFilter Supplier filter
     * @param searchKeyword Search keyword
     * @param activeOnly Show only active ingredients
     * @return Total count
     */
    public int getTotalIngredientsCount(Integer supplierFilter, String searchKeyword, Boolean activeOnly) {
        return ingredientDAO.getTotalIngredientsCount(supplierFilter, searchKeyword, activeOnly);
    }

    /**
     * Get ingredient by ID
     *
     * @param ingredientID Ingredient ID
     * @return Ingredient object
     */
    public Ingredient getIngredientById(int ingredientID) {
        return ingredientDAO.getIngredientById(ingredientID);
    }

    /**
     * Validate ingredient name
     *
     * @param name Ingredient name to validate
     * @return Error message if invalid, null if valid
     */
    private String validateIngredientName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên nguyên liệu không được để trống";
        }
        
        String trimmedName = name.trim();
        
        if (trimmedName.length() > 50) {
            return "Tên nguyên liệu không vượt quá 50 kí tự";
        }
        
        // Check for invalid characters (only allow letters, numbers, spaces, and hyphens)
        if (!trimmedName.matches("^[\\p{L}0-9 _\\-]+$")) {
            return "Tên nguyên liệu chứa ký tự không hợp lệ";
        }
        
        // Check if name starts or ends with hyphen or space
        if (trimmedName.startsWith("-") || trimmedName.endsWith("-") || 
            trimmedName.startsWith(" ") || trimmedName.endsWith(" ")) {
            return "Tên nguyên liệu không được bắt đầu hoặc kết thúc bằng dấu gạch ngang hoặc khoảng trắng";
        }
        
        // Check for consecutive hyphens or spaces
        if (trimmedName.contains("--") || trimmedName.contains("  ")) {
            return "Tên nguyên liệu không được chứa dấu gạch ngang hoặc khoảng trắng liên tiếp";
        }
        
        // Check if name is just a negative number (like "-5", "-10")
        if (trimmedName.matches("^-\\d+$")) {
            return "Tên nguyên liệu không được là số âm";
        }
        
        // Check if name starts with a number (optional - you can remove this if you want to allow it)
        // if (trimmedName.matches("^\\d")) {
        //     return "Tên nguyên liệu không được bắt đầu bằng số";
        // }
        
        return null; // Valid
    }

    /**
     * Create new ingredient with validation
     *
     * @param ingredient Ingredient to create
     * @return Result object with success status and message
     */
    public IngredientResult createIngredient(Ingredient ingredient) {
        // Validate name
        String nameValidationError = validateIngredientName(ingredient.getName());
        if (nameValidationError != null) {
            return new IngredientResult(false, nameValidationError);
        }
        
        // Validate description (tối đa 255 ký tự)
        if (ingredient.getDescription() != null && ingredient.getDescription().length() > 255) {
            return new IngredientResult(false, "Mô tả không được vượt quá 255 ký tự");
        }
        
        if (ingredient.getStockQuantity() == null || ingredient.getStockQuantity().compareTo(BigDecimal.ZERO) < 0) {
            return new IngredientResult(false, "Số lượng tồn kho phải lớn hơn hoặc bằng 0");
        }
        if (ingredient.getStockQuantity() != null && ingredient.getStockQuantity().compareTo(new BigDecimal("1000")) > 0) {
            return new IngredientResult(false, "Số lượng tồn kho không vượt quá 1000");
        }

        // Trim name before checking
        String trimmedName = ingredient.getName().trim();
        
        // Check if ingredient with same name and supplier already exists
        System.out.println("DEBUG: Checking duplicate - Name: '" + trimmedName + "', SupplierID: " + ingredient.getSupplierID());
        boolean exists = ingredientDAO.isIngredientNameAndSupplierExists(trimmedName, ingredient.getSupplierID(), null);
        System.out.println("DEBUG: Duplicate check result: " + exists);
        if (exists) {
            return new IngredientResult(false, "Tên nguyên liệu này đã tồn tại trong nhà cung cấp");
        }

        // Set default values
        ingredient.setName(trimmedName);
        if (ingredient.getStockQuantity() == null) {
            ingredient.setStockQuantity(BigDecimal.ZERO);
        }

        int ingredientID = ingredientDAO.createIngredient(ingredient);

        if (ingredientID > 0) {
            return new IngredientResult(true, "Thêm nguyên liệu thành công", ingredientID);
        } else {
            return new IngredientResult(false, "Có lỗi xảy ra khi thêm nguyên liệu");
        }
    }

    /**
     * Update ingredient with validation
     *
     * @param ingredient Ingredient to update
     * @return Result object with success status and message
     */
    public IngredientResult updateIngredient(Ingredient ingredient) {
        // Validate input
        if (ingredient.getIngredientID() <= 0) {
            return new IngredientResult(false, "ID nguyên liệu không hợp lệ");
        }
        
        // Validate name
        String nameValidationError = validateIngredientName(ingredient.getName());
        if (nameValidationError != null) {
            return new IngredientResult(false, nameValidationError);
        }
        
        // Validate description (tối đa 255 ký tự)
        if (ingredient.getDescription() != null && ingredient.getDescription().length() > 255) {
            return new IngredientResult(false, "Mô tả không được vượt quá 255 ký tự");
        }
        
        if (ingredient.getStockQuantity() == null || ingredient.getStockQuantity().compareTo(BigDecimal.ZERO) < 0) {
            return new IngredientResult(false, "Số lượng tồn kho phải lớn hơn hoặc bằng 0");
        }
        if (ingredient.getStockQuantity() != null && ingredient.getStockQuantity().compareTo(new BigDecimal("1000")) > 0) {
            return new IngredientResult(false, "Số lượng tồn kho không vượt quá 1000");
        }

        // Check if ingredient exists
        Ingredient existingIngredient = ingredientDAO.getIngredientById(ingredient.getIngredientID());
        if (existingIngredient == null) {
            return new IngredientResult(false, "Nguyên liệu không tồn tại");
        }

        // Trim name before checking
        String trimmedName = ingredient.getName().trim();
        
        // Check if ingredient with same name and supplier already exists (excluding current ingredient)
        if (ingredientDAO.isIngredientNameAndSupplierExists(trimmedName, ingredient.getSupplierID(), ingredient.getIngredientID())) {
            return new IngredientResult(false, "Nguyên liệu với tên này đã tồn tại cho nhà cung cấp này");
        }

        // Set values
        ingredient.setName(trimmedName);

        boolean success = ingredientDAO.updateIngredient(ingredient);

        if (success) {
            return new IngredientResult(true, "Cập nhật nguyên liệu thành công");
        } else {
            return new IngredientResult(false, "Có lỗi xảy ra khi cập nhật nguyên liệu");
        }
    }

    /**
     * Delete ingredient (soft delete)
     *
     * @param ingredientID Ingredient ID to delete
     * @return Result object with success status and message
     */
    public IngredientResult deleteIngredient(int ingredientID) {
        if (ingredientID <= 0) {
            return new IngredientResult(false, "ID nguyên liệu không hợp lệ");
        }

        // Check if ingredient exists
        Ingredient existingIngredient = ingredientDAO.getIngredientById(ingredientID);
        if (existingIngredient == null) {
            return new IngredientResult(false, "Nguyên liệu không tồn tại");
        }

        if (!existingIngredient.isActive()) {
            return new IngredientResult(false, "Nguyên liệu đã bị xóa");
        }

        boolean success = ingredientDAO.deleteIngredient(ingredientID);

        if (success) {
            return new IngredientResult(true, "Xóa nguyên liệu thành công");
        } else {
            return new IngredientResult(false, "Có lỗi xảy ra khi xóa nguyên liệu");
        }
    }

    /**
     * Get all available units
     *
     * @return List of units
     */
    public List<Setting> getAvailableUnits() {
        return ingredientDAO.getAvailableUnits();
    }

    /**
     * Get all active suppliers
     *
     * @return List of suppliers
     */
    public List<Supplier> getActiveSuppliers() {
        return supplierService.getActiveSuppliers();
    }

    /**
     * Get ingredients by supplier ID
     *
     * @param supplierId Supplier ID
     * @return List of ingredients from this supplier
     */
    public List<Ingredient> getIngredientsBySupplier(int supplierId) {
        return ingredientDAO.getIngredientsBySupplier(supplierId);
    }

    /**
     * Get ingredients with low stock
     *
     * @param threshold Minimum stock threshold
     * @return List of low stock ingredients
     */
    public List<Ingredient> getLowStockIngredients(BigDecimal threshold) {
        if (threshold == null) {
            threshold = new BigDecimal("10.0"); // Default threshold
        }
        return ingredientDAO.getLowStockIngredients(threshold);
    }

    /**
     * Toggle ingredient active status
     *
     * @param ingredientID Ingredient ID
     * @return Result object with success status and message
     */
    public IngredientResult toggleIngredient(int ingredientID) {
        if (ingredientID <= 0) {
            return new IngredientResult(false, "ID nguyên liệu không hợp lệ");
        }

        // Check if ingredient exists
        Ingredient existingIngredient = ingredientDAO.getIngredientById(ingredientID);
        if (existingIngredient == null) {
            return new IngredientResult(false, "Nguyên liệu không tồn tại");
        }

        boolean success = ingredientDAO.toggleIngredient(ingredientID);

        if (success) {
            return new IngredientResult(true, "Cập nhật trạng thái nguyên liệu thành công");
        } else {
            return new IngredientResult(false, "Có lỗi xảy ra khi cập nhật trạng thái nguyên liệu");
        }
    }

    /**
     * Update ingredient stock quantity
     *
     * @param ingredientID Ingredient ID
     * @param newQuantity New stock quantity
     * @return Result object
     */
    public IngredientResult updateStockQuantity(int ingredientID, BigDecimal newQuantity) {
        if (ingredientID <= 0) {
            return new IngredientResult(false, "ID nguyên liệu không hợp lệ");
        }

        if (newQuantity == null || newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            return new IngredientResult(false, "Số lượng tồn kho phải lớn hơn hoặc bằng 0");
        }
        if (newQuantity != null && newQuantity.compareTo(new BigDecimal("1000")) > 0) {
            return new IngredientResult(false, "Số lượng tồn kho không vượt quá 1000");
        }

        // Check if ingredient exists
        Ingredient existingIngredient = ingredientDAO.getIngredientById(ingredientID);
        if (existingIngredient == null) {
            return new IngredientResult(false, "Nguyên liệu không tồn tại");
        }

        boolean success = ingredientDAO.updateStockQuantity(ingredientID, newQuantity);

        if (success) {
            return new IngredientResult(true, "Cập nhật số lượng tồn kho thành công");
        } else {
            return new IngredientResult(false, "Có lỗi xảy ra khi cập nhật số lượng tồn kho");
        }
    }

    /**
     * Increase ingredient stock quantity (for receiving purchase orders)
     * 
     * @param ingredientID Ingredient ID
     * @param quantityToAdd Quantity to add to current stock
     * @return Result object
     */
    public IngredientResult increaseStockQuantity(int ingredientID, BigDecimal quantityToAdd) {
        if (ingredientID <= 0) {
            return new IngredientResult(false, "ID nguyên liệu không hợp lệ");
        }

        if (quantityToAdd == null || quantityToAdd.compareTo(BigDecimal.ZERO) <= 0) {
            return new IngredientResult(false, "Số lượng nhập phải lớn hơn 0");
        }

        // Check if ingredient exists
        Ingredient existingIngredient = ingredientDAO.getIngredientById(ingredientID);
        if (existingIngredient == null) {
            return new IngredientResult(false, "Nguyên liệu không tồn tại");
        }

        boolean success = ingredientDAO.increaseStockQuantity(ingredientID, quantityToAdd);

        if (success) {
            return new IngredientResult(true, "Cập nhật tồn kho thành công: +" + quantityToAdd);
        } else {
            return new IngredientResult(false, "Có lỗi xảy ra khi cập nhật tồn kho");
        }
    }

    /**
     * Inner class for service operation results
     */
    public static class IngredientResult {

        private boolean success;
        private String message;
        private int generatedId;

        public IngredientResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public IngredientResult(boolean success, String message, int generatedId) {
            this.success = success;
            this.message = message;
            this.generatedId = generatedId;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public int getGeneratedId() {
            return generatedId;
        }
    }
}
