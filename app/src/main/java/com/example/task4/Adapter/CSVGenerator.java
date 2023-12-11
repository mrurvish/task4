package com.example.task4.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.task4.DataModels.RidesRespons;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CSVGenerator {
    private final ContentResolver contentResolver;

    public CSVGenerator(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }
    public void generateCSV(List<RidesRespons.Ride> dataList) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeToDownloadsQ(dataList);
        } else {
            writeToDownloadsLegacy(dataList);
        }
    }

    private void writeToDownloadsLegacy(List<RidesRespons.Ride> dataList) {
        String csvFileName = "data.csv";
        String csvFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + csvFileName;
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            // Write header
            writer.writeNext(new String[]{"Ride Id", "Name", "Email", "Phone", "Pick Up", "Destination", "Driver", "Service Type", "Ride Date", "Ride Time", "Journey Distance", "Journey Time", "Estimated Fare", "Status"});

            // Write data
            for (RidesRespons.Ride data : dataList) {
                writer.writeNext(new String[]{String.valueOf(data.rideId), data.userName, data.user.getEmail(), data.user.getPhone(), data.pickUp, data.dropOff, data.driver.getName(), data.serviceType.getVehicleType(), data.rideDate, data.rideTime, data.journeyDistance, data.journeyTime, data.totalFare, String.valueOf(data.status)});
            }

            Log.d("CSVGenerator", "CSV file generated successfully. Path: " + csvFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CSVGenerator", e.getCause().toString());
            Log.d("CSVGenerator", csvFilePath);
        }
    }



    private void writeToDownloadsQ(List<RidesRespons.Ride> dataList) {

            String csvFileName = "data.csv";

            // ContentResolver for interacting with the MediaStore
            ContentResolver resolver = contentResolver;

            // Set up the ContentValues to describe the file
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, csvFileName);
            contentValues.put(MediaStore.Downloads.MIME_TYPE, "text/csv");

            // Insert the file into the MediaStore.Downloads
        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }

        // Open an OutputStream to the file and write the CSV content
            try (OutputStream os = resolver.openOutputStream(uri)) {
                CSVWriter writer = new CSVWriter(new OutputStreamWriter(os));

                // Write header
                writer.writeNext(new String[]{"Ride Id", "Name", "Email", "Phone", "Pick Up", "Destination", "Driver", "Service Type", "Ride Date", "Ride Time", "Journey Distance", "Journey Time", "Estimated Fare", "Status"});
                String driver;
                // Write data
                for (RidesRespons.Ride data : dataList) {
                    if (data.driver != null) {
                         driver= data.driver.getName();
                    }else {
                        driver="N/A";
                    }
                    writer.writeNext(new String[]{String.valueOf(data.rideId), data.userName, data.user.getEmail(), data.user.getPhone(), data.pickUp, data.dropOff, driver, data.serviceType.getVehicleType(), data.rideDate, data.rideTime, data.journeyDistance, data.journeyTime, data.totalFare, String.valueOf(data.status)});
                }

                writer.close(); // Close the CSVWriter

                Log.d(TAG, "CSV file generated successfully. Uri: " + uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error generating CSV file: " + e.getMessage());
            }
        }






    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
