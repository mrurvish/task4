package com.example.task4.Interface;

import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.DriverDetails;
import com.example.task4.DataModels.LocationZone;
import com.example.task4.DataModels.RidesRespons;

public interface AssignDialougListner {
    void onDriverreceived(RidesRespons.Ride ride, DriverDetails Driver);
}
