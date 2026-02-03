package model;

import java.sql.Timestamp;

public class User {
    private int userID;
    private String fullName;
    private String email;
    private String passwordHash;
    private String gender;
    private String phone;
    private String address;
    private String avatarUrl;
    private int roleID;
    private boolean isActive;
    private Timestamp createdAt;

    // Default constructor
    public User() {
    }

    // Constructor with all fields
    public User(int userID, String fullName, String email, String passwordHash, 
               String phone, String address, String avatarUrl, int roleID, boolean isActive, Timestamp createdAt) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = null;
        this.phone = phone;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.roleID = roleID;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Constructor without ID and timestamp (for insert operations)
    public User(String fullName, String email, String passwordHash, 
               String phone, String address, String avatarUrl, int roleID, boolean isActive) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = null;
        this.phone = phone;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.roleID = roleID;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
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
        return "User{" +
                "userID=" + userID +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", roleID=" + roleID +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}