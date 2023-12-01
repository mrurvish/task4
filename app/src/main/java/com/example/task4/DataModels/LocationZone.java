package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LocationZone implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    @SerializedName("location")
    private String location;

    @SerializedName("coordinates")
    private Coordinates coordinates;

    @SerializedName("__v")
    private int v;

    public LocationZone(String id, String name, String country, String location, Coordinates coordinates, int v) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.location = location;
        this.coordinates = coordinates;
        this.v = v;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public static class Coordinates implements Serializable{

        @SerializedName("type")
        private String type;

        @SerializedName("coordinates")
        private List<List<List<Double>>> coordinatesList;

        public Coordinates(String type, List<List<List<Double>>> coordinatesList) {
            this.type = type;
            this.coordinatesList = coordinatesList;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<List<List<Double>>> getCoordinatesList() {
            return coordinatesList;
        }

        public void setCoordinatesList(List<List<List<Double>>> coordinatesList) {
            this.coordinatesList = coordinatesList;
        }
        // getters and setters
    }

}
