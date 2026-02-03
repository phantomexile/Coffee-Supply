package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {
    private int productID;
    private String productName;
    private String description;
    private String imageUrl;
    private int categoryID;
    private BigDecimal price;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Additional fields for display purposes
    private String categoryName;
    // Aggregated recipe formula for UI (e.g., "18g cafe + 30ml sua")
    private String formula;

    // Default constructor
    public Product() {
    }

    // Constructor with all fields
    public Product(int productID, String productName, String description, String imageUrl, int categoryID, 
                  BigDecimal price, boolean isActive, Timestamp createdAt) {
        this.productID = productID;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categoryID = categoryID;
        this.price = price;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert operations)
    public Product(String productName, String description, String imageUrl, int categoryID, 
                  BigDecimal price, boolean isActive) {
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categoryID = categoryID;
        this.price = price;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return isActive;
    }
    
    // Additional getter for JSP EL compatibility
    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    
    public void setIsActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", categoryID=" + categoryID +
                ", price=" + price +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}