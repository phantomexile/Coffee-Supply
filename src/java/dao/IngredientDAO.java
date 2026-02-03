/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Ingredient;
import model.Setting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * DAO class for Ingredient management
 * @author Hòa
 */
public class IngredientDAO extends BaseDAO {
    
    /**
     * Get all ingredients with pagination and filtering
     * @param page Page number (starting from 1)
     * @param pageSize Number of records per page
     * @param supplierFilter Supplier filter (null for all suppliers)
     * @param searchKeyword Search keyword for ingredient name
     * @param activeOnly Show only active ingredients
     * @return List of ingredients
     */
    public List<Ingredient> getAllIngredients(int page, int pageSize, Integer supplierFilter, 
                                            String searchKeyword, Boolean activeOnly) {
        List<Ingredient> ingredients = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.IngredientID, i.Name, i.Description, i.UnitID, i.StockQuantity, ");
        sql.append("i.SupplierID, i.IsActive, i.CreatedAt, ");
        sql.append("u.Value as UnitName, s.SupplierName as SupplierName ");
        sql.append("FROM Ingredient i ");
        sql.append("LEFT JOIN Setting u ON i.UnitID = u.SettingID AND u.Type = 'Unit' ");
        sql.append("LEFT JOIN Supplier s ON i.SupplierID = s.SupplierID ");
        sql.append("WHERE 1=1 ");
        
        // Add filters
        List<Object> params = new ArrayList<>();
        if (supplierFilter != null) {
            sql.append("AND i.SupplierID = ? ");
            params.add(supplierFilter);
        }
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND i.Name ILIKE ? ");
            params.add("%" + searchKeyword.trim() + "%");
        }
        if (activeOnly != null) {
            if (activeOnly) {
                sql.append("AND i.IsActive = TRUE ");
            } else {
                sql.append("AND i.IsActive = FALSE ");
            }
        }
        
        sql.append("ORDER BY i.CreatedAt DESC ");
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientID(rs.getInt("IngredientID"));
                ingredient.setName(rs.getString("Name"));
                ingredient.setDescription(rs.getString("Description"));
                ingredient.setUnitID(rs.getInt("UnitID"));
                ingredient.setStockQuantity(rs.getBigDecimal("StockQuantity"));
                ingredient.setSupplierID(rs.getInt("SupplierID"));
                ingredient.setActive(rs.getBoolean("IsActive"));
                ingredient.setCreatedAt(rs.getTimestamp("CreatedAt"));
                ingredient.setUnitName(rs.getString("UnitName"));
                ingredient.setSupplierName(rs.getString("SupplierName"));
                
                ingredients.add(ingredient);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ingredients;
    }
    
    /**
     * Get total count of ingredients with filters
     * @param supplierFilter Supplier filter
     * @param searchKeyword Search keyword
     * @param activeOnly Show only active ingredients
     * @return Total count
     */
    public int getTotalIngredientsCount(Integer supplierFilter, String searchKeyword, Boolean activeOnly) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Ingredient i ");
        sql.append("WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        if (supplierFilter != null) {
            sql.append("AND i.SupplierID = ? ");
            params.add(supplierFilter);
        }
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND i.Name ILIKE ? ");
            params.add("%" + searchKeyword.trim() + "%");
        }
        if (activeOnly != null && activeOnly) {
            sql.append("AND i.IsActive = TRUE ");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get ingredient by ID
     * @param ingredientID Ingredient ID
     * @return Ingredient object or null if not found
     */
    public Ingredient getIngredientById(int ingredientID) {
        String sql = "SELECT i.IngredientID, i.Name, i.Description, i.UnitID, i.StockQuantity, " +
                    "i.SupplierID, i.IsActive, i.CreatedAt, " +
                    "u.Value as UnitName, s.SupplierName as SupplierName " +
                    "FROM Ingredient i " +
                    "LEFT JOIN Setting u ON i.UnitID = u.SettingID AND u.Type = 'Unit' " +
                    "LEFT JOIN Supplier s ON i.SupplierID = s.SupplierID " +
                    "WHERE i.IngredientID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ingredientID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientID(rs.getInt("IngredientID"));
                ingredient.setName(rs.getString("Name"));
                ingredient.setDescription(rs.getString("Description"));
                ingredient.setUnitID(rs.getInt("UnitID"));
                ingredient.setStockQuantity(rs.getBigDecimal("StockQuantity"));
                ingredient.setSupplierID(rs.getInt("SupplierID"));
                ingredient.setActive(rs.getBoolean("IsActive"));
                ingredient.setCreatedAt(rs.getTimestamp("CreatedAt"));
                ingredient.setUnitName(rs.getString("UnitName"));
                ingredient.setSupplierName(rs.getString("SupplierName"));
                
                return ingredient;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Create new ingredient
     * @param ingredient Ingredient to create
     * @return Generated ingredient ID, or 0 if failed
     */
    public int createIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO Ingredient (Name, Description, UnitID, StockQuantity, SupplierID, IsActive) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING IngredientID";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ingredient.getName());
            ps.setString(2, ingredient.getDescription());
            ps.setInt(3, ingredient.getUnitID());
            ps.setBigDecimal(4, ingredient.getStockQuantity());
            ps.setInt(5, ingredient.getSupplierID());
            ps.setBoolean(6, ingredient.isActive());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("IngredientID");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Update existing ingredient
     * @param ingredient Ingredient to update
     * @return true if successful, false otherwise
     */
    public boolean updateIngredient(Ingredient ingredient) {
        String sql = "UPDATE Ingredient SET Name = ?, Description = ?, UnitID = ?, StockQuantity = ?, " +
                    "SupplierID = ?, IsActive = ? WHERE IngredientID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ingredient.getName());
            ps.setString(2, ingredient.getDescription());
            ps.setInt(3, ingredient.getUnitID());
            ps.setBigDecimal(4, ingredient.getStockQuantity());
            ps.setInt(5, ingredient.getSupplierID());
            ps.setBoolean(6, ingredient.isActive());
            ps.setInt(7, ingredient.getIngredientID());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete ingredient (soft delete by setting IsActive to false)
     * @param ingredientID Ingredient ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteIngredient(int ingredientID) {
        String sql = "UPDATE Ingredient SET IsActive = false WHERE IngredientID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ingredientID);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get all available units for ingredients
     * @return List of unit settings
     */
    public List<Setting> getAvailableUnits() {
        List<Setting> units = new ArrayList<>();
        String sql = "SELECT SettingID, Type, Value, Description, IsActive " +
                    "FROM Setting " +
                    "WHERE Type = 'Unit' AND IsActive = TRUE " +
                    "ORDER BY Value";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Setting unit = new Setting();
                unit.setSettingID(rs.getInt("SettingID"));
                unit.setType(rs.getString("Type"));
                unit.setValue(rs.getString("Value"));
                unit.setDescription(rs.getString("Description"));
                unit.setActive(rs.getBoolean("IsActive"));
                
                units.add(unit);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return units;
    }
    
    /**
     * Check if ingredient name already exists
     * @param name Ingredient name to check
     * @param excludeIngredientID Ingredient ID to exclude from check (for updates)
     * @return true if name exists, false otherwise
     */
    public boolean isIngredientNameExists(String name, Integer excludeIngredientID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Ingredient WHERE LOWER(Name) = LOWER(?) ");
        
        if (excludeIngredientID != null) {
            sql.append("AND IngredientID != ? ");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            ps.setString(1, name);
            if (excludeIngredientID != null) {
                ps.setInt(2, excludeIngredientID);
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
    
    /**
     * Check if ingredient with same name and supplier already exists
     * @param name Ingredient name to check (should be trimmed)
     * @param supplierID Supplier ID to check
     * @param excludeIngredientID Ingredient ID to exclude from check (for updates)
     * @return true if combination exists, false otherwise
     */
    public boolean isIngredientNameAndSupplierExists(String name, int supplierID, Integer excludeIngredientID) {
        StringBuilder sql = new StringBuilder();
        // Use TRIM() function in PostgreSQL to remove leading/trailing spaces
        sql.append("SELECT COUNT(*) FROM Ingredient WHERE LOWER(TRIM(BOTH FROM Name)) = LOWER(TRIM(BOTH FROM ?)) AND SupplierID = ? ");
        
        if (excludeIngredientID != null) {
            sql.append("AND IngredientID != ? ");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            ps.setString(1, name);
            ps.setInt(2, supplierID);
            if (excludeIngredientID != null) {
                ps.setInt(3, excludeIngredientID);
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("DEBUG: Checking ingredient - Name: '" + name + "', SupplierID: " + supplierID + ", ExcludeID: " + excludeIngredientID + ", Count: " + count);
                return count > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR in isIngredientNameAndSupplierExists: " + e.getMessage());
            System.err.println("SQL: " + sql.toString());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get ingredients with low stock (below minimum threshold)
     * @param threshold Minimum stock threshold
     * @return List of low stock ingredients
     */
  public List<Ingredient> getLowStockIngredients(BigDecimal threshold) {
    List<Ingredient> ingredients = new ArrayList<>();

    String sql = """
        SELECT 
            i.IngredientID, 
            i.Name, 
            i.Description,
            i.UnitID, 
            i.StockQuantity, 
            i.SupplierID, 
            i.IsActive, 
            i.CreatedAt, 
            u.Value AS UnitName, 
            s.SupplierName AS SupplierName
        FROM Ingredient i
        LEFT JOIN Setting u 
            ON i.UnitID = u.SettingID 
            AND u.Type = 'Unit'
        LEFT JOIN Supplier s 
            ON i.SupplierID = s.SupplierID
        WHERE i.IsActive = TRUE 
          AND i.StockQuantity < ?
        ORDER BY i.StockQuantity ASC
        """;

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setBigDecimal(1, threshold);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientID(rs.getInt("IngredientID"));
                ingredient.setName(rs.getString("Name"));
                ingredient.setDescription(rs.getString("Description"));
                ingredient.setUnitID(rs.getInt("UnitID"));
                ingredient.setStockQuantity(rs.getBigDecimal("StockQuantity"));
                ingredient.setSupplierID(rs.getInt("SupplierID"));
                ingredient.setActive(rs.getBoolean("IsActive"));
                ingredient.setCreatedAt(rs.getTimestamp("CreatedAt"));
                ingredient.setUnitName(rs.getString("UnitName"));
                ingredient.setSupplierName(rs.getString("SupplierName"));

                ingredients.add(ingredient);
            }
        }

    } catch (SQLException e) {
        System.err.println("Error fetching low stock ingredients: " + e.getMessage());
        e.printStackTrace();
    }

    return ingredients;
}

    
    /**
     * Update ingredient stock quantity
     * @param ingredientID Ingredient ID
     * @param newQuantity New stock quantity
     * @return true if successful, false otherwise
     */
    public boolean updateStockQuantity(int ingredientID, BigDecimal newQuantity) {
        String sql = "UPDATE Ingredient SET StockQuantity = ? WHERE IngredientID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, newQuantity);
            ps.setInt(2, ingredientID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Increase ingredient stock quantity by adding receivedQuantity
     * Used when receiving purchase orders
     * @param ingredientID Ingredient ID
     * @param quantityToAdd Quantity to add to current stock
     * @return true if successful, false otherwise
     */
    public boolean increaseStockQuantity(int ingredientID, BigDecimal quantityToAdd) {
        String sql = "UPDATE Ingredient SET StockQuantity = StockQuantity + ? WHERE IngredientID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, quantityToAdd);
            ps.setInt(2, ingredientID);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Toggle ingredient active status
     * @param ingredientID Ingredient ID
     * @return true if successful, false otherwise
     */
    public boolean toggleIngredient(int ingredientID) {
        // First get current status
        Ingredient ingredient = getIngredientById(ingredientID);
        if (ingredient == null) {
            return false;
        }
        
        // Toggle the status
        boolean newStatus = !ingredient.isActive();
        
        String sql = "UPDATE Ingredient SET IsActive = ? WHERE IngredientID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBoolean(1, newStatus);
            ps.setInt(2, ingredientID);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get ingredients by supplier ID
     */
    public List<Ingredient> getIngredientsBySupplier(int supplierId) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT i.IngredientID, i.Name, i.Description, i.UnitID, i.StockQuantity, " +
                    "i.SupplierID, i.IsActive, i.CreatedAt, " +
                    "u.Value as UnitName, s.SupplierName as SupplierName " +
                    "FROM Ingredient i " +
                    "LEFT JOIN Setting u ON i.UnitID = u.SettingID AND u.Type = 'Unit' " +
                    "LEFT JOIN Supplier s ON i.SupplierID = s.SupplierID " +
                    "WHERE i.SupplierID = ? " +
                    "ORDER BY i.Name";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, supplierId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setIngredientID(rs.getInt("IngredientID"));
                ingredient.setName(rs.getString("Name"));
                ingredient.setDescription(rs.getString("Description"));
                ingredient.setUnitID(rs.getInt("UnitID"));
                ingredient.setStockQuantity(rs.getBigDecimal("StockQuantity"));
                ingredient.setSupplierID(rs.getInt("SupplierID"));
                ingredient.setActive(rs.getBoolean("IsActive"));
                ingredient.setCreatedAt(rs.getTimestamp("CreatedAt"));
                ingredient.setUnitName(rs.getString("UnitName"));
                ingredient.setSupplierName(rs.getString("SupplierName"));
                
                ingredients.add(ingredient);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ingredients;
    }
}