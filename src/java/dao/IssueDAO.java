package dao;

import model.Issue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * DAO class for Issue management
 */
public class IssueDAO extends BaseDAO {
    
    /**
     * Get all issues with pagination, filtering, and detailed information
     * @param page Page number (starting from 1)
     * @param pageSize Number of records per page
     * @param statusFilter Status filter (null for all statuses)
     * @param ingredientFilter Ingredient filter (null for all ingredients)
     * @param createdByFilter CreatedBy filter (null for all creators)
     * @return List of issues with detailed information
     */
    public List<Issue> getAllIssues(int page, int pageSize, Integer statusFilter, 
                                   Integer ingredientFilter, Integer createdByFilter) {
        return getAllIssues(page, pageSize, statusFilter, ingredientFilter, createdByFilter, null, null);
    }
    
    /**
     * Get all issues with pagination, filtering, sorting, and detailed information
     * @param page Page number (starting from 1)
     * @param pageSize Number of records per page
     * @param statusFilter Status filter (null for all statuses)
     * @param ingredientFilter Ingredient filter (null for all ingredients)
     * @param createdByFilter CreatedBy filter (null for all creators)
     * @param sortBy Column to sort by (optional)
     * @param sortOrder Sort order: asc or desc (optional)
     * @return List of issues with detailed information
     */
    public List<Issue> getAllIssues(int page, int pageSize, Integer statusFilter, 
                                   Integer ingredientFilter, Integer createdByFilter,
                                   String sortBy, String sortOrder) {
        List<Issue> issues = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.IssueID, i.IngredientID, i.Description, i.Quantity, ");
        sql.append("i.StatusID, i.CreatedBy, i.ConfirmedBy, i.CreatedAt, ");
        sql.append("ing.Name as IngredientName, ");
        sql.append("u.Value as UnitName, ");
        sql.append("st.Value as StatusName, ");
        sql.append("uc.FullName as CreatedByName, ");
        sql.append("uconf.FullName as ConfirmedByName ");
        sql.append("FROM Issue i ");
        sql.append("LEFT JOIN Ingredient ing ON i.IngredientID = ing.IngredientID ");
        sql.append("LEFT JOIN Setting u ON ing.UnitID = u.SettingID ");
        sql.append("LEFT JOIN Setting st ON i.StatusID = st.SettingID ");
        sql.append("LEFT JOIN \"User\" uc ON i.CreatedBy = uc.UserID ");
        sql.append("LEFT JOIN \"User\" uconf ON i.ConfirmedBy = uconf.UserID ");
        sql.append("WHERE 1=1 ");
        
        // Add filters
        List<Object> params = new ArrayList<>();
        if (statusFilter != null) {
            sql.append("AND i.StatusID = ? ");
            params.add(statusFilter);
        }
        if (ingredientFilter != null) {
            sql.append("AND i.IngredientID = ? ");
            params.add(ingredientFilter);
        }
        if (createdByFilter != null) {
            sql.append("AND i.CreatedBy = ? ");
            params.add(createdByFilter);
        }
        
        // Add sorting
        String orderByClause = "ORDER BY ";
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            String columnName;
            switch (sortBy.toLowerCase()) {
                case "id":
                    columnName = "i.IssueID";
                    break;
                case "ingredient":
                    columnName = "ing.Name";
                    break;
                case "quantity":
                    columnName = "i.Quantity";
                    break;
                case "status":
                    columnName = "st.Value";
                    break;
                case "created":
                    columnName = "i.CreatedAt";
                    break;
                case "reporter":
                    columnName = "uc.FullName";
                    break;
                default:
                    columnName = "i.CreatedAt";
            }
            
            String order = "ASC";
            if (sortOrder != null && sortOrder.trim().equalsIgnoreCase("desc")) {
                order = "DESC";
            }
            
            orderByClause += columnName + " " + order + " ";
        } else {
            orderByClause += "i.CreatedAt DESC ";
        }
        
        sql.append(orderByClause);
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
                Issue issue = new Issue();
                issue.setIssueID(rs.getInt("IssueID"));
                issue.setIngredientID(rs.getInt("IngredientID"));
                // Lấy description trực tiếp, không parse IssueType
                issue.setDescription(rs.getString("Description"));
                issue.setIssueType(null); // Không dùng IssueType nữa
                issue.setQuantity(rs.getBigDecimal("Quantity"));
                issue.setStatusID(rs.getInt("StatusID"));
                issue.setCreatedBy(rs.getInt("CreatedBy"));
                issue.setConfirmedBy((Integer) rs.getObject("ConfirmedBy"));
                issue.setCreatedAt(rs.getTimestamp("CreatedAt"));
                
                // Set additional display fields
                issue.setIngredientName(rs.getString("IngredientName"));
                issue.setUnitName(rs.getString("UnitName"));
                issue.setStatusName(rs.getString("StatusName"));
                issue.setCreatedByName(rs.getString("CreatedByName"));
                issue.setConfirmedByName(rs.getString("ConfirmedByName"));
                
                issues.add(issue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return issues;
    }
    
    /**
     * Get total count of issues with filters
     * @param statusFilter Status filter (null for all statuses)
     * @param ingredientFilter Ingredient filter (null for all ingredients)
     * @param createdByFilter CreatedBy filter (null for all creators)
     * @return Total count
     */
    public int getTotalIssueCount(Integer statusFilter, Integer ingredientFilter, Integer createdByFilter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM Issue WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        if (statusFilter != null) {
            sql.append("AND StatusID = ? ");
            params.add(statusFilter);
        }
        if (ingredientFilter != null) {
            sql.append("AND IngredientID = ? ");
            params.add(ingredientFilter);
        }
        if (createdByFilter != null) {
            sql.append("AND CreatedBy = ? ");
            params.add(createdByFilter);
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
     * Get issue by ID with detailed information
     * @param issueID Issue ID
     * @return Issue object or null if not found
     */
    public Issue getIssueById(int issueID) {
        String sql = "SELECT i.IssueID, i.IngredientID, i.Description, i.Quantity, " +
                    "i.StatusID, i.CreatedBy, i.ConfirmedBy, i.CreatedAt, " +
                    "ing.Name as IngredientName, " +
                    "u.Value as UnitName, " +
                    "st.Value as StatusName, " +
                    "uc.FullName as CreatedByName, " +
                    "uconf.FullName as ConfirmedByName " +
                    "FROM Issue i " +
                    "LEFT JOIN Ingredient ing ON i.IngredientID = ing.IngredientID " +
                    "LEFT JOIN Setting u ON ing.UnitID = u.SettingID " +
                    "LEFT JOIN Setting st ON i.StatusID = st.SettingID " +
                    "LEFT JOIN \"User\" uc ON i.CreatedBy = uc.UserID " +
                    "LEFT JOIN \"User\" uconf ON i.ConfirmedBy = uconf.UserID " +
                    "WHERE i.IssueID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, issueID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Issue issue = new Issue();
                issue.setIssueID(rs.getInt("IssueID"));
                issue.setIngredientID(rs.getInt("IngredientID"));
                // Lấy description trực tiếp, không parse IssueType
                issue.setDescription(rs.getString("Description"));
                issue.setIssueType(null); // Không dùng IssueType nữa
                issue.setQuantity(rs.getBigDecimal("Quantity"));
                issue.setStatusID(rs.getInt("StatusID"));
                issue.setCreatedBy(rs.getInt("CreatedBy"));
                issue.setConfirmedBy((Integer) rs.getObject("ConfirmedBy"));
                issue.setCreatedAt(rs.getTimestamp("CreatedAt"));
                
                // Set additional display fields
                issue.setIngredientName(rs.getString("IngredientName"));
                issue.setUnitName(rs.getString("UnitName"));
                issue.setStatusName(rs.getString("StatusName"));
                issue.setCreatedByName(rs.getString("CreatedByName"));
                issue.setConfirmedByName(rs.getString("ConfirmedByName"));
                
                return issue;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all issues that share the same description (request) and creator.
     * This is used to display multi-ingredient requests together.
     */
    public List<Issue> getIssuesByDescriptionAndCreator(String description, int createdBy) {
        List<Issue> issues = new ArrayList<>();
        String sql = "SELECT i.IssueID, i.IngredientID, i.Description, i.Quantity, " +
                    "i.StatusID, i.CreatedBy, i.ConfirmedBy, i.CreatedAt, " +
                    "ing.Name as IngredientName, " +
                    "u.Value as UnitName, " +
                    "st.Value as StatusName, " +
                    "uc.FullName as CreatedByName, " +
                    "uconf.FullName as ConfirmedByName " +
                    "FROM Issue i " +
                    "LEFT JOIN Ingredient ing ON i.IngredientID = ing.IngredientID " +
                    "LEFT JOIN Setting u ON ing.UnitID = u.SettingID " +
                    "LEFT JOIN Setting st ON i.StatusID = st.SettingID " +
                    "LEFT JOIN \"User\" uc ON i.CreatedBy = uc.UserID " +
                    "LEFT JOIN \"User\" uconf ON i.ConfirmedBy = uconf.UserID " +
                    "WHERE i.Description = ? AND i.CreatedBy = ? " +
                    "ORDER BY i.IssueID";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, description);
            ps.setInt(2, createdBy);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Issue issue = new Issue();
                issue.setIssueID(rs.getInt("IssueID"));
                issue.setIngredientID(rs.getInt("IngredientID"));

                // Lấy description trực tiếp, không parse IssueType
                issue.setDescription(rs.getString("Description"));
                issue.setIssueType(null); // Không dùng IssueType nữa
                issue.setQuantity(rs.getBigDecimal("Quantity"));
                issue.setStatusID(rs.getInt("StatusID"));
                issue.setCreatedBy(rs.getInt("CreatedBy"));
                issue.setConfirmedBy((Integer) rs.getObject("ConfirmedBy"));
                issue.setCreatedAt(rs.getTimestamp("CreatedAt"));

                issue.setIngredientName(rs.getString("IngredientName"));
                issue.setUnitName(rs.getString("UnitName"));
                issue.setStatusName(rs.getString("StatusName"));
                issue.setCreatedByName(rs.getString("CreatedByName"));
                issue.setConfirmedByName(rs.getString("ConfirmedByName"));

                issues.add(issue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return issues;
    }

    /**
     * Create new issue
     * @param issue Issue object
     * @return Generated issue ID, or -1 if failed
     */
    public int createIssue(Issue issue) {
        // Note: IssueType is not a column in the Issue table, it's stored in Description or not used
        String sql = "INSERT INTO Issue (IngredientID, Description, Quantity, StatusID, CreatedBy, ConfirmedBy) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, issue.getIngredientID());
            // Lưu description trực tiếp, không thêm IssueType
            ps.setString(2, issue.getDescription());
            ps.setBigDecimal(3, issue.getQuantity());
            ps.setInt(4, issue.getStatusID());
            ps.setInt(5, issue.getCreatedBy());
            if (issue.getConfirmedBy() != null) {
                ps.setInt(6, issue.getConfirmedBy());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("=== SQL ERROR in createIssue ===");
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Update issue
     * @param issue Issue object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateIssue(Issue issue) {
        String sql = "UPDATE Issue SET IngredientID = ?, Description = ?, Quantity = ?, " +
                    "StatusID = ?, ConfirmedBy = ? WHERE IssueID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, issue.getIngredientID());
            ps.setString(2, issue.getDescription());
            ps.setBigDecimal(3, issue.getQuantity());
            ps.setInt(4, issue.getStatusID());
            if (issue.getConfirmedBy() != null) {
                ps.setInt(5, issue.getConfirmedBy());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, issue.getIssueID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete issue by ID
     * @param issueID Issue ID
     * @return true if successful, false otherwise
     */
    public boolean deleteIssue(int issueID) {
        String sql = "DELETE FROM Issue WHERE IssueID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, issueID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update issue status
     * @param issueID Issue ID
     * @param statusID New status ID
     * @return true if successful
     */
    public boolean updateIssueStatus(int issueID, int statusID) {
        String sql = "UPDATE Issue SET StatusID = ? WHERE IssueID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, statusID);
            ps.setInt(2, issueID);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Resolve issue (set status to Resolved)
     * @param issueID Issue ID
     * @return true if successful
     */
    public boolean resolveIssue(int issueID) {
        // Use hardcoded Resolved StatusID = 27
        String updateSQL = "UPDATE Issue SET StatusID = 27 WHERE IssueID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
            
            // Update issue to Resolved status
            psUpdate.setInt(1, issueID);
            
            return psUpdate.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Reject issue with reason
     * @param issueID Issue ID
     * @param rejectionReason Reason for rejection
     * @return true if successful
     */
    public boolean rejectIssue(int issueID, String rejectionReason) {
        // Use hardcoded Rejected StatusID = 28
        String updateSQL = "UPDATE Issue SET StatusID = 28, RejectionReason = ? WHERE IssueID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement psUpdate = conn.prepareStatement(updateSQL)) {
            
            // Update issue to Rejected status
            psUpdate.setString(1, rejectionReason);
            psUpdate.setInt(2, issueID);
            
            return psUpdate.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get issue statistics by status
     * @return Array of counts [Reported, Under Investigation, Resolved, Rejected]
     */
    public int[] getIssueStatsByStatus() {
        int[] stats = new int[4]; // [Reported, Under Investigation, Resolved, Rejected]
        String sql = "SELECT st.Value as StatusName, COUNT(*) as Count " +
                    "FROM Issue i " +
                    "JOIN Setting st ON i.StatusID = st.SettingID " +
                    "WHERE st.Type = 'IssueStatus' " +
                    "GROUP BY st.Value";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String statusName = rs.getString("StatusName");
                int count = rs.getInt("Count");
                
                switch (statusName) {
                    case "Reported":
                        stats[0] = count;
                        break;
                    case "Under Investigation":
                        stats[1] = count;
                        break;
                    case "Resolved":
                        stats[2] = count;
                        break;
                    case "Rejected":
                        stats[3] = count;
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
