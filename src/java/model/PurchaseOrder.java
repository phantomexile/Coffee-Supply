package model;

import java.sql.Timestamp;

public class PurchaseOrder {
    private int poID;
    private int shopID;
    private int supplierID;
    private int createdBy;
    private int statusID;
    private String rejectReason;
    private Timestamp createdAt;
    
    // Additional fields for display names
    private String shopName;
    private String supplierName;
    private String createdByName;

    // Default constructor
    public PurchaseOrder() {
    }

    // Constructor with all fields
    public PurchaseOrder(int poID, int shopID, int supplierID, int createdBy, 
                        int statusID, String rejectReason, Timestamp createdAt) {
        this.poID = poID;
        this.shopID = shopID;
        this.supplierID = supplierID;
        this.createdBy = createdBy;
        this.statusID = statusID;
        this.rejectReason = rejectReason;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert operations)
    public PurchaseOrder(int shopID, int supplierID, int createdBy, int statusID) {
        this.shopID = shopID;
        this.supplierID = supplierID;
        this.createdBy = createdBy;
        this.statusID = statusID;
    }

    // Getters and Setters
    public int getPoID() {
        return poID;
    }

    public void setPoID(int poID) {
        this.poID = poID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
    
    // Getters and Setters for display names
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String toString() {
        return "PurchaseOrder{" +
                "poID=" + poID +
                ", shopID=" + shopID +
                ", supplierID=" + supplierID +
                ", createdBy=" + createdBy +
                ", statusID=" + statusID +
                ", rejectReason='" + rejectReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}