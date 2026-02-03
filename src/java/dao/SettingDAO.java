package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Setting;

/**
 * Data Access Object for Setting
 */
public class SettingDAO extends BaseDAO {
    
    /**
     * Get all settings
     * @return List of all settings
     */
    public List<Setting> getAllSettings() {
        List<Setting> settings = new ArrayList<>();
        String sql = "SELECT * FROM Setting ORDER BY Type, Priority ASC, SettingID ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Setting setting = new Setting();
                setting.setSettingID(rs.getInt("SettingID"));
                setting.setName(rs.getString("Name"));
                setting.setType(rs.getString("Type"));
                setting.setValue(rs.getString("Value"));
                setting.setPriority(rs.getInt("Priority"));
                setting.setDescription(rs.getString("Description"));
                setting.setActive(rs.getBoolean("IsActive"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return settings;
    }
    
    /**
     * Get settings by type
     * @param type Setting type
     * @return List of settings with specified type
     */
    public List<Setting> getSettingsByType(String type) {
        List<Setting> settings = new ArrayList<>();
        String sql = "SELECT * FROM Setting WHERE Type = ? ORDER BY Priority ASC, Value ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Setting setting = new Setting();
                setting.setSettingID(rs.getInt("SettingID"));
                setting.setName(rs.getString("Name"));
                setting.setType(rs.getString("Type"));
                setting.setValue(rs.getString("Value"));
                setting.setPriority(rs.getInt("Priority"));
                setting.setDescription(rs.getString("Description"));
                setting.setActive(rs.getBoolean("IsActive"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return settings;
    }
    
    /**
     * Get setting by ID
     * @param settingID Setting ID
     * @return Setting object or null if not found
     */
    public Setting getSettingById(int settingID) {
        String sql = "SELECT * FROM Setting WHERE SettingID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, settingID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Setting setting = new Setting();
                setting.setSettingID(rs.getInt("SettingID"));
                setting.setName(rs.getString("Name"));
                setting.setType(rs.getString("Type"));
                setting.setValue(rs.getString("Value"));
                setting.setPriority(rs.getInt("Priority"));
                setting.setDescription(rs.getString("Description"));
                setting.setActive(rs.getBoolean("IsActive"));
                return setting;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Create new setting
     * @param setting Setting object
     * @return true if successful, false otherwise
     */
    public boolean createSetting(Setting setting) {
        // Validate required fields
        if (setting.getName() == null || setting.getName().trim().isEmpty()) {
            System.out.println("Error: Setting name cannot be null or empty");
            return false;
        }
        
        if (setting.getType() == null || setting.getType().trim().isEmpty()) {
            System.out.println("Error: Setting type cannot be null or empty");
            return false;
        }
        
        if (setting.getValue() == null || setting.getValue().trim().isEmpty()) {
            System.out.println("Error: Setting value cannot be null or empty");
            return false;
        }
        
        // Check if name already exists
        if (isSettingNameExists(setting.getName(), null)) {
            System.out.println("Error: Setting name '" + setting.getName() + "' already exists");
            return false;
        }
        
        // Check if value already exists for this type
        if (isSettingValueExists(setting.getType(), setting.getValue(), null)) {
            System.out.println("Error: Setting value '" + setting.getValue() + "' already exists for type '" + setting.getType() + "'");
            return false;
        }
        
        // Check if priority already exists for this type
        if (isSettingPriorityExists(setting.getType(), setting.getPriority(), null)) {
            System.out.println("Error: Setting priority '" + setting.getPriority() + "' already exists for type '" + setting.getType() + "'");
            return false;
        }
        
        String sql = "INSERT INTO Setting (Name, Type, Value, Priority, Description, IsActive) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, setting.getName() != null ? setting.getName().trim() : null);
            ps.setString(2, setting.getType().trim());
            ps.setString(3, setting.getValue().trim());
            ps.setInt(4, setting.getPriority());
            ps.setString(5, setting.getDescription() != null ? setting.getDescription().trim() : null);
            ps.setBoolean(6, setting.isActive());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update existing setting
     * @param setting Setting object
     * @return true if successful, false otherwise
     */
    public boolean updateSetting(Setting setting) {
        // Validate required fields
        if (setting.getName() == null || setting.getName().trim().isEmpty()) {
            System.out.println("Error: Setting name cannot be null or empty");
            return false;
        }
        
        if (setting.getType() == null || setting.getType().trim().isEmpty()) {
            System.out.println("Error: Setting type cannot be null or empty");
            return false;
        }
        
        if (setting.getValue() == null || setting.getValue().trim().isEmpty()) {
            System.out.println("Error: Setting value cannot be null or empty");
            return false;
        }
        
        // Check if name already exists (excluding current setting)
        if (isSettingNameExists(setting.getName(), setting.getSettingID())) {
            System.out.println("Error: Setting name '" + setting.getName() + "' already exists");
            return false;
        }
        
        // Check if value already exists for this type (excluding current setting)
        if (isSettingValueExists(setting.getType(), setting.getValue(), setting.getSettingID())) {
            System.out.println("Error: Setting value '" + setting.getValue() + "' already exists for type '" + setting.getType() + "'");
            return false;
        }
        
        // Check if priority already exists for this type (excluding current setting)
        if (isSettingPriorityExists(setting.getType(), setting.getPriority(), setting.getSettingID())) {
            System.out.println("Error: Setting priority '" + setting.getPriority() + "' already exists for type '" + setting.getType() + "'");
            return false;
        }
        
        String sql = "UPDATE Setting SET Name = ?, Type = ?, Value = ?, Priority = ?, Description = ?, IsActive = ? WHERE SettingID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, setting.getName() != null ? setting.getName().trim() : null);
            ps.setString(2, setting.getType().trim());
            ps.setString(3, setting.getValue().trim());
            ps.setInt(4, setting.getPriority());
            ps.setString(5, setting.getDescription() != null ? setting.getDescription().trim() : null);
            ps.setBoolean(6, setting.isActive());
            ps.setInt(7, setting.getSettingID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Delete setting by ID
     * @param settingID Setting ID
     * @return true if successful, false otherwise
     */
    public boolean deleteSetting(int settingID) {
        // Check if setting is being used by users (for Role type)
        Setting setting = getSettingById(settingID);
        if (setting != null && "Role".equals(setting.getType())) {
            if (isRoleInUse(settingID)) {
                System.out.println("Error: Cannot delete role '" + setting.getValue() + "' because it is currently assigned to users");
                return false;
            }
        }
        
        String sql = "DELETE FROM Setting WHERE SettingID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, settingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if setting value exists for a specific type (excluding a specific ID)
     * @param type Setting type
     * @param value Setting value
     * @param excludeSettingID Setting ID to exclude from check (for updates)
     * @return true if exists, false otherwise
     */
    public boolean isSettingValueExists(String type, String value, Integer excludeSettingID) {
        String sql = "SELECT COUNT(*) FROM Setting WHERE Type = ? AND Value = ?";
        if (excludeSettingID != null) {
            sql += " AND SettingID != ?";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, type);
            ps.setString(2, value);
            if (excludeSettingID != null) {
                ps.setInt(3, excludeSettingID);
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
     * Check if setting name exists (excluding a specific ID)
     * @param name Setting name
     * @param excludeSettingID Setting ID to exclude from check (for updates)
     * @return true if exists, false otherwise
     */
    public boolean isSettingNameExists(String name, Integer excludeSettingID) {
        String sql = "SELECT COUNT(*) FROM Setting WHERE LOWER(Name) = LOWER(?)";
        if (excludeSettingID != null) {
            sql += " AND SettingID != ?";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            if (excludeSettingID != null) {
                ps.setInt(2, excludeSettingID);
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
     * Check if setting priority exists for a specific type (excluding a specific ID)
     * @param type Setting type
     * @param priority Setting priority
     * @param excludeSettingID Setting ID to exclude from check (for updates)
     * @return true if exists, false otherwise
     */
    public boolean isSettingPriorityExists(String type, int priority, Integer excludeSettingID) {
        String sql = "SELECT COUNT(*) FROM Setting WHERE Type = ? AND Priority = ?";
        if (excludeSettingID != null) {
            sql += " AND SettingID != ?";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, type);
            ps.setInt(2, priority);
            if (excludeSettingID != null) {
                ps.setInt(3, excludeSettingID);
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
     * Get distinct setting types
     * @return List of distinct types
     */
    public List<String> getDistinctTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT Type FROM Setting ORDER BY Type";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                types.add(rs.getString("Type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return types;
    }
    
    /**
     * Get settings with pagination
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @param typeFilter Type filter (optional)
     * @return List of settings
     */
    public List<Setting> getSettings(int page, int pageSize, String typeFilter) {
        return getSettings(page, pageSize, typeFilter, null, null);
    }
    
    /**
     * Get settings with pagination and multiple filters
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @param typeFilter Type filter (optional)
     * @param searchFilter Search filter (optional)
     * @param statusFilter Status filter (optional)
     * @return List of settings
     */
    public List<Setting> getSettings(int page, int pageSize, String typeFilter, String searchFilter, String statusFilter) {
        List<Setting> settings = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Setting WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (typeFilter != null && !typeFilter.trim().isEmpty()) {
            sql.append(" AND Type = ?");
            params.add(typeFilter);
        }
        
        if (searchFilter != null && !searchFilter.trim().isEmpty()) {
            sql.append(" AND (LOWER(Name) LIKE LOWER(?) OR LOWER(Value) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?))");
            String searchPattern = "%" + searchFilter + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            boolean isActive = "1".equals(statusFilter);
            sql.append(" AND IsActive = ?");
            params.add(isActive);
        }
        
        sql.append(" ORDER BY Type, Priority ASC, SettingID ASC LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Setting setting = new Setting();
                setting.setSettingID(rs.getInt("SettingID"));
                setting.setName(rs.getString("Name"));
                setting.setType(rs.getString("Type"));
                setting.setValue(rs.getString("Value"));
                setting.setPriority(rs.getInt("Priority"));
                setting.setDescription(rs.getString("Description"));
                setting.setActive(rs.getBoolean("IsActive"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return settings;
    }
    
    /**
     * Get total count of settings
     * @param typeFilter Type filter (optional)
     * @return Total count
     */
    public int getTotalSettingsCount(String typeFilter) {
        return getTotalSettingsCount(typeFilter, null, null);
    }
    
    /**
     * Get total count of settings with multiple filters
     * @param typeFilter Type filter (optional)
     * @param searchFilter Search filter (optional)
     * @param statusFilter Status filter (optional)
     * @return Total count
     */
    public int getTotalSettingsCount(String typeFilter, String searchFilter, String statusFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Setting WHERE 1=1");
        List<Object> params = new ArrayList<>();
        
        if (typeFilter != null && !typeFilter.trim().isEmpty()) {
            sql.append(" AND Type = ?");
            params.add(typeFilter);
        }
        
        if (searchFilter != null && !searchFilter.trim().isEmpty()) {
            sql.append(" AND (LOWER(Name) LIKE LOWER(?) OR LOWER(Value) LIKE LOWER(?) OR LOWER(Description) LIKE LOWER(?))");
            String searchPattern = "%" + searchFilter + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            boolean isActive = "1".equals(statusFilter);
            sql.append(" AND IsActive = ?");
            params.add(isActive);
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
     * Get role SettingID by its value (e.g., "Barista", "Inventory"). Returns null if not found or inactive.
     */
    public Integer getRoleIdByValue(String roleValue) {
        String sql = "SELECT SettingID FROM Setting WHERE Type = 'Role' AND Value = ? AND IsActive = true LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleValue);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("SettingID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Check if a role is currently being used by any users
     * @param roleSettingID Role setting ID
     * @return true if role is in use, false otherwise
     */
    public boolean isRoleInUse(int roleSettingID) {
        String sql = "SELECT COUNT(*) FROM \"User\" WHERE RoleID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roleSettingID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Validate setting type (check if it's a valid type)
     * @param type Setting type to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidSettingType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return false;
        }
        
        // Allowed setting types in the system
        String[] validTypes = {"Role", "Category", "Unit", "POStatus", "IssueStatus", "OrderStatus"};
        
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(type.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get settings by type with validation (overloaded method)
     * @param type Setting type
     * @return List of settings with specified type
     */
    public List<Setting> getSettingsByTypeWithValidation(String type) {
        if (type == null || type.trim().isEmpty()) {
            System.out.println("Error: Setting type cannot be null or empty");
            return new ArrayList<>();
        }
        
        return getSettingsByType(type.trim());
    }
    
    /**
     * Soft delete setting (set IsActive to false instead of hard delete)
     * @param settingID Setting ID to deactivate
     * @return true if successful, false otherwise
     */
    public boolean deactivateSetting(int settingID) {
        // Check if setting is being used by users (for Role type)
        Setting setting = getSettingById(settingID);
        if (setting != null && "Role".equals(setting.getType())) {
            if (isRoleInUse(settingID)) {
                System.out.println("Error: Cannot deactivate role '" + setting.getValue() + "' because it is currently assigned to users");
                return false;
            }
        }
        
        String sql = "UPDATE Setting SET IsActive = false WHERE SettingID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, settingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Reactivate setting (set IsActive to true)
     * @param settingID Setting ID to reactivate
     * @return true if successful, false otherwise
     */
    public boolean reactivateSetting(int settingID) {
        String sql = "UPDATE Setting SET IsActive = true WHERE SettingID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, settingID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}