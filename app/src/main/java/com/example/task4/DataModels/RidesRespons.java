package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RidesRespons {
    @SerializedName("rideCount")
    public int rideCount;

    @SerializedName("pageCount")
    public int pageCount;

    @SerializedName("rides")
    public List<Ride> rides;

    public static class Ride {
        @SerializedName("_id")
        public String id;

        @SerializedName("userID")
        public String userId;

        @SerializedName("cityID")
        public String cityId;

        @SerializedName("serviceTypeID")
        public String serviceTypeId;

        @SerializedName("rideID")
        public int rideId;

        @SerializedName("userName")
        public String userName;

        @SerializedName("pickUp")
        public String pickUp;

        @SerializedName("stops")
        public List<Stop> stops;

        @SerializedName("dropOff")
        public String dropOff;

        @SerializedName("journeyDistance")
        public String journeyDistance;

        @SerializedName("journeyTime")
        public String journeyTime;

        @SerializedName("totalFare")
        public String totalFare;

        @SerializedName("paymentType")
        public String paymentType;

        @SerializedName("rideDate")
        public String rideDate;

        @SerializedName("rideTime")
        public String rideTime;

        @SerializedName("status")
        public int status;

        @SerializedName("rejectedDriverID")
        public List<String> rejectedDriverId;

        @SerializedName("timeoutDriverID")
        public List<String> timeoutDriverId;

        @SerializedName("createdAt")
        public String createdAt;

        @SerializedName("updatedAt")
        public String updatedAt;

        @SerializedName("__v")
        public int v;

        @SerializedName("assignSelected")
        public boolean assignSelected;

        @SerializedName("driverID")
        public String driverId;

        @SerializedName("feedback")
        public String feedback;

        @SerializedName("user")
        public User user;

        @SerializedName("city")
        public LocationZone city;

        @SerializedName("serviceType")
        public CityVihicals serviceType;

        @SerializedName("driver")
        public DriverDetails driver;

        public static class Stop {
            @SerializedName("lat")
            public double lat;

            @SerializedName("lng")
            public double lng;
        }
    }
}
