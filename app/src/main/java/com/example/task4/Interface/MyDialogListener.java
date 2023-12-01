package com.example.task4.Interface;

import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.LocationZone;

public interface MyDialogListener {
    void onDataReceived(DirectionsResponse response, LocationZone city);


}
