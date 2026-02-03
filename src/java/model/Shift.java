package model;

import java.sql.Time;
import java.sql.Timestamp;

public class Shift {
    private int shiftID;
    private String shiftName;
    private Time startTime;
    private Time endTime;
    private String description;
    private Integer managerID;
    private String managerName; // For display purposes
    private boolean isActive;
    private Timestamp createdAt;

    // Default constructor
    public Shift() {
    }

    // Constructor with all fields
    public Shift(int shiftID, String shiftName, Time startTime, Time endTime, 
                 String description, Integer managerID, boolean isActive, Timestamp createdAt) {
        this.shiftID = shiftID;
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.managerID = managerID;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert)
    public Shift(String shiftName, Time startTime, Time endTime, 
                 String description, Integer managerID, boolean isActive) {
        this.shiftName = shiftName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.managerID = managerID;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getManagerID() {
        return managerID;
    }

    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "shiftID=" + shiftID +
                ", shiftName='" + shiftName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                ", managerID=" + managerID +
                ", managerName='" + managerName + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}
