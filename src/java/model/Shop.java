package model;

import java.sql.Timestamp;

public class Shop {
    private int shopID;
    private String shopName;
    private String address;
    private String phone;
    private Integer ownerID;
    private boolean isActive;
    private String shopImage;
    private Timestamp createdAt;

    // Default constructor
    public Shop() {
    }

    // Constructor with all fields
    public Shop(int shopID, String shopName, String address, String phone, Integer ownerID,
               boolean isActive, String shopImage, Timestamp createdAt) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.address = address;
        this.phone = phone;
        this.ownerID = ownerID;
        this.isActive = isActive;
        this.shopImage = shopImage;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert operations)
    public Shop(String shopName, String address, String phone, Integer ownerID, 
               boolean isActive, String shopImage) {
        this.shopName = shopName;
        this.address = address;
        this.phone = phone;
        this.ownerID = ownerID;
        this.isActive = isActive;
        this.shopImage = shopImage;
    }

    // Getters and Setters
    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopID=" + shopID +
                ", shopName='" + shopName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", ownerID=" + ownerID +
                ", isActive=" + isActive +
                ", shopImage='" + shopImage + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}