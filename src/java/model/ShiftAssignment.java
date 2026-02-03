package model;

import java.sql.Date;
import java.sql.Timestamp;

public class ShiftAssignment {
    private int assignmentID;
    private int shiftID;
    private int userID;
    private Date assignedDate;
    private String notes;
    private Timestamp createdAt;
    
    // Additional fields for display
    private String shiftName;
    private String userName;
    private String userEmail;
    private String startTime;
    private String endTime;

    // Default constructor
    public ShiftAssignment() {
    }

    // Constructor with all fields
    public ShiftAssignment(int assignmentID, int shiftID, int userID, Date assignedDate, 
                          String notes, Timestamp createdAt) {
        this.assignmentID = assignmentID;
        this.shiftID = shiftID;
        this.userID = userID;
        this.assignedDate = assignedDate;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert)
    public ShiftAssignment(int shiftID, int userID, Date assignedDate, String notes) {
        this.shiftID = shiftID;
        this.userID = userID;
        this.assignedDate = assignedDate;
        this.notes = notes;
    }

    // Getters and Setters
    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ShiftAssignment{" +
                "assignmentID=" + assignmentID +
                ", shiftID=" + shiftID +
                ", userID=" + userID +
                ", assignedDate=" + assignedDate +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                ", shiftName='" + shiftName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
