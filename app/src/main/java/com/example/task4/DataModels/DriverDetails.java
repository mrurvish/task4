package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

public class DriverDetails {
    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("profile")
    public String profile;

    @SerializedName("driverID")
    public int driverID;

    @SerializedName("email")
    public String email;

    @SerializedName("phoneCode")
    public String phoneCode;

    @SerializedName("phone")
    public String phone;

    @SerializedName("cityID")
    public String cityID;

    @SerializedName("isApproved")
    public boolean isApproved;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("updatedAt")
    public String updatedAt;

    @SerializedName("__v")
    public int v;

    @SerializedName("serviceTypeID")
    public String serviceTypeID;

    @SerializedName("status")
    public int status;
}
