package dao;

import model.Shift;
import model.ShiftAssignment;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAO extends BaseDAO {

    // ==================== SHIFT MANAGEMENT ====================
    
    /**
     * Get all shifts with manager names
     */
    public List<Shift> getAllShifts() {
        List<Shift> shifts = new ArrayList<>();
        String sql = "SELECT s.ShiftID, s.ShiftName, s.StartTime, s.EndTime, " +
                     "s.Description, s.ManagerID, s.IsActive, s.CreatedAt, " +
                     "u.FullName as ManagerName " +
                     "FROM Shift s " +
                     "LEFT JOIN \"User\" u ON s.ManagerID = u.UserID " +
                     "ORDER BY s.ShiftID DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Shift shift = new Shift();
                shift.setShiftID(rs.getInt("ShiftID"));
                shift.setShiftName(rs.getString("ShiftName"));
                shift.setStartTime(rs.getTime("StartTime"));
                shift.setEndTime(rs.getTime("EndTime"));
                shift.setDescription(rs.getString("Description"));
                shift.setManagerID(rs.getInt("ManagerID"));
                shift.setManagerName(rs.getString("ManagerName"));
                shift.setActive(rs.getBoolean("IsActive"));
                shift.setCreatedAt(rs.getTimestamp("CreatedAt"));
                shifts.add(shift);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all shifts: " + e.getMessage());
            e.printStackTrace();
        }
        return shifts;
    }

    /**
     * Get shift by ID
     */
    public Shift getShiftById(int shiftID) {
        String sql = "SELECT s.ShiftID, s.ShiftName, s.StartTime, s.EndTime, " +
                     "s.Description, s.ManagerID, s.IsActive, s.CreatedAt, " +
                     "u.FullName as ManagerName " +
                     "FROM Shift s " +
                     "LEFT JOIN \"User\" u ON s.ManagerID = u.UserID " +
                     "WHERE s.ShiftID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shiftID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Shift shift = new Shift();
                shift.setShiftID(rs.getInt("ShiftID"));
                shift.setShiftName(rs.getString("ShiftName"));
                shift.setStartTime(rs.getTime("StartTime"));
                shift.setEndTime(rs.getTime("EndTime"));
                shift.setDescription(rs.getString("Description"));
                shift.setManagerID(rs.getInt("ManagerID"));
                shift.setManagerName(rs.getString("ManagerName"));
                shift.setActive(rs.getBoolean("IsActive"));
                shift.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return shift;
            }
        } catch (SQLException e) {
            System.err.println("Error getting shift by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Insert new shift
     */
    public boolean insertShift(Shift shift) {
        String sql = "INSERT INTO Shift (ShiftName, StartTime, EndTime, Description, ManagerID, IsActive) " +
                     "VALUES (?, ?::time, ?::time, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, shift.getShiftName());
            stmt.setString(2, shift.getStartTime().toString());
            stmt.setString(3, shift.getEndTime().toString());
            stmt.setString(4, shift.getDescription());
            if (shift.getManagerID() != null) {
                stmt.setInt(5, shift.getManagerID());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setBoolean(6, shift.isActive());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting shift: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update shift
     */
    public boolean updateShift(Shift shift) {
        String sql = "UPDATE Shift SET ShiftName = ?, StartTime = ?::time, EndTime = ?::time, " +
                     "Description = ?, ManagerID = ?, IsActive = ? " +
                     "WHERE ShiftID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, shift.getShiftName());
            stmt.setString(2, shift.getStartTime().toString());
            stmt.setString(3, shift.getEndTime().toString());
            stmt.setString(4, shift.getDescription());
            if (shift.getManagerID() != null) {
                stmt.setInt(5, shift.getManagerID());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setBoolean(6, shift.isActive());
            stmt.setInt(7, shift.getShiftID());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating shift: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Toggle shift active status
     */
    public boolean toggleShiftStatus(int shiftID) {
        String sql = "UPDATE Shift SET IsActive = NOT IsActive WHERE ShiftID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shiftID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error toggling shift status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete shift
     */
    public boolean deleteShift(int shiftID) {
        String sql = "DELETE FROM Shift WHERE ShiftID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shiftID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting shift: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ==================== SHIFT ASSIGNMENT MANAGEMENT ====================
    
    /**
     * Get all shift assignments with details
     */
    public List<ShiftAssignment> getAllAssignments() {
        List<ShiftAssignment> assignments = new ArrayList<>();
        String sql = "SELECT sa.AssignmentID, sa.ShiftID, sa.UserID, sa.AssignedDate, " +
                     "sa.Notes, sa.CreatedAt, " +
                     "s.ShiftName, s.StartTime, s.EndTime, " +
                     "u.FullName as UserName, u.Email as UserEmail " +
                     "FROM ShiftAssignment sa " +
                     "JOIN Shift s ON sa.ShiftID = s.ShiftID " +
                     "JOIN \"User\" u ON sa.UserID = u.UserID " +
                     "ORDER BY sa.AssignedDate DESC, s.StartTime";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ShiftAssignment assignment = new ShiftAssignment();
                assignment.setAssignmentID(rs.getInt("AssignmentID"));
                assignment.setShiftID(rs.getInt("ShiftID"));
                assignment.setUserID(rs.getInt("UserID"));
                assignment.setAssignedDate(rs.getDate("AssignedDate"));
                assignment.setNotes(rs.getString("Notes"));
                assignment.setCreatedAt(rs.getTimestamp("CreatedAt"));
                assignment.setShiftName(rs.getString("ShiftName"));
                assignment.setUserName(rs.getString("UserName"));
                assignment.setUserEmail(rs.getString("UserEmail"));
                assignment.setStartTime(rs.getTime("StartTime").toString());
                assignment.setEndTime(rs.getTime("EndTime").toString());
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all assignments: " + e.getMessage());
            e.printStackTrace();
        }
        return assignments;
    }

    /**
     * Get assignments by shift ID
     */
    public List<ShiftAssignment> getAssignmentsByShiftId(int shiftID) {
        List<ShiftAssignment> assignments = new ArrayList<>();
        String sql = "SELECT sa.AssignmentID, sa.ShiftID, sa.UserID, sa.AssignedDate, " +
                     "sa.Notes, sa.CreatedAt, " +
                     "s.ShiftName, s.StartTime, s.EndTime, " +
                     "u.FullName as UserName, u.Email as UserEmail " +
                     "FROM ShiftAssignment sa " +
                     "JOIN Shift s ON sa.ShiftID = s.ShiftID " +
                     "JOIN \"User\" u ON sa.UserID = u.UserID " +
                     "WHERE sa.ShiftID = ? " +
                     "ORDER BY sa.AssignedDate DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shiftID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ShiftAssignment assignment = new ShiftAssignment();
                assignment.setAssignmentID(rs.getInt("AssignmentID"));
                assignment.setShiftID(rs.getInt("ShiftID"));
                assignment.setUserID(rs.getInt("UserID"));
                assignment.setAssignedDate(rs.getDate("AssignedDate"));
                assignment.setNotes(rs.getString("Notes"));
                assignment.setCreatedAt(rs.getTimestamp("CreatedAt"));
                assignment.setShiftName(rs.getString("ShiftName"));
                assignment.setUserName(rs.getString("UserName"));
                assignment.setUserEmail(rs.getString("UserEmail"));
                assignment.setStartTime(rs.getTime("StartTime").toString());
                assignment.setEndTime(rs.getTime("EndTime").toString());
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting assignments by shift ID: " + e.getMessage());
            e.printStackTrace();
        }
        return assignments;
    }

    /**
     * Get assignments by user ID
     */
    public List<ShiftAssignment> getAssignmentsByUserId(int userID) {
        List<ShiftAssignment> assignments = new ArrayList<>();
        String sql = "SELECT sa.AssignmentID, sa.ShiftID, sa.UserID, sa.AssignedDate, " +
                     "sa.Notes, sa.CreatedAt, " +
                     "s.ShiftName, s.StartTime, s.EndTime, " +
                     "u.FullName as UserName, u.Email as UserEmail " +
                     "FROM ShiftAssignment sa " +
                     "JOIN Shift s ON sa.ShiftID = s.ShiftID " +
                     "JOIN \"User\" u ON sa.UserID = u.UserID " +
                     "WHERE sa.UserID = ? " +
                     "ORDER BY sa.AssignedDate DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ShiftAssignment assignment = new ShiftAssignment();
                assignment.setAssignmentID(rs.getInt("AssignmentID"));
                assignment.setShiftID(rs.getInt("ShiftID"));
                assignment.setUserID(rs.getInt("UserID"));
                assignment.setAssignedDate(rs.getDate("AssignedDate"));
                assignment.setNotes(rs.getString("Notes"));
                assignment.setCreatedAt(rs.getTimestamp("CreatedAt"));
                assignment.setShiftName(rs.getString("ShiftName"));
                assignment.setUserName(rs.getString("UserName"));
                assignment.setUserEmail(rs.getString("UserEmail"));
                assignment.setStartTime(rs.getTime("StartTime").toString());
                assignment.setEndTime(rs.getTime("EndTime").toString());
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            System.err.println("Error getting assignments by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return assignments;
    }

    /**
     * Insert new shift assignment
     */
    public boolean insertAssignment(ShiftAssignment assignment) {
        String sql = "INSERT INTO ShiftAssignment (ShiftID, UserID, AssignedDate, Notes) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, assignment.getShiftID());
            stmt.setInt(2, assignment.getUserID());
            stmt.setDate(3, assignment.getAssignedDate());
            stmt.setString(4, assignment.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting assignment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete assignment
     */
    public boolean deleteAssignment(int assignmentID) {
        String sql = "DELETE FROM ShiftAssignment WHERE AssignmentID = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, assignmentID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting assignment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all active users (for assignment dropdown)
     */
    public List<User> getAllActiveUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT UserID, FullName, Email FROM \"User\" WHERE IsActive = TRUE ORDER BY FullName";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Get all active shifts (for assignment dropdown)
     */
    public List<Shift> getAllActiveShifts() {
        List<Shift> shifts = new ArrayList<>();
        String sql = "SELECT ShiftID, ShiftName, StartTime, EndTime FROM Shift WHERE IsActive = TRUE ORDER BY StartTime";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Shift shift = new Shift();
                shift.setShiftID(rs.getInt("ShiftID"));
                shift.setShiftName(rs.getString("ShiftName"));
                shift.setStartTime(rs.getTime("StartTime"));
                shift.setEndTime(rs.getTime("EndTime"));
                shifts.add(shift);
            }
        } catch (SQLException e) {
            System.err.println("Error getting active shifts: " + e.getMessage());
            e.printStackTrace();
        }
        return shifts;
    }
}
