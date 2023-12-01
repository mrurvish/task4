package com.example.task4.DataModels;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RideBody implements Serializable {

    @SerializedName("userID")
    private String userID;

    @SerializedName("cityID")
    private String cityID;

    @SerializedName("serviceTypeID")
    private String serviceTypeID;

    @SerializedName("userName")
    private String userName;

    @SerializedName("pickUp")
    private String pickUp;

    @SerializedName("stops")
    private  String jsonString;



    @SerializedName("dropOff")
    private String dropOff;

    @SerializedName("journeyDistance")
    private String journeyDistance;

    @SerializedName("journeyTime")
    private String journeyTime;

    @SerializedName("totalFare")
    private String totalFare;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("rideDate")
    private String rideDate;

    @SerializedName("rideTime")
    private String rideTime;
public RideBody(){}
    public RideBody(String userID, String cityID, String serviceTypeID, String userName, String pickUp, String stops, String dropOff, String journeyDistance, String journeyTime, String totalFare, String paymentType, String rideDate, String rideTime) {
        this.userID = userID;
        this.cityID = cityID;
        this.serviceTypeID = serviceTypeID;
        this.userName = userName;
        this.pickUp = pickUp;
        this.jsonString = stops;
       // this.stops = stops;
        this.dropOff = dropOff;
        this.journeyDistance = journeyDistance;
        this.journeyTime = journeyTime;
        this.totalFare = totalFare;
        this.paymentType = paymentType;
        this.rideDate = rideDate;
        this.rideTime = rideTime;


        // Convert the object to a JSON string

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(String serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getStops() {
        return jsonString;
    }

    public void setStops(String stops) {
        this.jsonString = stops;
    }

    public String getDropOff() {
        return dropOff;
    }

    public void setDropOff(String dropOff) {
        this.dropOff = dropOff;
    }

    public String getJourneyDistance() {
        return journeyDistance;
    }

    public void setJourneyDistance(String journeyDistance) {
        this.journeyDistance = journeyDistance;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

     public static class Stops implements Serializable {
        @SerializedName("lat")
        public Double lat;
        @SerializedName("lng")
        public Double lng;

        public Stops(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }


    public class Response {
        @SerializedName("msg")
        public String message;
        @SerializedName("error")
        public String error;
    }
}
