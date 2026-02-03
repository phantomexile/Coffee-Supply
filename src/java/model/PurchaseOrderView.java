package model;

import java.sql.Timestamp;

/**
 * Extended PurchaseOrder model with joined information
 */
public class PurchaseOrderView extends PurchaseOrder {
    private String shopName;
    private String supplierName;
    private String createdByName;
    private String statusName;
    private String rejectReason;
    
    // Default constructor
    public PurchaseOrderView() {
        super();
    }
    
    // Constructor with all fields
    public PurchaseOrderView(int poID, int shopID, int supplierID, int createdBy, 
                            int statusID, String rejectReason, Timestamp createdAt, 
                            String shopName, String supplierName, String createdByName, 
                            String statusName) {
        super(poID, shopID, supplierID, createdBy, statusID, rejectReason, createdAt);
        this.shopName = shopName;
        this.supplierName = supplierName;
        this.createdByName = createdByName;
        this.statusName = statusName;
    }

    // Getters and Setters
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

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    @Override
    public String toString() {
        return "PurchaseOrderView{" +
                "poID=" + getPoID() +
                ", shopID=" + getShopID() +
                ", shopName='" + shopName + '\'' +
                ", supplierID=" + getSupplierID() +
                ", supplierName='" + supplierName + '\'' +
                ", createdBy=" + getCreatedBy() +
                ", createdByName='" + createdByName + '\'' +
                ", statusID=" + getStatusID() +
                ", statusName='" + statusName + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
