package model;

import java.math.BigDecimal;

public class PurchaseOrderDetail {
    private int poDetailID;
    private int poID;
    private int ingredientID;
    private BigDecimal quantity;
    private int unitID;
    private BigDecimal receivedQuantity;
    
    // Additional fields for display
    private String ingredientName;
    private String unitName;

    // Default constructor
    public PurchaseOrderDetail() {
    }

    // Constructor with all fields
    public PurchaseOrderDetail(int poDetailID, int poID, int ingredientID, 
                              BigDecimal quantity, int unitID, BigDecimal receivedQuantity) {
        this.poDetailID = poDetailID;
        this.poID = poID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unitID = unitID;
        this.receivedQuantity = receivedQuantity;
    }

    // Constructor without ID (for insert operations)
    public PurchaseOrderDetail(int poID, int ingredientID, BigDecimal quantity, int unitID, BigDecimal receivedQuantity) {
        this.poID = poID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unitID = unitID;
        this.receivedQuantity = receivedQuantity;
    }

    // Getters and Setters
    public int getPoDetailID() {
        return poDetailID;
    }

    public void setPoDetailID(int poDetailID) {
        this.poDetailID = poDetailID;
    }

    public int getPoID() {
        return poID;
    }

    public void setPoID(int poID) {
        this.poID = poID;
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

    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }
    
    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }
    
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String toString() {
        return "PurchaseOrderDetail{" +
                "poDetailID=" + poDetailID +
                ", poID=" + poID +
                ", ingredientID=" + ingredientID +
                ", quantity=" + quantity +
                ", unitID=" + unitID +
                ", receivedQuantity=" + receivedQuantity +
                '}';
    }
}