/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.SupplierDAO;
import dao.UserDAO;
import model.Supplier;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

/**
 * Service class for Supplier management
 * Provides business logic and validation for supplier operations
 * @author DrDYNew
 */
public class SupplierService {
    
    private SupplierDAO supplierDAO;
    private UserDAO userDAO;
    
    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
        this.userDAO = new UserDAO();
    }
    
    /**
     * Get all suppliers
     * @return List of suppliers
     */
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }
    
    /**
     * Get supplier by ID
     * @param supplierID Supplier ID
     * @return Supplier object or null if not found
     */
    public Supplier getSupplierById(int supplierID) {
        return supplierDAO.getSupplierById(supplierID);
    }
    
    /**
     * Search suppliers by keyword
     * @param keyword Search keyword
     * @return List of matching suppliers
     */
    public List<Supplier> searchSuppliers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllSuppliers();
        }
        return supplierDAO.searchSuppliers(keyword.trim());
    }
    
    /**
     * Get suppliers with pagination
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @return List of suppliers
     */
    public List<Supplier> getSuppliers(int page, int pageSize) {
        return supplierDAO.getSuppliers(page, pageSize);
    }
    
    /**
     * Get suppliers with pagination and status filter
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @param statusFilter Status filter (active/inactive)
     * @return List of suppliers
     */
    public List<Supplier> getSuppliers(int page, int pageSize, String statusFilter) {
        return supplierDAO.getSuppliers(page, pageSize, statusFilter);
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
        return supplierDAO.getSuppliers(page, pageSize, statusFilter, searchFilter);
    }
    
    /**
     * Get total count of suppliers
     * @return Total count
     */
    public int getTotalSuppliersCount() {
        return supplierDAO.getTotalSuppliersCount();
    }
    
    /**
     * Get total count of suppliers with status filter
     * @param statusFilter Status filter (optional)
     * @return Total count
     */
    public int getTotalSuppliersCount(String statusFilter) {
        return supplierDAO.getTotalSuppliersCount(statusFilter);
    }
    
    /**
     * Get total count of suppliers with multiple filters
     * @param statusFilter Status filter (optional)
     * @param searchFilter Search filter (optional)
     * @return Total count
     */
    public int getTotalSuppliersCount(String statusFilter, String searchFilter) {
        return supplierDAO.getTotalSuppliersCount(statusFilter, searchFilter);
    }
    
    /**
     * Toggle supplier active status
     * @param supplierID Supplier ID
     * @return Error message if failed, null if successful
     */
    public String toggleSupplierStatus(int supplierID) {
        if (supplierID <= 0) {
            return "ID nhà cung cấp không hợp lệ";
        }
        
        // Check if supplier exists
        Supplier existingSupplier = supplierDAO.getSupplierById(supplierID);
        if (existingSupplier == null) {
            return "Nhà cung cấp không tồn tại";
        }
        
        boolean success = supplierDAO.toggleSupplierStatus(supplierID);
        
        if (success) {
            return null; // Success - no error
        } else {
            return "Có lỗi xảy ra khi cập nhật trạng thái nhà cung cấp";
        }
    }
    
    public Map<String, String> validateSupplierFields(Supplier supplier, boolean isUpdate) {
        Map<String, String> errors = new HashMap<>();
        
        // Validate supplier name
        String supplierName = supplier.getSupplierName();
        if (supplierName == null || supplierName.trim().isEmpty()) {
            errors.put("supplierName", "Tên nhà cung cấp không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedName = supplierName.trim();
            if (trimmedName.isEmpty()) {
                // Case: only whitespace like "   "
                errors.put("supplierName", "Tên nhà cung cấp không được để trống");
            } else if (!supplierName.equals(trimmedName)) {
                // Case: has leading or trailing whitespace like " ABC" or "ABC "
                errors.put("supplierName", "Tên nhà cung cấp không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Validate format - letters and spaces allowed, no numbers or special characters
                if (!isValidSupplierName(supplierName)) {
                    errors.put("supplierName", "Tên nhà cung cấp chỉ được chứa chữ , không có số hoặc ký tự đặc biệt");
                } else if (supplierName.length() > 100) {
                    errors.put("supplierName", "Tên nhà cung cấp không được vượt quá 100 ký tự");
                } else if (isSupplierNameExists(supplierName, isUpdate ? supplier.getSupplierID() : null)) {
                    errors.put("supplierName", "Tên nhà cung cấp đã tồn tại");
                } else {
                    // Normalize supplier name (already trimmed, no leading/trailing whitespace)
                    supplier.setSupplierName(supplierName);
                }
            }
        }
        
        // Validate contact name
        String contactName = supplier.getContactName();
        if (contactName == null || contactName.trim().isEmpty()) {
            errors.put("contactName", "Người liên hệ không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedContactName = contactName.trim();
            if (trimmedContactName.isEmpty()) {
                // Case: only whitespace like "   "
                errors.put("contactName", "Người liên hệ không được để trống");
            } else if (!contactName.equals(trimmedContactName)) {
                // Case: has leading or trailing whitespace like " ABC" or "ABC "
                errors.put("contactName", "Người liên hệ không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Validate format - letters and spaces allowed, no numbers or special characters
                if (!isValidContactName(contactName)) {
                    errors.put("contactName", "Tên người liên hệ chỉ được chứa chữ và khoảng trắng, không có số hoặc ký tự đặc biệt");
                } else if (contactName.length() > 100) {
                    errors.put("contactName", "Tên người liên hệ không được vượt quá 100 ký tự");
                } else {
                    // Normalize contact name (already trimmed, no leading/trailing whitespace)
                    supplier.setContactName(contactName);
                }
            }
        }
        // Validate email
        String email = supplier.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedEmail = email.trim();
            if (!email.equals(trimmedEmail)) {
                errors.put("email", "Email không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Normalize email (already trimmed)
                email = trimmedEmail;
                supplier.setEmail(email);
                if (!isValidEmail(email)) {
                    errors.put("email", "Email phải có định dạng hợp lệ (chứa @)");
                } else if (email.length() > 100) {
                    errors.put("email", "Email không được vượt quá 100 ký tự");
                } else if (isSupplierEmailExists(email, isUpdate ? supplier.getSupplierID() : null)) {
                    errors.put("email", "Email đã tồn tại ở nhà cung cấp khác");
                } else if (isUserEmailExists(email)) {
                    errors.put("email", "Email đã tồn tại ở tài khoản người dùng");
                }
            }
        }
        
        // Validate phone
        String phone = supplier.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            errors.put("phone", "Số điện thoại không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedPhone = phone.trim();
            if (!phone.equals(trimmedPhone)) {
                errors.put("phone", "Số điện thoại không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Normalize phone (already trimmed)
                phone = trimmedPhone;
                supplier.setPhone(phone);
                if (!isValidPhoneNumber(phone)) {
                    errors.put("phone", "Số điện thoại phải là 10 chữ số và không chứa ký tự đặc biệt");
                }
            }
        }
        
        // Validate address
        String address = supplier.getAddress();
        if (address == null || address.trim().isEmpty()) {
            errors.put("address", "Địa chỉ không được để trống");
        } else {
            // Check if has leading or trailing whitespace
            String trimmedAddress = address.trim();
            if (!address.equals(trimmedAddress)) {
                errors.put("address", "Địa chỉ không được chứa khoảng trắng ở đầu hoặc cuối");
            } else {
                // Normalize address (already trimmed)
                address = trimmedAddress;
                supplier.setAddress(address);
                if (address.length() > 255) {
                    errors.put("address", "Địa chỉ không được vượt quá 255 ký tự");
                }
            }
        }
        return errors;
    }

    /**
     * Create new supplier
     * @param supplier Supplier object
     * @return Map of validation errors, empty map if successful
     */
    public Map<String, String> createSupplier(Supplier supplier) {
        Map<String, String> errors = validateSupplierFields(supplier, false);
        if (!errors.isEmpty()) {
            return errors;
        }
        
        // Trim and validation already done in validateSupplierFields
        // Check if supplier name already exists
        if (isSupplierNameExists(supplier.getSupplierName(), null)) {
            Map<String, String> nameError = new HashMap<>();
            nameError.put("supplierName", "Tên nhà cung cấp đã tồn tại");
            return nameError;
        }
        
        // Check if email already exists (supplier and user)
        if (supplier.getEmail() != null && !supplier.getEmail().isEmpty()) {
            if (isSupplierEmailExists(supplier.getEmail(), null)) {
                Map<String, String> emailError = new HashMap<>();
                emailError.put("email", "Email đã tồn tại ở nhà cung cấp khác");
                return emailError;
            }
            if (isUserEmailExists(supplier.getEmail())) {
                Map<String, String> emailError = new HashMap<>();
                emailError.put("email", "Email đã tồn tại ở tài khoản người dùng");
                return emailError;
            }
        }
        
        int supplierID = supplierDAO.insertSupplier(supplier);
        
        if (supplierID > 0) {
            return new HashMap<>(); // Success - empty errors map
        } else {
            Map<String, String> systemError = new HashMap<>();
            systemError.put("system", "Có lỗi xảy ra khi thêm nhà cung cấp");
            return systemError;
        }
    }
    
    /**
     * Update existing supplier
     * @param supplier Supplier object
     * @return Map of validation errors, empty map if successful
     */
    public Map<String, String> updateSupplier(Supplier supplier) {
        if (supplier.getSupplierID() <= 0) {
            Map<String, String> errors = new HashMap<>();
            errors.put("supplierId", "ID nhà cung cấp không hợp lệ");
            return errors;
        }
        Map<String, String> errors = validateSupplierFields(supplier, true);
        if (!errors.isEmpty()) {
            return errors;
        }
        
        // Check if supplier exists
        Supplier existingSupplier = supplierDAO.getSupplierById(supplier.getSupplierID());
        if (existingSupplier == null) {
            Map<String, String> systemError = new HashMap<>();
            systemError.put("system", "Nhà cung cấp không tồn tại");
            return systemError;
        }
        
        // Check if supplier name already exists (excluding current supplier)
        if (isSupplierNameExists(supplier.getSupplierName(), supplier.getSupplierID())) {
            Map<String, String> nameError = new HashMap<>();
            nameError.put("supplierName", "Tên nhà cung cấp đã tồn tại");
            return nameError;
        }
        
        // Trim and validation already done in validateSupplierFields
        // Check if email already exists (if provided and different from current)
        if (supplier.getEmail() != null && !supplier.getEmail().isEmpty()) {
            if (!supplier.getEmail().equalsIgnoreCase(existingSupplier.getEmail())) {
                if (isSupplierEmailExists(supplier.getEmail(), supplier.getSupplierID())) {
                    Map<String, String> emailError = new HashMap<>();
                    emailError.put("email", "Email đã tồn tại ở nhà cung cấp khác");
                    return emailError;
                }
                if (isUserEmailExists(supplier.getEmail())) {
                    Map<String, String> emailError = new HashMap<>();
                    emailError.put("email", "Email đã tồn tại ở tài khoản người dùng");
                    return emailError;
                }
            }
        }
        
        boolean success = supplierDAO.updateSupplier(supplier);
        
        if (success) {
            return new HashMap<>(); // Success - empty errors map
        } else {
            Map<String, String> systemError = new HashMap<>();
            systemError.put("system", "Có lỗi xảy ra khi cập nhật nhà cung cấp");
            return systemError;
        }
    }
    
    /**
     * Delete supplier (soft delete)
     * @param supplierID Supplier ID to delete
     * @return Error message if failed, null if successful
     */
    public String deleteSupplier(int supplierID) {
        if (supplierID <= 0) {
            return "ID nhà cung cấp không hợp lệ";
        }
        
        // Check if supplier exists
        Supplier existingSupplier = supplierDAO.getSupplierById(supplierID);
        if (existingSupplier == null) {
            return "Nhà cung cấp không tồn tại";
        }
        
        if (!existingSupplier.isActive()) {
            return "Nhà cung cấp đã bị xóa";
        }
        
        boolean success = supplierDAO.deleteSupplier(supplierID);
        
        if (success) {
            return null; // Success - no error
        } else {
            return "Có lỗi xảy ra khi xóa nhà cung cấp";
        }
    }
    
    /**
     * Check if supplier name already exists
     * @param supplierName Supplier name to check
     * @param excludeSupplierID Supplier ID to exclude from check (for updates)
     * @return true if name exists, false otherwise
     */
    private boolean isSupplierNameExists(String supplierName, Integer excludeSupplierID) {
        List<Supplier> suppliers = supplierDAO.searchSuppliers(supplierName);
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierName().equalsIgnoreCase(supplierName)) {
                if (excludeSupplierID == null || supplier.getSupplierID() != excludeSupplierID) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @param excludeSupplierID Supplier ID to exclude from check (for updates)
     * @return true if email exists, false otherwise
     */
    private boolean isSupplierEmailExists(String email, Integer excludeSupplierID) {
        return supplierDAO.isSupplierEmailExists(email, excludeSupplierID);
    }

    private boolean isUserEmailExists(String email) {
        return userDAO.isEmailExists(email, null);
    }
    
    /**
     * Validate supplier name format (no numbers or special characters)
     * @param supplierName Supplier name to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidSupplierName(String supplierName) {
        if (supplierName == null || supplierName.trim().isEmpty()) {
            return false; // Supplier name is required
        }
        
        // Trim whitespace
        String trimmed = supplierName.trim();
        
        // Check if contains only letters, spaces, and Vietnamese characters
        // Allow letters (a-z, A-Z), spaces, and Vietnamese characters
        String nameRegex = "^[a-zA-Z\\s\\u00C0-\\u1EF9]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(trimmed).matches();
    }
    
    /**
     * Validate contact name format (no numbers or special characters)
     * @param contactName Contact name to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidContactName(String contactName) {
        if (contactName == null || contactName.trim().isEmpty()) {
            return false; // Contact name is required
        }
        
        // Trim whitespace
        String trimmed = contactName.trim();
        
        // Check if contains only letters, spaces, and Vietnamese characters
        // Allow letters (a-z, A-Z), spaces, and Vietnamese characters
        String nameRegex = "^[a-zA-Z\\s\\u00C0-\\u1EF9]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(trimmed).matches();
    }
    
    /**
     * Validate email format (must contain @)
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Allow empty email
        }
        
        // Trim whitespace
        String trimmed = email.trim();
        
        // Simple validation: must contain @ and have valid format
        return trimmed.contains("@") && 
               trimmed.length() > 3 && 
               !trimmed.startsWith("@") && 
               !trimmed.endsWith("@") &&
               trimmed.indexOf("@") == trimmed.lastIndexOf("@"); // Only one @
    }
    
    /**
     * Validate phone number format (exactly 10 digits, no special characters)
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Allow empty phone
        }
        
        // Trim whitespace
        String trimmed = phone.trim();
        
        // Check if contains only digits
        String phoneRegex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(trimmed).matches();
    }
      public List<Supplier> getActiveSuppliers() {
        return supplierDAO.getActiveSuppliers();
    }
}



