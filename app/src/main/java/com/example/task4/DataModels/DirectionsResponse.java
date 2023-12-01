package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DirectionsResponse implements Serializable {
    @SerializedName("status")
    public String status;

    @SerializedName("routes")
    public List<Route> routes;

    public static class Route implements Serializable {
        @SerializedName("legs")
        public List<Leg> legs;
    }

    public static class Leg implements Serializable{
        @SerializedName("distance")
        public Distance distance;

        @SerializedName("duration")
        public Duration duration;

        @SerializedName("steps")
        public List<Step> steps;
        @SerializedName("start_location")
        public StartLoc startloc;
        @SerializedName("start_address")
        public String start_address;
        @SerializedName("end_address")
        public String end_address;
        @SerializedName("end_location")
        public EndLoc endloc;
    }
public static class StartLoc implements Serializable{
        @SerializedName("lat")
        public Double lat;
       @SerializedName("lng")
        public Double lng;
}
    public static class EndLoc implements Serializable{
        @SerializedName("lat")
        public Double lat;
        @SerializedName("lng")
        public Double lng;
    }
    public static class Distance implements Serializable{
        @SerializedName("text")
        public String text;

        @SerializedName("value")
        public int value;
    }

    public static class Duration implements Serializable{
        @SerializedName("text")
        public String text;

        @SerializedName("value")
        public int value;
    }

    public static class Step implements Serializable{
        @SerializedName("html_instructions")
        public String htmlInstructions;

        @SerializedName("polyline")
        public Polyline polyline;
        @SerializedName("distance")
        public Distance distance;
    }

    public static class Polyline implements Serializable{
        @SerializedName("points")
        public String points;
    }
}


