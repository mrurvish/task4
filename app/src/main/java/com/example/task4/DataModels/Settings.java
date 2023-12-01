package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Settings implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("stops")
    private String stops;

    @SerializedName("__v")
    private int version;

    @SerializedName("driverTimeout")
    private String driverTimeout;

    @SerializedName("stripeKey")
    private String stripeKey;

    public Settings(String id, String stops, int version, String driverTimeout, String stripeKey) {
        this.id = id;
        this.stops = stops;
        this.version = version;
        this.driverTimeout = driverTimeout;
        this.stripeKey = stripeKey;
    }

    public Settings() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDriverTimeout() {
        return driverTimeout;
    }

    public void setDriverTimeout(String driverTimeout) {
        this.driverTimeout = driverTimeout;
    }

    public String getStripeKey() {
        return stripeKey;
    }

    public void setStripeKey(String stripeKey) {
        this.stripeKey = stripeKey;
    }
}
