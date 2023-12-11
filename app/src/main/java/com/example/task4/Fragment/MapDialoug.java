package com.example.task4.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.Network.ApiPath;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;


public class MapDialoug extends DialogFragment implements OnMapReadyCallback {
    Polyline polyline;
    Marker marker;
    private GoogleMap googleMap;
    private View mapview;
    DirectionsResponse response;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_dialoug,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);
        mapview = mapFragment.getView();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            //  dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


            dialog.getWindow().setGravity(Gravity.TOP);


            dialog.setCanceledOnTouchOutside(false);

        }
        return dialog;
    }
    @Override
    public void onResume() {
        super.onResume();

        // Set the dialog size to match the screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        drawpoliline();

    }
    public void setdirection(DirectionsResponse response)
    {
        this.response= response;
    }
    public void drawpoliline()
    {
        if (polyline != null) {
            polyline.remove();
        }
        if (marker != null) {
            marker.remove();
        }
        googleMap.clear();
        List<List<DirectionsResponse.Step>> steplist = new ArrayList<>();
        List<LatLng> decodedPath;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.geodesic(true);// Set color to blue
        polylineOptions.width(20);
        for (int i = 0; i < response.routes.get(0).legs.size(); i++) {
            steplist.add(response.routes.get(0).legs.get(i).steps);

        }
        for (List<DirectionsResponse.Step> steps : steplist) {

            //  List<DirectionsResponse.Step> steps = response.routes.get(0).legs.get(0).steps;
            for (DirectionsResponse.Step step : steps) {
                // Decode the polyline points
                decodedPath = PolyUtil.decode(step.polyline.points);

                // coveredPathIndex = findClosestPointIndex(decodedPath, lastCoveredPoint);
                // Add the points to the PolylineOptions
                polylineOptions.addAll(decodedPath);

            }

        }

        polyline = googleMap.addPolyline(polylineOptions);
        //  DirectionsResponse.Leg steps1 = response.routes.get(0).legs.get(0);


        List<LatLng> latlng = new ArrayList<>();
        List<MarkerOptions> markeroptionslist = new ArrayList<>();

        for (int i = 0; i < response.routes.get(0).legs.size(); i++) {
            latlng.add(new LatLng(response.routes.get(0).legs.get(i).startloc.lat, response.routes.get(0).legs.get(i).startloc.lng));

            markeroptionslist.add(new MarkerOptions().position(latlng.get(i)).title(response.routes.get(0).legs.get(i).start_address).title(i + " : " + response.routes.get(0).legs.get(i).start_address));
            String start = response.routes.get(0).legs.get(0).start_address;
            String end = response.routes.get(0).legs.get(i).end_address;
            if (i == response.routes.get(0).legs.size() - 1) {
                if (!start.equals(response.routes.get(0).legs.get(i).end_address)) {
                    latlng.add(new LatLng(response.routes.get(0).legs.get(i).endloc.lat, response.routes.get(0).legs.get(i).endloc.lng));

                    markeroptionslist.add(new MarkerOptions().position(latlng.get(i + 1)).title(response.routes.get(0).legs.get(i).end_address).title(i + 1 + " : " + response.routes.get(0).legs.get(i).end_address));
                    marker = googleMap.addMarker(markeroptionslist.get(i + 1));
                }
            }
            marker = googleMap.addMarker(markeroptionslist.get(i));
        }
        movecamera(new LatLng(response.routes.get(0).legs.get(0).startloc.lat,response.routes.get(0).legs.get(0).startloc.lng),5);
    }
    public void movecamera(LatLng latlng, float zoom) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }
}
