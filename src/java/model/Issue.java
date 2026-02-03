package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Issue {
    private int issueID;
    private int ingredientID;
    private String issueType;  // "ISSUE_REPORT" hoặc "RESTOCK_REQUEST"
    private String description;  // Mô tả vấn đề
    private BigDecimal quantity;
    private int statusID;
    private int createdBy;
    private Integer confirmedBy;
    private Timestamp createdAt;
    
    // Additional fields for display
    private String ingredientName;
    private String statusName;
    private String createdByName;
    private String confirmedByName;
    private String unitName;

    // Default constructor
    public Issue() {
    }

    // Constructor with all fields
    public Issue(int issueID, int ingredientID, String issueType, String description, BigDecimal quantity, int statusID, 
                int createdBy, Integer confirmedBy, Timestamp createdAt) {
        this.issueID = issueID;
        this.ingredientID = ingredientID;
        this.issueType = issueType;
        this.description = description;
        this.quantity = quantity;
        this.statusID = statusID;
        this.createdBy = createdBy;
        this.confirmedBy = confirmedBy;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert operations)
    public Issue(int ingredientID, String issueType, String description, BigDecimal quantity, int statusID, 
                int createdBy, Integer confirmedBy) {
        this.ingredientID = ingredientID;
        this.issueType = issueType;
        this.description = description;
        this.quantity = quantity;
        this.statusID = statusID;
        this.createdBy = createdBy;
        this.confirmedBy = confirmedBy;
    }

    // Getters and Setters
    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getConfirmedBy() {
        return confirmedBy;
    }

    public void setConfirmedBy(Integer confirmedBy) {
        this.confirmedBy = confirmedBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getConfirmedByName() {
        return confirmedByName;
    }

    public void setConfirmedByName(String confirmedByName) {
        this.confirmedByName = confirmedByName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "issueID=" + issueID +
                ", ingredientID=" + ingredientID +
                ", issueType='" + issueType + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", statusID=" + statusID +
                ", createdBy=" + createdBy +
                ", confirmedBy=" + confirmedBy +
                ", createdAt=" + createdAt +
                '}';
    }
}