package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

public class CityVihicals {
    @SerializedName("_id")
    private String id;

    @SerializedName("country")
    private String country;

    @SerializedName("city")
    private String city;

    @SerializedName("vehicleType")
    private String vehicleType;

    @SerializedName("driverProfit")
    private int driverProfit;

    @SerializedName("minFare")
    private int minFare;

    @SerializedName("basePriceDistance")
    private int basePriceDistance;

    @SerializedName("basePrice")
    private int basePrice;

    @SerializedName("unitDistancePrice")
    private int unitDistancePrice;

    @SerializedName("unitTimePrice")
    private int unitTimePrice;

    @SerializedName("maxSpace")
    private int maxSpace;

    @SerializedName("__v")
    private int v;

    public CityVihicals(String id, String country, String city, String vehicleType, int driverProfit, int minFare, int basePriceDistance, int basePrice, int unitDistancePrice, int unitTimePrice, int maxSpace, int v) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.vehicleType = vehicleType;
        this.driverProfit = driverProfit;
        this.minFare = minFare;
        this.basePriceDistance = basePriceDistance;
        this.basePrice = basePrice;
        this.unitDistancePrice = unitDistancePrice;
        this.unitTimePrice = unitTimePrice;
        this.maxSpace = maxSpace;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getDriverProfit() {
        return driverProfit;
    }

    public void setDriverProfit(int driverProfit) {
        this.driverProfit = driverProfit;
    }

    public int getMinFare() {
        return minFare;
    }

    public void setMinFare(int minFare) {
        this.minFare = minFare;
    }

    public int getBasePriceDistance() {
        return basePriceDistance;
    }

    public void setBasePriceDistance(int basePriceDistance) {
        this.basePriceDistance = basePriceDistance;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getUnitDistancePrice() {
        return unitDistancePrice;
    }

    public void setUnitDistancePrice(int unitDistancePrice) {
        this.unitDistancePrice = unitDistancePrice;
    }

    public int getUnitTimePrice() {
        return unitTimePrice;
    }

    public void setUnitTimePrice(int unitTimePrice) {
        this.unitTimePrice = unitTimePrice;
    }

    public int getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(int maxSpace) {
        this.maxSpace = maxSpace;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }
}
