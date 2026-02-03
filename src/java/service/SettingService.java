package service;

import dao.SettingDAO;
import model.Setting;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service class for Setting management
 */
public class SettingService {
    private SettingDAO settingDAO;
    
    public SettingService() {
        this.settingDAO = new SettingDAO();
    }
    
    /**
     * Get all settings
     * @return List of all settings
     */
    public List<Setting> getAllSettings() {
        return settingDAO.getAllSettings();
    }
    
    /**
     * Get settings by type
     * @param type Setting type
     * @return List of settings
     */
    public List<Setting> getSettingsByType(String type) {
        return settingDAO.getSettingsByType(type);
    }
    
    /**
     * Get setting by ID
     * @param settingID Setting ID
     * @return Setting object
     */
    public Setting getSettingById(int settingID) {
        return settingDAO.getSettingById(settingID);
    }
    
    /**
     * Get settings with pagination
     * @param page Page number
     * @param pageSize Page size
     * @param typeFilter Type filter
     * @return List of settings
     */
    public List<Setting> getSettings(int page, int pageSize, String typeFilter) {
        return settingDAO.getSettings(page, pageSize, typeFilter);
    }
    
    /**
     * Get settings with pagination and multiple filters
     * @param page Page number
     * @param pageSize Page size
     * @param typeFilter Type filter
     * @param searchFilter Search filter
     * @param statusFilter Status filter
     * @return List of settings
     */
    public List<Setting> getSettings(int page, int pageSize, String typeFilter, String searchFilter, String statusFilter) {
        return settingDAO.getSettings(page, pageSize, typeFilter, searchFilter, statusFilter);
    }
    
    /**
     * Get total settings count
     * @param typeFilter Type filter
     * @return Total count
     */
    public int getTotalSettingsCount(String typeFilter) {
        return settingDAO.getTotalSettingsCount(typeFilter);
    }
    
    /**
     * Get total settings count with multiple filters
     * @param typeFilter Type filter
     * @param searchFilter Search filter
     * @param statusFilter Status filter
     * @return Total count
     */
    public int getTotalSettingsCount(String typeFilter, String searchFilter, String statusFilter) {
        return settingDAO.getTotalSettingsCount(typeFilter, searchFilter, statusFilter);
    }
    
    /**
     * Get distinct types
     * @return List of types
     */
    public List<String> getDistinctTypes() {
        return settingDAO.getDistinctTypes();
    }
    
    /**
     * Validate setting data
     * @param setting Setting object
     * @param isUpdate true if this is an update operation
     * @return Map of field errors, empty map if valid
     */
    public Map<String, String> validateSetting(Setting setting, boolean isUpdate) {
        Map<String, String> errors = new HashMap<>();
        
        // Validate name field
        String name = setting.getName();
        if (name == null || name.trim().isEmpty()) {
            errors.put("name", "Tên cài đặt không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedName = name.trim();
            if (!name.equals(trimmedName)) {
                errors.put("name", "Tên cài đặt không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Validate format - letters and spaces allowed, no numbers or special characters
                if (!isValidName(name)) {
                    errors.put("name", "Tên cài đặt chỉ được chứa chữ cái và khoảng trắng, không được có số hoặc ký tự đặc biệt");
                }
            }
        }
        
        // Check uniqueness of name (only if name is valid)
        if (!errors.containsKey("name") && name != null && !name.trim().isEmpty()) {
            // Normalize name (trim spaces) before checking uniqueness
            String normalizedName = name.trim();
            if (!normalizedName.equals(name)) {
                setting.setName(normalizedName);
                name = normalizedName;
            }
            Integer excludeSettingID = isUpdate ? setting.getSettingID() : null;
            if (settingDAO.isSettingNameExists(normalizedName, excludeSettingID)) {
                errors.put("name", "Tên cài đặt này đã tồn tại trong hệ thống");
            }
        }
        
        // Validate type field (dropdown - only need to check if empty and valid value)
        String type = setting.getType() != null ? setting.getType().trim() : null;
        if (type == null || type.isEmpty()) {
            errors.put("type", "Loại cài đặt không được để trống");
        } else {
            // Normalize type (trim spaces)
            if (!type.equals(setting.getType())) {
                setting.setType(type);
            }
            // Type is from dropdown, just verify it's in allowed list
            if (!isAllowedType(type)) {
                errors.put("type", "Loại cài đặt không hợp lệ. Chỉ cho phép: Role, Category, Unit, POStatus, IssueStatus, OrderStatus");
            }
        }
        
        // Validate value field
        String value = setting.getValue();
        if (value == null || value.trim().isEmpty()) {
            errors.put("value", "Giá trị không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedValue = value.trim();
            if (!value.equals(trimmedValue)) {
                errors.put("value", "Giá trị không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Normalize value (already trimmed)
                value = trimmedValue;
                if (!value.equals(setting.getValue())) {
                    setting.setValue(value);
                }
                // Validate format - letters and spaces allowed, no numbers or special characters
                if (!isValidValue(value)) {
                    errors.put("value", "Giá trị chỉ được chứa chữ cái và khoảng trắng, không được có số hoặc ký tự đặc biệt");
                }
            }
        }
        
        // Validate priority field
        if (setting.getPriority() < 1) {
            errors.put("priority", "Độ ưu tiên phải lớn hơn 0");
        }
        
        // Validate description field
        if (setting.getDescription() != null && !setting.getDescription().trim().isEmpty()) {
            String description = setting.getDescription();
            // Check if has leading or trailing whitespace
            String trimmedDescription = description.trim();
            if (!description.equals(trimmedDescription)) {
                errors.put("description", "Mô tả không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Normalize description (already trimmed)
                description = trimmedDescription;
                if (!description.equals(setting.getDescription())) {
                    setting.setDescription(description);
                }
                // Check length after trimming
                if (description.length() > 255) {
                    errors.put("description", "Mô tả không được vượt quá 255 ký tự");
                }
            }
        }
        
        // Check uniqueness of value within type (only if value and type are valid)
        if (!errors.containsKey("value") && !errors.containsKey("type") && value != null && !value.trim().isEmpty() && type != null) {
            Integer excludeSettingID = isUpdate ? setting.getSettingID() : null;
            if (settingDAO.isSettingValueExists(type, value.trim(), excludeSettingID)) {
                errors.put("value", "Giá trị này đã tồn tại trong loại cài đặt");
            }
        }
        
        // Check uniqueness of priority within type (only if priority and type are valid)
        if (!errors.containsKey("priority") && !errors.containsKey("type") && type != null) {
            Integer excludeSettingID = isUpdate ? setting.getSettingID() : null;
            if (settingDAO.isSettingPriorityExists(type, setting.getPriority(), excludeSettingID)) {
                errors.put("priority", "Độ ưu tiên này đã tồn tại trong loại cài đặt");
            }
        }
        
        return errors;
    }
    
    /**
     * Create new setting
     * @param setting Setting object
     * @return Map of validation errors, empty map if successful
     */
    public Map<String, String> createSetting(Setting setting) {
        // Validate setting data
        Map<String, String> errors = validateSetting(setting, false);
        if (!errors.isEmpty()) {
            return errors;
        }
        
        // Create setting
        if (settingDAO.createSetting(setting)) {
            return new HashMap<>(); // Success - empty errors map
        } else {
            Map<String, String> systemError = new HashMap<>();
            systemError.put("system", "Lỗi hệ thống khi tạo cài đặt");
            return systemError;
        }
    }
    
    /**
     * Update existing setting
     * @param setting Setting object
     * @return Map of validation errors, empty map if successful
     */
    public Map<String, String> updateSetting(Setting setting) {
        // Check if setting exists
        Setting existingSetting = settingDAO.getSettingById(setting.getSettingID());
        if (existingSetting == null) {
            Map<String, String> errors = new HashMap<>();
            errors.put("system", "Không tìm thấy cài đặt");
            return errors;
        }
        
        // Validate setting data
        Map<String, String> errors = validateSetting(setting, true);
        if (!errors.isEmpty()) {
            return errors;
        }
        
        // Update setting
        if (settingDAO.updateSetting(setting)) {
            return new HashMap<>(); // Success - empty errors map
        } else {
            Map<String, String> systemError = new HashMap<>();
            systemError.put("system", "Lỗi hệ thống khi cập nhật cài đặt");
            return systemError;
        }
    }
    
    /**
     * Delete setting
     * @param settingID Setting ID
     * @return Error message if failed, null if successful
     */
    public String deleteSetting(int settingID) {
        // Check if setting exists
        Setting setting = settingDAO.getSettingById(settingID);
        if (setting == null) {
            return "Không tìm thấy cài đặt";
        }
        
        // Check if setting is being used (basic validation)
        // Note: This would need more comprehensive checking based on business rules
        if (isSettingInUse(settingID)) {
            return "Không thể xóa cài đặt này vì đang được sử dụng trong hệ thống";
        }
        
        // Delete setting
        if (settingDAO.deleteSetting(settingID)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi xóa cài đặt";
        }
    }
    
    /**
     * Toggle setting active status
     * @param settingID Setting ID
     * @return Error message if failed, null if successful
     */
    public String toggleSetting(int settingID) {
        // Check if setting exists
        Setting setting = settingDAO.getSettingById(settingID);
        if (setting == null) {
            return "Không tìm thấy cài đặt";
        }
        
        // Toggle active status
        setting.setActive(!setting.isActive());
        
        // Update setting
        if (settingDAO.updateSetting(setting)) {
            return null; // Success
        } else {
            return "Lỗi hệ thống khi cập nhật trạng thái cài đặt";
        }
    }
    
    /**
     * Check if type is one of the allowed application types
     */
    private boolean isAllowedType(String type) {
        // Exactly 6 types supported for admin settings management
        return "Role".equals(type)
            || "Category".equals(type)
            || "Unit".equals(type)
            || "POStatus".equals(type)
            || "IssueStatus".equals(type)
            || "OrderStatus".equals(type);
    }
    
    /**
     * Validate name format - letters (a-z, A-Z) and spaces allowed in the middle, no numbers or special characters
     * @param name Name (should already be trimmed - no leading/trailing spaces)
     * @return true if valid
     */
    private boolean isValidName(String name) {
        // Allow letters (a-z, A-Z) and spaces in the middle, no numbers or special characters
        // Must not start or end with space (should be trimmed before calling)
        return name.matches("^[a-zA-Z]+(\\s+[a-zA-Z]+)*$");
    }
    
    /**
     * Validate value format - letters (a-z, A-Z) and spaces allowed in the middle, no numbers or special characters
     * @param value Value (should already be trimmed - no leading/trailing spaces)
     * @return true if valid
     */
    private boolean isValidValue(String value) {
        // Allow letters (a-z, A-Z) and spaces in the middle, no numbers or special characters
        // Must not start or end with space (should be trimmed before calling)
        return value.matches("^[a-zA-Z]+(\\s+[a-zA-Z]+)*$");
    }
    
    /**
     * Check if setting is being used in the system
     * @param settingID Setting ID
     * @return true if in use
     */
    private boolean isSettingInUse(int settingID) {
        // TODO: Implement comprehensive checking
        // This should check if the setting is referenced in:
        // - Users table (RoleID)
        // - Products table (CategoryID)
        // - Ingredients table (UnitID)
        // - PurchaseOrders table (StatusID)
        // - Issues table (StatusID)
        // - Orders table (StatusID)
        
        // For now, return false to allow deletion
        // In production, implement proper foreign key checking
        return false;
    }
}