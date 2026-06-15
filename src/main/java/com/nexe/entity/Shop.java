package com.nexe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long shopId;

    // ===== Basic Details =====
    private String ownerName;
    private String mobileNumber;
    private String businessName;
    private String businessType;
    private String businessAddress;
    private String gstNumber;

    // ===== Business Timing =====
    private String openingTime;
    private String closingTime;

    // ===== Payment =====
    private String upiId;

    // ===== Images =====
    private String ownerPhotoPath;
    private String shopPhotoPath;

    // ===== Status =====
    private boolean active = true;

    // ===== Timestamps =====
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== RELATION: MENU =====
    @OneToMany(
            mappedBy = "shop",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<MenuItem> menuItems = new ArrayList<>();

    // ===== Lifecycle =====

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Helper Methods =====

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        menuItem.setShop(this);
    }

    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
        menuItem.setShop(null);
    }

    // ===== Getters & Setters =====

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getOwnerPhotoPath() {
        return ownerPhotoPath;
    }

    public void setOwnerPhotoPath(String ownerPhotoPath) {
        this.ownerPhotoPath = ownerPhotoPath;
    }

    public String getShopPhotoPath() {
        return shopPhotoPath;
    }

    public void setShopPhotoPath(String shopPhotoPath) {
        this.shopPhotoPath = shopPhotoPath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;

        if (menuItems != null) {
            menuItems.forEach(item -> item.setShop(this));
        }
    }
}