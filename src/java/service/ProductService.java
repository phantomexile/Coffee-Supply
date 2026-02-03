package service;

import dao.ProductDAO;
import model.Product;
import model.BOMItem;
import java.util.List;
import java.math.BigDecimal;

public class ProductService {
    
    private ProductDAO productDAO;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Get all products with pagination and filters
     */
    public List<Product> getAllProducts(int page, int pageSize, String searchTerm, Integer categoryId, Boolean isActive) {
        return productDAO.getAllProducts(page, pageSize, searchTerm, categoryId, isActive);
    }
    
    /**
     * Get all products with pagination, filters and sorting
     */
    public List<Product> getAllProducts(int page, int pageSize, String searchTerm, Integer categoryId, Boolean isActive, String sortBy, String sortOrder) {
        return productDAO.getAllProducts(page, pageSize, searchTerm, categoryId, isActive, sortBy, sortOrder);
    }
    
    /**
     * Get total count for pagination
     */
    public int getTotalProductsCount(String searchTerm, Integer categoryId, Boolean isActive) {
        return productDAO.getTotalProductsCount(searchTerm, categoryId, isActive);
    }
    
    /**
     * Get product by ID
     */
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }
    
    /**
     * Create new product with validation
     */
    public boolean createProduct(Product product) throws Exception {
        // Validate product data
        validateProduct(product);
        
        // Check if product name already exists
        if (isProductNameExists(product.getProductName(), null)) {
            throw new Exception("Sản phẩm '" + product.getProductName() + "' đã tồn tại. Vui lòng chọn tên khác.");
        }
        
        return productDAO.createProduct(product);
    }
    
    /**
     * Update product with validation
     */
    public boolean updateProduct(Product product) throws Exception {
        // Validate product data
        validateProduct(product);
        
        // Check if product name already exists (excluding current product)
        if (isProductNameExists(product.getProductName(), product.getProductID())) {
            throw new Exception("Sản phẩm '" + product.getProductName() + "' đã tồn tại. Vui lòng chọn tên khác.");
        }
        
        return productDAO.updateProduct(product);
    }
    
    /**
     * Delete product (soft delete)
     */
    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }
    
    /**
     * Get categories for dropdown
     */
    public List<Object[]> getCategories() {
        return productDAO.getCategories();
    }
    
    /**
     * Validate product data
     */
    private void validateProduct(Product product) throws Exception {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new Exception("Tên sản phẩm không được để trống");
        }
        
        if (product.getProductName().length() > 100) {
            throw new Exception("Tên sản phẩm không được vượt quá 100 ký tự");
        }
        
        if (product.getDescription() != null && product.getDescription().length() > 255) {
            throw new Exception("Mô tả không được vượt quá 255 ký tự");
        }
        
        if (product.getCategoryID() <= 0) {
            throw new Exception("Vui lòng chọn danh mục");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Giá sản phẩm phải lớn hơn 0");
        }
    }
    
    private boolean isProductNameExists(String productName, Integer excludeProductId) {
        return productDAO.isProductNameExists(productName, excludeProductId);
    }
    
    /**
     * Calculate total pages for pagination
     */
    public int getTotalPages(int totalRecords, int pageSize) {
        return (int) Math.ceil((double) totalRecords / pageSize);
    }
    
    /**
     * Get active products only
     */
    public List<Product> getActiveProducts(int page, int pageSize, String searchTerm, Integer categoryId) {
        return productDAO.getAllProducts(page, pageSize, searchTerm, categoryId, true);
    }
    
    /**
     * Toggle product status
     */
    public boolean toggleProductStatus(int productId) {
        Product product = productDAO.getProductById(productId);
        if (product != null) {
            product.setActive(!product.isActive());
            return productDAO.updateProduct(product);
        }
        return false;
    }
    
    /**
     * Format recipe text from BOM items
     * Format: "18 g Cà phê Espresso xay + 180 ml Sữa tươi nguyên kem + 5 g Đường trắng"
     * @param bomItems List of BOM items
     * @return Formatted recipe string
     */
    public String formatRecipe(List<BOMItem> bomItems) {
        if (bomItems == null || bomItems.isEmpty()) {
            return "";
        }
        
        StringBuilder recipe = new StringBuilder();
        
        for (int i = 0; i < bomItems.size(); i++) {
            BOMItem item = bomItems.get(i);
            
            // Add "+" separator if not first item
            if (i > 0) {
                recipe.append(" + ");
            }
            
            // Format quantity: remove trailing zeros (18.0000 -> 18, 18.5 -> 18.5)
            BigDecimal quantity = item.getQuantity();
            if (quantity != null) {
                String qtyStr = quantity.stripTrailingZeros().toPlainString();
                
                // Build recipe part: "qty unitName ingredientName"
                recipe.append(qtyStr)
                      .append(" ")
                      .append(item.getUnitName() != null ? item.getUnitName() : "")
                      .append(" ")
                      .append(item.getIngredientName() != null ? item.getIngredientName() : "");
            }
        }
        
        return recipe.toString();
    }
}