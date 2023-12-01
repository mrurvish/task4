package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Locationdata implements Serializable {
    @SerializedName("lat")
    String lat;
    @SerializedName("lng")
    String lng;

    public Locationdata(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
