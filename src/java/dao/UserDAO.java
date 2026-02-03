/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.User;
import model.Setting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO extends BaseDAO {

    /**
     * Get all users with pagination and filtering
     *
     * @param page Page number (starting from 1)
     * @param pageSize Number of records per page
     * @param roleFilter Role filter (null for all roles)
     * @param searchKeyword Search keyword for name or email
     * @return List of users
     */
    public List<User> getAllUser(int page, int pageSize, Integer roleFilter, String searchKeyword, Boolean activeOnly) {
        return getAllUser(page, pageSize, roleFilter, searchKeyword, activeOnly, null, null);
    }
    
    /**
     * Get all users with pagination, filters and sorting
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @param roleFilter Role filter (null for all roles)
     * @param searchKeyword Search keyword for name or email
     * @param activeOnly Filter by active status
     * @param sortBy Sort column
     * @param sortOrder Sort order (asc/desc)
     * @return List of users
     */
    public List<User> getAllUser(int page, int pageSize, Integer roleFilter, String searchKeyword, Boolean activeOnly, String sortBy, String sortOrder) {
        List<User> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.UserID, u.FullName, u.Email, u.PasswordHash, u.Gender, u.Phone, ");
        sql.append("u.Address, u.AvatarUrl, u.RoleID, u.IsActive, u.CreatedAt, s.Value as RoleName ");
        sql.append("FROM \"User\" u ");
        sql.append("JOIN Setting s ON u.RoleID = s.SettingID ");
        sql.append("WHERE s.Type = 'Role' ");

        // Add filters
        if (roleFilter != null) {
            sql.append("AND u.RoleID = ? ");
        }
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND (u.FullName ILIKE ? OR u.Email ILIKE ?) ");
        }

        if (activeOnly != null) {
            if (activeOnly) {
                sql.append("AND u.isactive = TRUE ");
            } else {
                sql.append("AND u.isactive = FALSE ");
            }
        }

        // Add sorting
        String orderByClause = "ORDER BY ";
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String columnName;
            switch (sortBy.toLowerCase()) {
                case "id":
                    columnName = "u.UserID";
                    break;
                case "name":
                    columnName = "u.FullName";
                    break;
                case "email":
                    columnName = "u.Email";
                    break;
                case "gender":
                    columnName = "u.Gender";
                    break;
                case "role":
                    columnName = "u.RoleID";
                    break;
                case "status":
                    columnName = "u.IsActive";
                    break;
                case "created":
                    columnName = "u.CreatedAt";
                    break;
                default:
                    columnName = "u.UserID";
            }
            
            String order = "ASC";
            if (sortOrder != null && sortOrder.trim().equalsIgnoreCase("desc")) {
                order = "DESC";
            }
            
            orderByClause += columnName + " " + order + " ";
        } else {
            orderByClause += "u.UserID ASC ";
        }
        
        sql.append(orderByClause);
        //phân trang
        sql.append("LIMIT ? OFFSET ?");

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (roleFilter != null) {
                ps.setInt(paramIndex++, roleFilter);
            }
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
            }
            
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setActive(rs.getBoolean("IsActive"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt"));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Get total count of users for pagination
     *
     * @param roleFilter Role filter (null for all roles)
     * @param searchKeyword Search keyword
     * @param activeOnly
     * @return Total count
     */
    public int getTotalUserCount(Integer roleFilter, String searchKeyword, Boolean activeOnly) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM \"User\" u ");
        sql.append("JOIN Setting s ON u.RoleID = s.SettingID ");
        sql.append("WHERE s.Type = 'Role' ");

        if (roleFilter != null) {
            sql.append("AND u.RoleID = ? ");
        }
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND (u.FullName ILIKE ? OR u.Email ILIKE ?) ");
        }

        if (activeOnly != null && activeOnly) {
            sql.append("AND i.IsActive = TRUE ");
        }

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (roleFilter != null) {
                ps.setInt(paramIndex++, roleFilter);
            }
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword.trim() + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
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
     * Create new user
     *
     * @param user User object to create
     * @return true if successful, false otherwise
     */
    public boolean createUser(User user) {
        // Supabase schema: table "User" without Gender column
        String sql = "INSERT INTO \"User\" (FullName, Email, PasswordHash, Gender, Phone, Address, AvatarUrl, RoleID, IsActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getGender());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getAvatarUrl());
            stmt.setInt(8, user.getRoleID());
            stmt.setBoolean(9, user.isActive());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setUserID(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update user information
     *
     * @param user User object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE \"User\" SET FullName = ?, Email = ?, Gender = ?, Phone = ?, Address = ?, "
                + "AvatarUrl = ?, RoleID = ?, IsActive = ? WHERE UserID = ?";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getGender());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getAvatarUrl());
            stmt.setInt(7, user.getRoleID());
            stmt.setBoolean(8, user.isActive());
            stmt.setInt(9, user.getUserID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update user password
     *
     * @param userID User ID
     * @param newPasswordHash New hashed password
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(int userID, String newPasswordHash) {
        String sql = "UPDATE \"User\" SET PasswordHash = ? WHERE UserID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPasswordHash);
            ps.setInt(2, userID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Delete user (soft delete by setting IsActive to false)
     *
     * @param userID User ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(int userID) {
        String sql = "UPDATE \"User\" SET IsActive = false WHERE UserID = ?";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get user by ID
     *
     * @param userID User ID
     * @return User object or null if not found
     */
    public User getUserById(int userID) {
        String sql = "SELECT u.UserID, u.FullName, u.Email, u.PasswordHash, u.Gender, u.Phone, "
                + "u.Address, u.AvatarUrl, u.RoleID, u.IsActive, u.CreatedAt, s.Value as RoleName "
                + "FROM \"User\" u "
                + "JOIN Setting s ON u.RoleID = s.SettingID "
                + "WHERE u.UserID = ? AND s.Type = 'Role'";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setActive(rs.getBoolean("IsActive"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Check if email already exists
     *
     * @param email Email to check
     * @param excludeUserID User ID to exclude from check (for updates)
     * @return true if email exists, false otherwise
     */
    public boolean isEmailExists(String email, Integer excludeUserID) {
        String sql = "SELECT COUNT(*) FROM \"User\" WHERE Email = ?";
        if (excludeUserID != null) {
            sql += " AND UserID != ?";
        }

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            if (excludeUserID != null) {
                ps.setInt(2, excludeUserID);
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
     * Get user by email
     *
     * @param email Email to search for
     * @return User object if found, null otherwise
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT u.UserID, u.FullName, u.Email, u.PasswordHash, u.Gender, u.Phone, "
                + "u.Address, u.AvatarUrl, u.RoleID, u.IsActive, u.CreatedAt, s.Value as RoleName "
                + "FROM \"User\" u "
                + "JOIN Setting s ON u.RoleID = s.SettingID "
                + "WHERE u.Email = ? AND s.Type = 'Role'";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPasswordHash(rs.getString("PasswordHash"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRoleID(rs.getInt("RoleID"));
                user.setActive(rs.getBoolean("IsActive"));
                user.setCreatedAt(rs.getTimestamp("CreatedAt"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all available roles that HR can create (Inventory and Barista)
     *
     * @return List of roles
     */
    public List<Setting> getAvailableRolesForHR() {
        List<Setting> roles = new ArrayList<>();
        String sql = "SELECT SettingID, Type, Value, Description, IsActive "
                + "FROM Setting "
                + "WHERE Type = 'Role' AND IsActive = true "
                + "AND Value IN ('Inventory', 'Barista','User' ) "
                + "ORDER BY Value";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Setting role = new Setting();
                role.setSettingID(rs.getInt("SettingID"));
                role.setType(rs.getString("Type"));
                role.setValue(rs.getString("Value"));
                role.setDescription(rs.getString("Description"));
                role.setActive(rs.getBoolean("IsActive"));

                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Get all available roles that Admin can create (all roles)
     *
     * @return List of roles
     */
    public List<Setting> getAllRoles() {
        List<Setting> roles = new ArrayList<>();
        String sql = "SELECT SettingID, Type, Value, Description, IsActive "
                + "FROM Setting "
                + "WHERE Type = 'Role' AND IsActive = true "
                + "ORDER BY Value";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Setting role = new Setting();
                role.setSettingID(rs.getInt("SettingID"));
                role.setType(rs.getString("Type"));
                role.setValue(rs.getString("Value"));
                role.setDescription(rs.getString("Description"));
                role.setActive(rs.getBoolean("IsActive"));

                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Get available roles for Admin users (all roles except Admin)
     *
     * @return List of roles
     */
    public List<Setting> getAvailableRolesForAdmin() {
        List<Setting> roles = new ArrayList<>();
        String sql = "SELECT SettingID, Type, Value, Description, IsActive "
                + "FROM Setting "
                + "WHERE Type = 'Role' AND IsActive = true "
                + "AND Value != 'Admin' "
                + "ORDER BY Value";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Setting role = new Setting();
                role.setSettingID(rs.getInt("SettingID"));
                role.setType(rs.getString("Type"));
                role.setValue(rs.getString("Value"));
                role.setDescription(rs.getString("Description"));
                role.setActive(rs.getBoolean("IsActive"));
                roles.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    /**
     * Update user password
     *
     * @param userId User ID
     * @param newPasswordHash New hashed password
     * @return true if successful, false otherwise
     */
    public boolean updateUserPassword(int userId, String newPasswordHash) {
        String sql = "UPDATE \"User\" SET PasswordHash = ? WHERE UserID = ?";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPasswordHash);
            stmt.setInt(2, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hash password using BCrypt
     *
     * @param password Plain text password
     * @return Hashed password
     */
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
