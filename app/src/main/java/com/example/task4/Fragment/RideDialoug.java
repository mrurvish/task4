package com.example.task4.Fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RideDialoug extends  DialogFragment implements OnMapReadyCallback {
    List<RidesRespons.Ride> rides;
    int position;
    ImageView profile,fullscreen;
    LinearLayout layout;
    SharedPreferencesManager manager;
    String token;
DirectionsResponse directionsResponse;
    TextView txt_name,txt_phone,txt_email,txt_pickup,txt_dropoff,txt_etime,txt_edist,txt_time,txt_date,txt_price,txt_servicetype;
    List<String> addressstrings;
  Socket socket;
    Polyline polyline;
    Marker marker;
    private GoogleMap googleMap;
    private View mapview;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.ride_dialoug,container,false);


        txt_name=view.findViewById(R.id.txt_user_name_d);
        txt_phone = view.findViewById(R.id.txt_user_phone_d);
        txt_email=view.findViewById(R.id.txt_user_email_d);
        txt_pickup = view.findViewById(R.id.txt_pickup_rv);
        txt_dropoff = view.findViewById(R.id.txt_dropoff_rv);
        txt_etime = view.findViewById(R.id.txt_etime_d);
        txt_edist = view.findViewById(R.id.txt_edistance_d);
        txt_time = view.findViewById(R.id.txt_time_d);
        txt_date=view.findViewById(R.id.txt_date_d);
        txt_price = view.findViewById(R.id.txt_price_d);
        txt_servicetype = view.findViewById(R.id.txt_service_d);
        profile =  view.findViewById(R.id.profile_pic_d);
        layout = view.findViewById(R.id.linearlayout_stops);
        fullscreen =view.findViewById(R.id.fullscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapDialoug mapDialoug = new MapDialoug();
                mapDialoug.setdirection(directionsResponse);
                mapDialoug.show( requireActivity().getSupportFragmentManager(), FullscreenDialoug.class.getSimpleName());
            }
        });


        manager = new SharedPreferencesManager(requireActivity());
        if(rides!=null)
        {

            RidesRespons.Ride currentride = rides.get(position);
            txt_name.setText(currentride.user.getName());
            txt_phone.setText(currentride.user.getPhoneCode().toString()+" "+rides.get(position).user.getPhone().toString());
            txt_email.setText(currentride.user.getEmail());
            txt_pickup.setText(currentride.pickUp);
            txt_dropoff.setText(currentride.dropOff);
            txt_etime.setText(currentride.journeyTime+" "+"Minutes");
            txt_edist.setText(currentride.journeyDistance+" "+"Km");
            txt_time.setText(currentride.rideTime);
            txt_date.setText(currentride.rideDate);
            txt_price.setText("₹"+" "+currentride.totalFare);
            txt_servicetype.setText(currentride.serviceType.getVehicleType());

            rideStops();

        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RidesRespons.Ride currentride = rides.get(position);
        String path = "http://192.168.0.215:3000/" + currentride.user.getProfile();
        Picasso.get()
                .load(path)
                .into(profile);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        mapview = mapFragment.getView();
        getdirections(currentride.pickUp,currentride.dropOff);
    }



    private void rideStops() {

        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());

        addressstrings= new ArrayList<>();
                for(int i = 0;i<rides.get(position).stops.size();i++){
                    try {
                        List<Address> addresses = geocoder.getFromLocation(rides.get(position).stops.get(i).lat, rides.get(position).stops.get(i).lng, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            addressstrings.add(address.getAddressLine(0)); // Get the first line of the address
                        }
                    } catch (IOException e) {
                        Log.e("AddressUtils", "Error obtaining address", e);
                    }
                }
                for (int i=0;i<addressstrings.size();i++)
                {
                    LinearLayout linearLayout = new LinearLayout(requireActivity());
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    //     linearLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black));
                    layout.addView(linearLayout, i);
                    ImageView image = new ImageView(requireActivity());
                    image.setImageResource(R.drawable.stops);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    imageParams.gravity = Gravity.CENTER_VERTICAL;
                    imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
                    image.setLayoutParams(imageParams);
                    TextView tv = new TextView(requireActivity());

                    tv.setText(addressstrings.get(i));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    tv.setLayoutParams(tvParams);

                /*

                    tv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);



                    */
                    linearLayout.addView(image,0);
                    linearLayout.addView(tv,1);
                }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            dialog.getWindow().getAttributes().windowAnimations = R.style.Ride;


            dialog.getWindow().setGravity(Gravity.CENTER);


            dialog.setCanceledOnTouchOutside(true);

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
    public void setcustomedata(List<RidesRespons.Ride> rides,int position)
    {
        this.rides=rides;
        this.position=position;
    }
    public void getdirections(String origin, String destination)
    {

        String waypoints ="";
        for (int i=0;i<addressstrings.size();i++)
        {
            if (i==0) {
                waypoints +=addressstrings.get(i) ;
            }else {
                waypoints+=  "|"+ addressstrings.get(i);
            }

        }

        Toast.makeText(requireActivity(), "getting routes", Toast.LENGTH_SHORT).show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiPath service = retrofit.create(ApiPath.class);
        Call<DirectionsResponse> call = service.getDirections(origin, destination,waypoints ,"AIzaSyBhiXFlGTlbp7A-EgMQFN4ke81_kefuOug");
        call.enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (!response.body().status.equals("ZERO_RESULTS"))
                {
                    directionsResponse=response.body();
                    drawpoliline();
                }
                else {
                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "direction Failed", Toast.LENGTH_SHORT).show();
            }
        });
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
        for (int i = 0; i < directionsResponse.routes.get(0).legs.size(); i++) {
            steplist.add(directionsResponse.routes.get(0).legs.get(i).steps);

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

        for (int i = 0; i < directionsResponse.routes.get(0).legs.size(); i++) {
            latlng.add(new LatLng(directionsResponse.routes.get(0).legs.get(i).startloc.lat, directionsResponse.routes.get(0).legs.get(i).startloc.lng));

            markeroptionslist.add(new MarkerOptions().position(latlng.get(i)).title(directionsResponse.routes.get(0).legs.get(i).start_address).title(i + " : " + directionsResponse.routes.get(0).legs.get(i).start_address));
            String start = directionsResponse.routes.get(0).legs.get(0).start_address;
            String end = directionsResponse.routes.get(0).legs.get(i).end_address;
            if (i == directionsResponse.routes.get(0).legs.size() - 1) {
                if (!start.equals(directionsResponse.routes.get(0).legs.get(i).end_address)) {
                    latlng.add(new LatLng(directionsResponse.routes.get(0).legs.get(i).endloc.lat, directionsResponse.routes.get(0).legs.get(i).endloc.lng));

                    markeroptionslist.add(new MarkerOptions().position(latlng.get(i + 1)).title(directionsResponse.routes.get(0).legs.get(i).end_address).title(i + 1 + " : " + directionsResponse.routes.get(0).legs.get(i).end_address));
                    marker = googleMap.addMarker(markeroptionslist.get(i + 1));
                }
            }
            marker = googleMap.addMarker(markeroptionslist.get(i));
        }
        movecamera(new LatLng(directionsResponse.routes.get(0).legs.get(0).startloc.lat,directionsResponse.routes.get(0).legs.get(0).startloc.lng),5);
    }
    @Override
    public void onMapReady(@androidx.annotation.NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
    public void movecamera(LatLng latlng, float zoom) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }
}
