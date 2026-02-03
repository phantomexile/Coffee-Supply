package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;

/**
 * DAO for Supplier operations
 */
public class SupplierDAO extends BaseDAO {

    /**
     * Get all suppliers
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier Order by SupplierID ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setContactName(rs.getString("ContactName"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setActive(rs.getBoolean("IsActive"));
                supplier.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? 
                    rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
                list.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get suppliers with pagination
     */
    public List<Supplier> getSuppliers(int page, int pageSize) {
        return getSuppliers(page, pageSize, null, null);
    }
    
    /**
     * Get suppliers with pagination and status filter
     */
    public List<Supplier> getSuppliers(int page, int pageSize, String statusFilter) {
        return getSuppliers(page, pageSize, statusFilter, null);
    }
    
    /**
     * Get suppliers with pagination and multiple filters
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @param statusFilter Status filter (optional)
     * @param searchFilter Search filter (optional)
     * @return List of suppliers
     */
    public List<Supplier> getSuppliers(int page, int pageSize, String statusFilter, String searchFilter) {
        List<Supplier> suppliers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Supplier WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            if ("active".equals(statusFilter)) {
                sql.append(" AND IsActive = TRUE");
            } else if ("inactive".equals(statusFilter)) {
                sql.append(" AND IsActive = FALSE");
            }
        }
        
        if (searchFilter != null && !searchFilter.trim().isEmpty()) {
            sql.append(" AND (LOWER(SupplierName) LIKE LOWER(?) OR LOWER(ContactName) LIKE LOWER(?) OR LOWER(Email) LIKE LOWER(?) OR Phone LIKE ?)");
            String searchPattern = "%" + searchFilter + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        sql.append(" ORDER BY SupplierID ASC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setContactName(rs.getString("ContactName"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setActive(rs.getBoolean("IsActive"));
                supplier.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? 
                    rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return suppliers;
    }

    /**
     * Get total count of suppliers
     */
    public int getTotalSuppliersCount() {
        return getTotalSuppliersCount(null, null);
    }
    
    /**
     * Get total count of suppliers with status filter
     */
    public int getTotalSuppliersCount(String statusFilter) {
        return getTotalSuppliersCount(statusFilter, null);
    }
    
    /**
     * Get total count of suppliers with multiple filters
     * @param statusFilter Status filter (optional)
     * @param searchFilter Search filter (optional)
     * @return Total count
     */
    public int getTotalSuppliersCount(String statusFilter, String searchFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Supplier WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            if ("active".equals(statusFilter)) {
                sql.append(" AND IsActive = TRUE");
            } else if ("inactive".equals(statusFilter)) {
                sql.append(" AND IsActive = FALSE");
            }
        }
        
        if (searchFilter != null && !searchFilter.trim().isEmpty()) {
            sql.append(" AND (LOWER(SupplierName) LIKE LOWER(?) OR LOWER(ContactName) LIKE LOWER(?) OR LOWER(Email) LIKE LOWER(?) OR Phone LIKE ?)");
            String searchPattern = "%" + searchFilter + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
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
     * Get supplier by ID
     */
    public Supplier getSupplierById(int supplierID) {
        String sql = "SELECT * FROM Supplier WHERE SupplierID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, supplierID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setContactName(rs.getString("ContactName"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setActive(rs.getBoolean("IsActive"));
                supplier.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? 
                    rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
                return supplier;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert new supplier
     */
    public int insertSupplier(Supplier supplier) {
        String sql = "INSERT INTO Supplier (SupplierName, ContactName, Email, Phone, Address, IsActive) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING SupplierID";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactName());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhone());
            ps.setString(5, supplier.getAddress());
            ps.setBoolean(6, supplier.isActive());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("SupplierID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Update supplier
     */
    public boolean updateSupplier(Supplier supplier) {
        String sql = "UPDATE Supplier SET SupplierName = ?, ContactName = ?, Email = ?, " +
                    "Phone = ?, Address = ?, IsActive = ? WHERE SupplierID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getContactName());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhone());
            ps.setString(5, supplier.getAddress());
            ps.setBoolean(6, supplier.isActive());
            ps.setInt(7, supplier.getSupplierID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete supplier (soft delete - set IsActive to false)
     */
    public boolean deleteSupplier(int supplierID) {
        String sql = "UPDATE Supplier SET IsActive = FALSE WHERE SupplierID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, supplierID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Toggle supplier active status
     */
    public boolean toggleSupplierStatus(int supplierID) {
        String sql = "UPDATE Supplier SET IsActive = NOT IsActive WHERE SupplierID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, supplierID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Search suppliers by name, contact name, email or phone
     */
    public List<Supplier> searchSuppliers(String keyword) {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier WHERE IsActive = TRUE AND " +
                    "(LOWER(SupplierName) LIKE LOWER(?) OR " +
                    "LOWER(ContactName) LIKE LOWER(?) OR " +
                    "LOWER(Email) LIKE LOWER(?) OR " +
                    "Phone LIKE ?) " +
                    "ORDER BY SupplierName";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setContactName(rs.getString("ContactName"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setActive(rs.getBoolean("IsActive"));
                supplier.setCreatedAt(rs.getTimestamp("CreatedAt") != null ? 
                    rs.getTimestamp("CreatedAt").toLocalDateTime() : null);
                list.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Check if a supplier email already exists (case-insensitive, exact match)
     * @param email Email to check
     * @param excludeSupplierID Supplier ID to exclude (for updates), nullable
     * @return true if exists, false otherwise
     */
    public boolean isSupplierEmailExists(String email, Integer excludeSupplierID) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Supplier WHERE LOWER(Email) = LOWER(?)");
        if (excludeSupplierID != null) {
            sql.append(" AND SupplierID != ?");
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, email);
            if (excludeSupplierID != null) {
                ps.setInt(2, excludeSupplierID);
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
     * Get all active suppliers
     * @return List of suppliers
     */
    public List<Supplier> getActiveSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT SupplierID, SupplierName, ContactName, Phone, Email, Address, IsActive, CreatedAt " +
                    "FROM Supplier " +
                    "WHERE IsActive = TRUE " +
                    "ORDER BY SupplierName";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("SupplierID"));
                supplier.setSupplierName(rs.getString("SupplierName"));
                supplier.setContactName(rs.getString("ContactName"));
                supplier.setPhone(rs.getString("Phone"));
                supplier.setEmail(rs.getString("Email"));
                supplier.setAddress(rs.getString("Address"));
                supplier.setActive(rs.getBoolean("IsActive"));
                supplier.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());

                suppliers.add(supplier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliers;
    }
}
