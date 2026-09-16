package com.nearnest.dto;

import org.springframework.web.multipart.MultipartFile;

public class BusinessRequest {

    private Long userId;

    private String businessName;

    private Long categoryId;

    private String contactNumber;

    private String location;

    private Double latitude;

    private Double longitude;

    private MultipartFile logo;

    private MultipartFile identityDocument;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public MultipartFile getLogo() {
        return logo;
    }

    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }


    public MultipartFile getIdentityDocument() {
        return identityDocument;
    }

    public void setIdentityDocument(MultipartFile identityDocument) {
        this.identityDocument = identityDocument;
    }
}