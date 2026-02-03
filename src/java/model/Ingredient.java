package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ingredient {
    private int ingredientID;
    private String name;
    private String description;  // Mô tả nguyên liệu (tối đa 255 ký tự)
    private int unitID;
    private BigDecimal stockQuantity;
    private int supplierID;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Additional fields for display
    private String unitName;
    private String supplierName;

    // Default constructor
    public Ingredient() {
    }

    // Constructor with all fields
    public Ingredient(int ingredientID, String name, int unitID, BigDecimal stockQuantity, 
                     int supplierID, boolean isActive, Timestamp createdAt) {
        this.ingredientID = ingredientID;
        this.name = name;
        this.unitID = unitID;
        this.stockQuantity = stockQuantity;
        this.supplierID = supplierID;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert operations)
    public Ingredient(String name, int unitID, BigDecimal stockQuantity, 
                     int supplierID, boolean isActive) {
        this.name = name;
        this.unitID = unitID;
        this.stockQuantity = stockQuantity;
        this.supplierID = supplierID;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(int ingredientID) {
        this.ingredientID = ingredientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String toString() {
        return "Ingredient{" +
                "ingredientID=" + ingredientID +
                ", name='" + name + '\'' +
                ", unitID=" + unitID +
                ", stockQuantity=" + stockQuantity +
                ", supplierID=" + supplierID +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}