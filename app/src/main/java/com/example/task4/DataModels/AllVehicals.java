package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

public class AllVehicals {
    @SerializedName("_id")
    private String id;

    @SerializedName("vehicleType")
    private String vehicleType;

    @SerializedName("vehicleImage")
    private String vehicleImage;

    @SerializedName("__v")
    private int v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }
}
