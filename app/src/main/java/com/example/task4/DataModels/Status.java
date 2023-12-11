package com.example.task4.DataModels;

import static java.util.Map.of;

import java.util.HashMap;
import java.util.Map;

public class Status {
    public static final Map<String, Integer> services;
    public static final Map<Integer, String> reversedServices;

    static {
        services = new HashMap<>();
        services.put("Cancel", -1);
        services.put("Hold", 0);
        services.put("Pending", 1);
        services.put("Assigning", 2);
        services.put("Accepted", 3);
        services.put("Arrived", 4);
        services.put("Picked", 5);
        services.put("Started", 6);
        services.put("Completed", 7);

        reversedServices = new HashMap<>();
        for (Map.Entry<String, Integer> entry : services.entrySet()) {
            reversedServices.put(entry.getValue(), entry.getKey());
        }
    }
}
