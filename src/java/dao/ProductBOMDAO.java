package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.BOMItem;

public class ProductBOMDAO extends BaseDAO {

    /**
     * Returns a concatenated formula string for a given product, e.g. "18 g Cà phê + 180 ml Sữa".
     */
    public String getFormulaByProductId(int productId) {
        String sql =
            "SELECT STRING_AGG(CONCAT(b.Quantity, ' ', u.Value, ' ', i.Name), ' + ' ORDER BY b.DisplayOrder) AS Formula " +
            "FROM ProductBOM b " +
            "JOIN Ingredient i ON i.IngredientID = b.IngredientID " +
            "LEFT JOIN Setting u ON u.SettingID = COALESCE(b.UnitID, i.UnitID) " +
            "WHERE b.ProductID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Formula");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * List products with aggregated formula for admin menu view.
     * Only returns products that have a formula (BOM).
     */
    public List<Product> getProductsWithFormula(int page, int pageSize, String searchTerm, Integer categoryId) {
        List<Product> products = new ArrayList<>();
        String sql =
            "SELECT DISTINCT p.*, s1.Value AS CategoryName, " +
            "       (SELECT STRING_AGG(CONCAT(b.Quantity, ' ', u.Value, ' ', i.Name), ' + ' ORDER BY b.DisplayOrder) " +
            "          FROM ProductBOM b " +
            "          JOIN Ingredient i ON i.IngredientID = b.IngredientID " +
            "          LEFT JOIN Setting u ON u.SettingID = COALESCE(b.UnitID, i.UnitID) " +
            "         WHERE b.ProductID = p.ProductID) AS Formula " +
            "FROM Product p " +
            "INNER JOIN ProductBOM bom ON bom.ProductID = p.ProductID " +
            "LEFT JOIN Setting s1 ON p.CategoryID = s1.SettingID " +
            "WHERE p.IsActive = TRUE ";

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += "AND (LOWER(p.ProductName) LIKE LOWER(?) OR LOWER(p.Description) LIKE LOWER(?)) ";
        }
        
        if (categoryId != null && categoryId > 0) {
            sql += "AND p.CategoryID = ? ";
        }

        sql += "ORDER BY p.ProductID ASC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int param = 1;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String pattern = "%" + searchTerm + "%";
                ps.setString(param++, pattern);
                ps.setString(param++, pattern);
            }
            if (categoryId != null && categoryId > 0) {
                ps.setInt(param++, categoryId);
            }
            ps.setInt(param++, pageSize);
            ps.setInt(param++, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setDescription(rs.getString("Description"));
                    product.setImageUrl(rs.getString("ImageUrl"));
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setPrice(rs.getBigDecimal("Price"));
                    product.setActive(rs.getBoolean("IsActive"));
                    product.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    product.setCategoryName(rs.getString("CategoryName"));
                    product.setFormula(rs.getString("Formula"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Get total count of products that have formulas.
     */
    public int getTotalProductsWithFormulaCount(String searchTerm, Integer categoryId) {
        String sql =
            "SELECT COUNT(DISTINCT p.ProductID) " +
            "FROM Product p " +
            "INNER JOIN ProductBOM bom ON bom.ProductID = p.ProductID " +
            "WHERE p.IsActive = TRUE ";

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += "AND (LOWER(p.ProductName) LIKE LOWER(?) OR LOWER(p.Description) LIKE LOWER(?)) ";
        }
        
        if (categoryId != null && categoryId > 0) {
            sql += "AND p.CategoryID = ? ";
        }

        sql += "AND EXISTS (" +
               "    SELECT 1 FROM ProductBOM b " +
               "    WHERE b.ProductID = p.ProductID" +
               ")";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int param = 1;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String pattern = "%" + searchTerm + "%";
                ps.setString(param++, pattern);
                ps.setString(param++, pattern);
            }
            if (categoryId != null && categoryId > 0) {
                ps.setInt(param++, categoryId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Check if a product has BOM (recipe)
     */
    public boolean hasBOM(int productId) {
        String sql = "SELECT COUNT(*) FROM ProductBOM WHERE ProductID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all BOM items for a product
     */
    public List<BOMItem> getBOMItemsByProductId(int productId) {
        List<BOMItem> items = new ArrayList<>();
        // Không dùng BOMID vì có thể không tồn tại trong bảng
        String sql = "SELECT b.ProductID, b.IngredientID, b.Quantity, b.UnitID, b.DisplayOrder, " +
                     "i.Name AS IngredientName, u.Value AS UnitName " +
                     "FROM ProductBOM b " +
                     "JOIN Ingredient i ON i.IngredientID = b.IngredientID " +
                     "LEFT JOIN Setting u ON u.SettingID = COALESCE(b.UnitID, i.UnitID) " +
                     "WHERE b.ProductID = ? " +
                     "ORDER BY b.DisplayOrder";

        System.out.println("DEBUG: getBOMItemsByProductId - ProductID: " + productId);
        System.out.println("DEBUG: SQL: " + sql);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            System.out.println("DEBUG: Executing query with ProductID: " + productId);
            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count++;
                    BOMItem item = new BOMItem();
                    // Không set BOMID vì không có trong kết quả query
                    item.setBomId(0); // Set default value
                    item.setProductId(rs.getInt("ProductID"));
                    item.setIngredientId(rs.getInt("IngredientID"));
                    item.setQuantity(rs.getBigDecimal("Quantity"));
                    item.setUnitId(rs.getObject("UnitID") != null ? rs.getInt("UnitID") : null);
                    item.setDisplayOrder(rs.getInt("DisplayOrder"));
                    item.setIngredientName(rs.getString("IngredientName"));
                    item.setUnitName(rs.getString("UnitName"));
                    
                    // Validate data
                    if (item.getIngredientId() == 0) {
                        System.err.println("WARNING: BOM Item has invalid IngredientID: 0");
                        continue; // Skip invalid items
                    }
                    if (item.getIngredientName() == null || item.getIngredientName().trim().isEmpty()) {
                        System.err.println("WARNING: BOM Item has null/empty IngredientName for IngredientID: " + item.getIngredientId());
                    }
                    
                    items.add(item);
                    System.out.println("DEBUG: Found BOM Item #" + count + " - ProductID: " + item.getProductId() + 
                                     ", IngredientID: " + item.getIngredientId() + 
                                     ", Quantity: " + item.getQuantity() +
                                     ", UnitID: " + item.getUnitId() +
                                     ", IngredientName: '" + item.getIngredientName() + "'" +
                                     ", DisplayOrder: " + item.getDisplayOrder());
                }
                System.out.println("DEBUG: Total BOM items found: " + count);
            }
        } catch (SQLException e) {
            System.err.println("ERROR: getBOMItemsByProductId failed for ProductID: " + productId);
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Delete all BOM items for a product
     */
    public boolean deleteBOMByProductId(int productId) {
        String sql = "DELETE FROM ProductBOM WHERE ProductID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Insert a BOM item
     */
    public boolean insertBOMItem(int productId, int ingredientId, java.math.BigDecimal quantity, Integer unitId, int displayOrder) {
        String sql = "INSERT INTO ProductBOM (ProductID, IngredientID, Quantity, UnitID, DisplayOrder) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, ingredientId);
            ps.setBigDecimal(3, quantity);
            if (unitId != null) {
                ps.setInt(4, unitId);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setInt(5, displayOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}


