package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductDAO extends BaseDAO {

    /**
     * Get all products with pagination
     */
    public List<Product> getAllProducts(int page, int pageSize, String searchTerm, Integer categoryId, Boolean isActive) {
        return getAllProducts(page, pageSize, searchTerm, categoryId, isActive, null, null);
    }
    
    /**
     * Get all products with pagination and sorting
     */
    public List<Product> getAllProducts(int page, int pageSize, String searchTerm, Integer categoryId, Boolean isActive, String sortBy, String sortOrder) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, s1.Value as CategoryName " +
                "FROM Product p " +
                "LEFT JOIN Setting s1 ON p.CategoryID = s1.SettingID " +
                "WHERE 1=1 ";

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += "AND (LOWER(p.ProductName) LIKE LOWER(?) OR LOWER(p.Description) LIKE LOWER(?)) ";
        }
        if (categoryId != null) {
            sql += "AND p.CategoryID = ? ";
        }
        if (isActive != null) {
            sql += "AND p.IsActive = ? ";
        }

        // Add sorting
        String orderByClause = "ORDER BY ";
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String columnName;
            switch (sortBy.toLowerCase()) {
                case "id":
                    columnName = "p.ProductID";
                    break;
                case "name":
                    columnName = "p.ProductName";
                    break;
                case "category":
                    columnName = "s1.Value";
                    break;
                case "price":
                    columnName = "p.Price";
                    break;
                case "status":
                    columnName = "p.IsActive";
                    break;
                default:
                    columnName = "p.ProductID";
            }
            
            String order = "ASC";
            if (sortOrder != null && sortOrder.trim().equalsIgnoreCase("desc")) {
                order = "DESC";
            }
            
            orderByClause += columnName + " " + order + " ";
        } else {
            orderByClause += "p.ProductID ASC ";
        }
        
        sql += orderByClause + "LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            if (categoryId != null) {
                ps.setInt(paramIndex++, categoryId);
            }
            if (isActive != null) {
                ps.setBoolean(paramIndex++, isActive);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setDescription(rs.getString("Description"));
                    product.setImageUrl(rs.getString("ImageUrl"));
                    // Co the tra ve 0 neu NULL - tuy model cua ban
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setPrice(rs.getBigDecimal("Price"));
                    product.setActive(rs.getBoolean("IsActive"));
                    product.setCreatedAt(rs.getTimestamp("CreatedAt"));

                    // Set additional display fields
                    product.setCategoryName(rs.getString("CategoryName"));

                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Get total count of products for pagination
     */
    public int getTotalProductsCount(String searchTerm, Integer categoryId, Boolean isActive) {
        String sql = "SELECT COUNT(*) FROM Product p WHERE 1=1 ";

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql += "AND (LOWER(p.ProductName) LIKE LOWER(?) OR LOWER(p.Description) LIKE LOWER(?)) ";
        }
        if (categoryId != null) {
            sql += "AND p.CategoryID = ? ";
        }
        if (isActive != null) {
            sql += "AND p.IsActive = ? ";
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int paramIndex = 1;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            if (categoryId != null) {
                ps.setInt(paramIndex++, categoryId);
            }
            if (isActive != null) {
                ps.setBoolean(paramIndex++, isActive);
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
     * Get product by ID
     */
    public Product getProductById(int productId) {
        String sql = "SELECT p.*, s1.Value as CategoryName " +
                "FROM Product p " +
                "LEFT JOIN Setting s1 ON p.CategoryID = s1.SettingID " +
                "WHERE p.ProductID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("ProductID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setDescription(rs.getString("Description"));
                    product.setImageUrl(rs.getString("ImageUrl"));
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setPrice(rs.getBigDecimal("Price"));
                    product.setActive(rs.getBoolean("IsActive"));
                    product.setCreatedAt(rs.getTimestamp("CreatedAt"));

                    // Set additional display fields
                    product.setCategoryName(rs.getString("CategoryName"));

                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create new product
     */
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO Product (ProductName, Description, ImageUrl, CategoryID, Price, IsActive) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImageUrl());
            ps.setInt(4, product.getCategoryID());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBoolean(6, product.isActive());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update product
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET ProductName = ?, Description = ?, ImageUrl = ?, CategoryID = ?, " +
                "Price = ?, IsActive = ? WHERE ProductID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getImageUrl());
            ps.setInt(4, product.getCategoryID());
            ps.setBigDecimal(5, product.getPrice());
            ps.setBoolean(6, product.isActive());
            ps.setInt(7, product.getProductID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete product (soft delete)
     */
    public boolean deleteProduct(int productId) {
        String sql = "UPDATE Product SET IsActive = FALSE WHERE ProductID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get categories for dropdown
     */
    public List<Object[]> getCategories() {
        List<Object[]> categories = new ArrayList<>();
        String sql = "SELECT SettingID, Value FROM Setting WHERE Type = 'Category' AND IsActive = TRUE ORDER BY Value";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(new Object[]{rs.getInt("SettingID"), rs.getString("Value")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Check if product with same name already exists
     * @param productName Product name to check
     * @param excludeProductId Product ID to exclude (for update operations), null for create
     * @return true if exists, false otherwise
     */
    public boolean isProductNameExists(String productName, Integer excludeProductId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Product WHERE LOWER(ProductName) = LOWER(?)");
        if (excludeProductId != null) {
            sql.append(" AND ProductID != ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, productName.trim());
            if (excludeProductId != null) {
                ps.setInt(2, excludeProductId);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
