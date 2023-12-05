package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

public class DriverDetails {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile")
    private String profile;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneCode")
    private String phoneCode;

    @SerializedName("phone")
    private String phone;

    @SerializedName("city")
    private String city;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("__v")
    private int version;

    @SerializedName("isApproved")
    private boolean isApproved;

    @SerializedName("driverID")
    private int driverID;

    @SerializedName("cityID")
    private String cityID;

    @SerializedName("serviceTypeID")
    private String serviceTypeID;

    @SerializedName("status")
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }



    public String getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public static class Combined{
        @SerializedName("ride")
        private RidesRespons.Ride ride;
        @SerializedName("driver")
        private DriverDetails driver;

        public Combined(RidesRespons.Ride ride, DriverDetails driver) {
            this.ride = ride;
            this.driver = driver;
        }
    }
}
