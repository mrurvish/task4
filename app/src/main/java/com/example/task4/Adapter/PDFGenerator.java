package com.example.task4.Adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.task4.DataModels.RidesRespons;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PDFGenerator{
    private static final String TAG = "PDFGenerator";
    private final ContentResolver contentResolver;

    public PDFGenerator(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void generatePDF(List<RidesRespons.Ride> dataList, String pdfFileName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeToDownloadsQ(dataList, pdfFileName);
        } else {
            writeToDownloadsLegacy(dataList, pdfFileName);
        }
    }

    private void writeToDownloadsLegacy(List<RidesRespons.Ride> dataList, String pdfFileName) {
        String pdfFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + pdfFileName;
        try {
            PdfWriter writer = new PdfWriter(pdfFilePath);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4.rotate());

            // Create a table
            Table table = new Table(new float[]{2, 4, 4, 3, 3, 4}); // Adjust column widths as needed

            // Add header row
            table.addHeaderCell("Ride Id");
            table.addHeaderCell("Name");
            table.addHeaderCell("Email");
            table.addHeaderCell("Phone");
            table.addHeaderCell("Pick Up");
            table.addHeaderCell("Destination");
            table.addHeaderCell("Driver");
            table.addHeaderCell("Service Type");
            table.addHeaderCell("Ride Date");
            table.addHeaderCell("Ride Time");
            table.addHeaderCell("Journey Distance");
            table.addHeaderCell("Journey Time");
            table.addHeaderCell("Estimated Fare");
            table.addHeaderCell("Status");
            String driver;


            // Add data rows
            for (RidesRespons.Ride data : dataList) {
                if (data.driver != null) {
                    driver= data.driver.getName();
                }else {
                    driver="N/A";
                }
                table.addCell(String.valueOf(data.rideId));
                table.addCell(data.userName);
                table.addCell(data.user.getEmail());
                table.addCell(data.user.getPhone());
                table.addCell(data.pickUp);
                table.addCell(data.dropOff);
                table.addCell(driver);
                table.addCell(data.serviceType.getVehicleType());
                table.addCell(data.rideDate);
                table.addCell(data.rideTime);
                table.addCell(data.journeyDistance);
                table.addCell(data.journeyTime);
                table.addCell(data.totalFare);
                table.addCell(String.valueOf(data.status));
            }

            // Add the table to the document
            document.add(table);

            document.close();

            Log.d(TAG, "PDF file with table generated successfully. Path: " + pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error generating PDF file: " + e.getMessage());
        }
    }

    private void writeToDownloadsQ(List<RidesRespons.Ride> dataList, String pdfFileName) {
        ContentResolver resolver = contentResolver;

        // Set up the ContentValues to describe the file
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, pdfFileName);
        contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");

        // Insert the file into the MediaStore.Downloads
        Uri uri = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
        }

        // Open an OutputStream to the file and write the PDF content
        try (OutputStream os = resolver.openOutputStream(uri)) {
            PdfWriter writer = new PdfWriter(os);
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize customPageSize = new PageSize(1000,800);

            Document document = new Document(pdfDocument,customPageSize);

            // Create a table
            Table table = new Table(new float[]{2, 4, 4, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4}); // Adjust column widths as needed

            // Add header row
            table.addHeaderCell("Ride Id");
            table.addHeaderCell("Name");
            table.addHeaderCell("Email");
            table.addHeaderCell("Phone");
            table.addHeaderCell("Pick Up");
            table.addHeaderCell("Destination");
            table.addHeaderCell("Driver");
            table.addHeaderCell("Service Type");
            table.addHeaderCell("Ride Date");
            table.addHeaderCell("Ride Time");
            table.addHeaderCell("Journey Distance");
            table.addHeaderCell("Journey Time");
            table.addHeaderCell("Estimated Fare");
            table.addHeaderCell("Status");
            String driver;

            // Add data rows
            for (RidesRespons.Ride data : dataList) {
                if (data.driver != null) {
                    driver= data.driver.getName();
                }else {
                    driver="N/A";
                }
                table.addCell(String.valueOf(data.rideId));
                table.addCell(data.userName);
                table.addCell(data.user.getEmail());
                table.addCell(data.user.getPhone());
                table.addCell(data.pickUp);
                table.addCell(data.dropOff);
                table.addCell(driver);
                table.addCell(data.serviceType.getVehicleType());
                table.addCell(data.rideDate);
                table.addCell(data.rideTime);
                table.addCell(data.journeyDistance);
                table.addCell(data.journeyTime);
                table.addCell(data.totalFare);
                table.addCell(String.valueOf(data.status));
            }

            // Add the table to the document
            document.add(table);

            document.close();

            Log.d(TAG, "PDF file with table generated successfully. Uri: " + uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error generating PDF file: " + e.getMessage());
        }
    }
}
