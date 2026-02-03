/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Shop;

public class ShopDAO extends BaseDAO {

    private Shop extractShopFromResultSet(ResultSet rs) throws SQLException {
        Shop shop = new Shop();
        shop.setShopID(rs.getInt("ShopID"));
        shop.setShopName(rs.getString("ShopName"));
        shop.setAddress(rs.getString("Address"));
        shop.setPhone(rs.getString("Phone"));
        
        int ownerId = rs.getInt("OwnerID");
        shop.setOwnerID(rs.wasNull() ? null : ownerId);
        
        shop.setActive(rs.getBoolean("IsActive"));
        shop.setShopImage(rs.getString("ShopImage"));
        shop.setCreatedAt(rs.getTimestamp("CreatedAt"));
        
        return shop;
    }

    public List<Shop> getAllShops() {
        List<Shop> shops = new ArrayList<>();
        String sql = "SELECT * FROM Shop ORDER BY CreatedAt DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                shops.add(extractShopFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }

    public Shop getShopById(int shopId) {
        String sql = "SELECT * FROM Shop WHERE ShopID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shopId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return extractShopFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int insertShop(Shop shop) {
        String sql = "INSERT INTO Shop (ShopName, Address, Phone, OwnerID, IsActive, ShopImage) VALUES (?, ?, ?, ?, ?, ?) RETURNING ShopID";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getAddress());
            ps.setString(3, shop.getPhone());
            
            if (shop.getOwnerID() != null) {
                ps.setInt(4, shop.getOwnerID());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            
            ps.setBoolean(5, shop.isActive());
            ps.setString(6, shop.getShopImage());
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int newId = rs.getInt(1);
                return newId;
            } else {
                System.out.println("No ID returned from insert operation");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in insertShop: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateShop(Shop shop) {
        String sql = "UPDATE Shop SET ShopName = ?, Address = ?, Phone = ?, OwnerID = ?, IsActive = ?, ShopImage = ? WHERE ShopID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, shop.getShopName());
            ps.setString(2, shop.getAddress());
            ps.setString(3, shop.getPhone());
            
            if (shop.getOwnerID() != null) {
                ps.setInt(4, shop.getOwnerID());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            
            ps.setBoolean(5, shop.isActive());
            ps.setString(6, shop.getShopImage());
            ps.setInt(7, shop.getShopID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteShop(int shopId) {
        String sql = "UPDATE Shop SET IsActive = FALSE WHERE ShopID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shopId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get active shops only
     */
    public List<Shop> getActiveShops() {
        List<Shop> shops = new ArrayList<>();
        String sql = "SELECT * FROM Shop WHERE IsActive = TRUE ORDER BY CreatedAt DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                shops.add(extractShopFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }
    
    /**
     * Search shops by name
     */
    public List<Shop> searchShopsByName(String keyword) {
        List<Shop> shops = new ArrayList<>();
        String sql = "SELECT * FROM Shop WHERE LOWER(ShopName) LIKE LOWER(?) ORDER BY CreatedAt DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                shops.add(extractShopFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shops;
    }
    
    /**
     * Get all shops with owner information
     */
    public List<Object[]> getAllShopsWithOwner() {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT s.*, u.FullName as OwnerName FROM Shop s " +
                    "LEFT JOIN \"User\" u ON s.OwnerID = u.UserID " +
                    "ORDER BY s.CreatedAt DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Shop shop = extractShopFromResultSet(rs);
                String ownerName = rs.getString("OwnerName");
                result.add(new Object[]{shop, ownerName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Get shops by owner ID with owner information (paginated)
     */
    public List<Object[]> getShopsByOwnerWithOwnerPaginated(int ownerID, int page, int pageSize) {
        return getShopsByOwnerWithOwnerPaginated(ownerID, page, pageSize, null, null, null);
    }
    
    /**
     * Get shops by owner ID with owner information (paginated with filters)
     */
    public List<Object[]> getShopsByOwnerWithOwnerPaginated(int ownerID, int page, int pageSize, 
                                                           String searchName, String searchAddress, String statusFilter) {
        List<Object[]> result = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.*, u.FullName as OwnerName FROM Shop s ");
        sql.append("LEFT JOIN \"User\" u ON s.OwnerID = u.UserID ");
        sql.append("WHERE s.OwnerID = ? ");
        
        List<Object> params = new ArrayList<>();
        params.add(ownerID);
        
        // Add search filters
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append("AND LOWER(s.ShopName) LIKE LOWER(?) ");
            params.add("%" + searchName.trim() + "%");
        }
        
        if (searchAddress != null && !searchAddress.trim().isEmpty()) {
            sql.append("AND LOWER(s.Address) LIKE LOWER(?) ");
            params.add("%" + searchAddress.trim() + "%");
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            if ("active".equals(statusFilter)) {
                sql.append("AND s.IsActive = true ");
            } else if ("inactive".equals(statusFilter)) {
                sql.append("AND s.IsActive = false ");
            }
        }
        
        sql.append("ORDER BY s.CreatedAt DESC ");
        sql.append("LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Shop shop = extractShopFromResultSet(rs);
                String ownerName = rs.getString("OwnerName");
                result.add(new Object[]{shop, ownerName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * Get total count of shops by owner ID
     */
    public int getTotalShopsCountByOwner(int ownerID) {
        return getTotalShopsCountByOwner(ownerID, null, null, null);
    }
    
    /**
     * Get total count of shops by owner ID with filters
     */
    public int getTotalShopsCountByOwner(int ownerID, String searchName, String searchAddress, String statusFilter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Shop s WHERE s.OwnerID = ? ");
        
        List<Object> params = new ArrayList<>();
        params.add(ownerID);
        
        // Add search filters
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append("AND LOWER(s.ShopName) LIKE LOWER(?) ");
            params.add("%" + searchName.trim() + "%");
        }
        
        if (searchAddress != null && !searchAddress.trim().isEmpty()) {
            sql.append("AND LOWER(s.Address) LIKE LOWER(?) ");
            params.add("%" + searchAddress.trim() + "%");
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            if ("active".equals(statusFilter)) {
                sql.append("AND s.IsActive = true ");
            } else if ("inactive".equals(statusFilter)) {
                sql.append("AND s.IsActive = false ");
            }
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
     * Get shops by owner ID with owner information
     */
    public List<Object[]> getShopsByOwnerWithOwner(int ownerID) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT s.*, u.FullName as OwnerName FROM Shop s " +
                    "LEFT JOIN \"User\" u ON s.OwnerID = u.UserID " +
                    "WHERE s.OwnerID = ? " +
                    "ORDER BY s.CreatedAt DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, ownerID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Shop shop = extractShopFromResultSet(rs);
                String ownerName = rs.getString("OwnerName");
                result.add(new Object[]{shop, ownerName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
