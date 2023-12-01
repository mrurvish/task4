package com.example.task4.DataModels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu implements Serializable {
   public List<String> title;
    public  HashMap<String,List<String>> items;
    public   Menu() {
        title = new ArrayList<String>();
        items = new HashMap<String, List<String>>();
        title.add("Rides");
        title.add("Users");
        title.add("Drivers");
        title.add("Pricing");
        title.add("Settings");

        List<String> rides = new ArrayList<String>();
        rides.add("Create Ride");
        rides.add("Confirmed Rides");
        rides.add("Ride History");
        items.put(title.get(0), rides);

        List<String> drivers = new ArrayList<String>();
        drivers.add("List");
        drivers.add("Running request");
        items.put(title.get(2), drivers);

        List<String> pricing = new ArrayList<String>();
        pricing.add("Country");
        pricing.add("City");
        pricing.add("Vehicle type");
        pricing.add("Vehicle Pricing");
        items.put(title.get(3), pricing);
    }

}
