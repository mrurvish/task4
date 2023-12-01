package com.example.task4.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.LocationZone;
import com.example.task4.DataModels.User;
import com.example.task4.Fragment.FullscreenDialoug;
import com.example.task4.Interface.MyDialogListener;
import com.example.task4.Preference.CreatePref;
import com.example.task4.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateRidesMap extends AppCompatActivity implements OnMapReadyCallback, MyDialogListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final float Default_zoom = 150;
    double latitude, longitude;
    View mapview;
    private GoogleMap googlemap;
    private FusedLocationProviderClient locationprovider;
    MaterialCardView cardview ;
    Polyline polyline;
    Marker marker;
    Button btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rides_map);
        btn_next=findViewById(R.id.btn_next);
        cardview = findViewById(R.id.chose_location);

        locationprovider = LocationServices.getFusedLocationProviderClient(this);
        initmap();
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationInput();
            }
        });
        Log.d("check","on create");

    }

    private void getLocationInput() {
        FullscreenDialoug fullScreenDialogFragment = new FullscreenDialoug();
        fullScreenDialogFragment.setMyDialogListener(this);
        fullScreenDialogFragment.show(getSupportFragmentManager(), FullscreenDialoug.class.getSimpleName());
    }

    private void initmap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mapview = mapFragment.getView();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googlemap = googleMap;
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        if (checkpermissions()) {
            getdevicelocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            View locationButton = ((View) mapview.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, 30);
        }


    }

    @SuppressLint("MissingPermission")
    public void getdevicelocation() {
        locationprovider = LocationServices.getFusedLocationProviderClient(this);
        locationprovider.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location currentloc = (Location) task.getResult();
                    if (currentloc!=null) {
                        movecamera(new LatLng(currentloc.getLatitude(), currentloc.getLongitude()), Default_zoom);
                    }
                    //  origin = currentloc.getLatitude() + "," + currentloc.getLongitude();
                } else {
                    Toast.makeText(CreateRidesMap.this, "unable to found location", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///  if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        //   startLocationUpdates();
        //  }
    }

    public void movecamera(LatLng latlng, float zoom) {
        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    public boolean checkpermissions() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(CreateRidesMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(CreateRidesMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateRidesMap.this, permission, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;

        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to get the current location.
                //permission_result = true;
                Toast.makeText(this, "permission is granted", Toast.LENGTH_SHORT).show();
                checkpermissions();

            } else {
                // Permission denied, show a message to the user.
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("check","on start");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("check","on resume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("check","on pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("check","on stop");
        CreatePref pref = new CreatePref(CreateRidesMap.this,"locations");
        pref.clearpref();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("check","on destroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("check","on restarte");
    }

    @Override
    public void onDataReceived(DirectionsResponse response, LocationZone city) {
        if (polyline!=null) {
            polyline.remove();
        }
        if (marker!=null) {
            marker.remove();
        }
        googlemap.clear();
        List<List<DirectionsResponse.Step>> steplist = new ArrayList<>();
        List<LatLng> decodedPath;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);
        polylineOptions.geodesic(true);// Set color to blue
        polylineOptions.width(20);
        for (int i =0;i<response.routes.get(0).legs.size();i++)
        {
            steplist.add(response.routes.get(0).legs.get(i).steps);

        }
        for (List<DirectionsResponse.Step> steps : steplist)
        {

            //  List<DirectionsResponse.Step> steps = response.routes.get(0).legs.get(0).steps;
            for (DirectionsResponse.Step step : steps) {
                // Decode the polyline points
                decodedPath = PolyUtil.decode(step.polyline.points);

                // coveredPathIndex = findClosestPointIndex(decodedPath, lastCoveredPoint);
                // Add the points to the PolylineOptions
                polylineOptions.addAll(decodedPath);

            }

        }

        polyline =  googlemap.addPolyline(polylineOptions);
        //  DirectionsResponse.Leg steps1 = response.routes.get(0).legs.get(0);





        List<LatLng> latlng=new ArrayList<>() ;
        List<MarkerOptions> markeroptionslist = new ArrayList<>();

        for (int i = 0;i<response.routes.get(0).legs.size();i++)
        {
            latlng.add(new LatLng(response.routes.get(0).legs.get(i).startloc.lat,response.routes.get(0).legs.get(i).startloc.lng));

            markeroptionslist.add(new MarkerOptions().position(latlng.get(i)).title(response.routes.get(0).legs.get(i).start_address).title(i+" : "+response.routes.get(0).legs.get(i).start_address));
            String start = response.routes.get(0).legs.get(0).start_address;
            String end = response.routes.get(0).legs.get(i).end_address;
            if (i==response.routes.get(0).legs.size()-1) {
                if (!start.equals(response.routes.get(0).legs.get(i).end_address)) {
                    latlng.add(new LatLng(response.routes.get(0).legs.get(i).endloc.lat, response.routes.get(0).legs.get(i).endloc.lng));

                    markeroptionslist.add(new MarkerOptions().position(latlng.get(i + 1)).title(response.routes.get(0).legs.get(i).end_address).title(i + 1 + " : " + response.routes.get(0).legs.get(i).end_address));
                    marker= googlemap.addMarker(markeroptionslist.get(i+1));
                }
            }
            marker= googlemap.addMarker(markeroptionslist.get(i));
        }
        btn_next.setEnabled(true);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateRidesMap.this,BookRide.class);
                intent.putExtra("bookride", response);
                Intent dataintent = getIntent();
                User user = (User) dataintent.getSerializableExtra("user");
                Log.d("intent",user.getEmail());
                intent.putExtra("user",user);
                intent.putExtra("city",city);
                startActivity(intent);

            }
        });
        // MarkerOptions markerOptions =new MarkerOptions()
        //   .position(latLng)


    }

}