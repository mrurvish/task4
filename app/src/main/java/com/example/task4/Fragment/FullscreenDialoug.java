package com.example.task4.Fragment;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task4.Adapter.PlaceAutocompleteAdapterNew;
import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.LocationZone;
import com.example.task4.DataModels.Locationdata;
import com.example.task4.DataModels.Settings;
import com.example.task4.Interface.MyDialogListener;
import com.example.task4.Preference.CreatePref;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FullscreenDialoug extends DialogFragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final String TAG = "Dialog";
    private Toolbar toolbar;
    FloatingActionButton btn_add_stop;
    AutoCompleteTextView text_pickup,text_dropoff;
    SharedPreferencesManager manager;
    LinearLayout layout,pickuplayout;
    Settings settings;
    Button btn_findrouts;
    int stopcount=0;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<ImageView> imageViewListcancel = new ArrayList<>();
    private List<AutoCompleteTextView> autoCompleteTextViewList = new ArrayList<>();
    List<LinearLayout> layouts= new ArrayList<>();
    private PlacesClient placesClient;
    PlaceAutocompleteAdapterNew adapter;
    CreatePref pref;
    private MyDialogListener mListener;
    LocationZone cityzone;
    int index = 0;

    String[] waypoint_array;
    //map
    private GoogleMap googleMap;
    private MapView mapView;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_dialoug, container, false);
        inflater.inflate(R.layout.fragment_fullscreen_dialoug, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        btn_add_stop = view.findViewById(R.id.add_stop);
       toolbar.setNavigationIcon(R.drawable.ic_baseline_close_24);
       layout = view.findViewById(R.id.layout_stops);
       pickuplayout = view.findViewById(R.id.pick_up_layout);
       text_pickup = view.findViewById(R.id.pick_up);
       btn_findrouts = view.findViewById(R.id.btn_getrouts);
       text_dropoff = view.findViewById(R.id.drop_off);
         pref = new CreatePref(requireActivity(),"locations");
        btn_add_stop.setEnabled(false);
        text_dropoff.setEnabled(false);
        btn_findrouts.setEnabled(false);



       //initalizing place
        Places.initialize(requireActivity(), String.valueOf("AIzaSyBhiXFlGTlbp7A-EgMQFN4ke81_kefuOug"));
        placesClient = Places.createClient(requireActivity());
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

         adapter = new PlaceAutocompleteAdapterNew(requireActivity(), placesClient, token);
        text_pickup.setAdapter(adapter);
        text_dropoff.setAdapter(adapter);
     //initsearch();
        manager = new SharedPreferencesManager(requireActivity());
        String usertoken = manager.getToken();
        if (!usertoken.isEmpty())
        {
            showHome(usertoken);
        }
        text_pickup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                getsearch();
                hideKeyboard(FullscreenDialoug.this);
                if(!text_pickup.getText().toString().isEmpty()  && !text_dropoff.getText().toString().isEmpty())
                {
                    btn_findrouts.setEnabled(true);
                }
            }
        });
        text_dropoff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                hideKeyboard(FullscreenDialoug.this);
                if(!text_pickup.getText().toString().isEmpty()  && !text_dropoff.getText().toString().isEmpty())
                {
                    btn_findrouts.setEnabled(true);
                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_add_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addstopview();
            }
        });

        btn_findrouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0 ; i<autoCompleteTextViewList.size();i++){
                    if (autoCompleteTextViewList.get(i).getText().toString().isEmpty())
                    {
                        autoCompleteTextViewList.get(i).setVisibility(View.GONE);
                        imageViewList.get(i).setVisibility(View.GONE);
                        autoCompleteTextViewList.remove(i);
                    }
                }
                pref.setString("editbox",text_pickup.getText().toString());
                pref.setString("editbox1",text_dropoff.getText().toString());
                if(autoCompleteTextViewList != null)
                {
                    for (int i=0 ; i<autoCompleteTextViewList.size();i++) {
                        pref.setString("stopedittext"+i,autoCompleteTextViewList.get(i).getText().toString());
                    }
                    pref.setString("count",String.valueOf(autoCompleteTextViewList.size()));
                }
                if (!text_pickup.getText().toString().isEmpty()&&!text_dropoff.getText().toString().isEmpty())
                {
                    getdirections(text_pickup.getText().toString(),text_dropoff.getText().toString(),autoCompleteTextViewList);
                }
            }
        });


        return view;
    }

    private void addstopview() {
        if(stopcount < Integer.valueOf(settings.getStops())) {
            LinearLayout linearLayout = new LinearLayout(requireActivity());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //     linearLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black));

            ImageView image = new ImageView(requireActivity());
            image.setImageResource(R.drawable.stops);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            AutoCompleteTextView tv = new AutoCompleteTextView(requireActivity());
            tv.setHint("Stop");
            tv.setBackgroundResource(android.R.drawable.editbox_background_normal);
            tv.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            tv.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            tv.setSingleLine();
            tv.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );

            tv.setLayoutParams(tvParams);
            ImageView image1 = new ImageView(requireActivity());
            image1.setImageResource(R.drawable.baseline_remove_circle_24);
            LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT

            );
            imageParams1.gravity = Gravity.CENTER_VERTICAL;
             // left, top, right, bottom margins
            image1.setLayoutParams(imageParams1);
            image1.setId(stopcount);

            linearLayout.addView(image);
            linearLayout.addView(tv);
            linearLayout.addView(image1);

            layout.addView(linearLayout, index);
            index++;
            //  layout.addView(textstop);
            imageViewList.add(image);
            imageViewListcancel.add(image1);
            autoCompleteTextViewList.add(tv);
            layouts.add(linearLayout);
            image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    layout.removeView(layouts.get(id));
                    autoCompleteTextViewList.remove(id);
                    imageViewList.remove(id);
                    imageViewListcancel.remove(id);
                    layouts.remove(id);
                    for (int i=0;i<imageViewListcancel.size();i++)
                    {
                        imageViewListcancel.get(i).setId(i);
                    }
                    stopcount--;
                    index--;
                    if (stopcount < 3) {
                        btn_add_stop.setVisibility(View.VISIBLE);
                    }
                }
            });
            autoCompleteTextViewList.get(stopcount).setAdapter(adapter);
            autoCompleteTextViewList.get(stopcount).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // getsearch();
                    hideKeyboard(FullscreenDialoug.this);
                    //   pref.setString("stopedittext"+view.getId(),adapterView);
                }
            });


            //  layout.addView(tv, index + 2);
            stopcount++;
            if(stopcount == Integer.valueOf(settings.getStops())) {
                btn_add_stop.setVisibility(View.GONE);
            }
        }

    }

    private void relodeview() {
        text_dropoff.setText(pref.getString("editbox1"));

        text_pickup.setText(pref.getString("editbox"));
        getsearch();
        int count = Integer.valueOf(pref.getString("count"));
        for(int i=0;i<count;i++)
        {
            addstopview();
            autoCompleteTextViewList.get(i).setText(pref.getString("stopedittext"+i));
        }
        btn_findrouts.setEnabled(true);
    }

    private void initsearch() {

        text_pickup.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, @androidx.annotation.NonNull KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("map", "search is called");
//|| actionId == EditorInfo.IME_ACTION_DONE ||event.getAction()==KeyEvent.ACTION_DOWN|| event.getAction() == KeyEvent.KEYCODE_ENTER
                    getsearch();
                  hideKeyboard(FullscreenDialoug.this);
                }
                return false;
            }
        });

    }

    private void getsearch() {
        String location = text_pickup.getText().toString();
        //text_pickup.getText().toString();
       // if (markerName != null)
          //  markerName.remove();
        // below line is to create a list of address
        // where we will store the list of all address.
        List<Address> addressList = new ArrayList<>();

        // checking if the entered location is null or not.
        // if (location != null || location.equals("")) {
        // on below line we are creating and initializing a geo coder.
        String hello;
        Geocoder geocoder = new Geocoder(requireActivity());
        try {
            // on below line we are getting location from the
            // location name and adding that location to address list.
           addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.getCause();
        }
        // on below line we are getting the location
        // from our list a first position.
       Address address = addressList.get(0);

        // on below line we are creating a variable for our location
        // where we will add our locations latitude and longitude.
      //  destination = address.getLatitude() + "," + address.getLongitude();
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
       // Toast.makeText(requireActivity(), ""+address.getLatitude()+"  "+address.getLongitude(), Toast.LENGTH_SHORT).show();
        // on below line we are adding marker to that position.
        hideKeyboard(FullscreenDialoug.this);
        Locationdata locationdata = new Locationdata(String.valueOf(address.getLatitude()),String.valueOf(address.getLongitude()));
        String usertoken = manager.getToken();
        if (!usertoken.isEmpty())
        {
            checkzone(usertoken,locationdata);
        }
       // markerName = googlemap.addMarker(new MarkerOptions().position(latLng).title("Title"));

        // below line is to animate camera to that position.
       // googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

    }

    private void checkzone(String usertoken, Locationdata locationdata) {
        String modifiedtoken = "Bearer " + usertoken;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);

        Call<List<LocationZone>> call = path.checkZone(modifiedtoken,locationdata);
        call.enqueue(new Callback<List<LocationZone>>() {
            @Override
            public void onResponse(Call<List<LocationZone>> call, Response<List<LocationZone>> response) {
                if (!response.body().isEmpty()) {
                    Toast.makeText(requireActivity(), "you are in zone  "+response.body().get(0).getName(), Toast.LENGTH_SHORT).show();
                    btn_add_stop.setEnabled(true);
                    text_dropoff.setEnabled(true);
                    cityzone = response.body().get(0);
                }
                else { Toast.makeText(requireActivity(), "you are not in zone.....", Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onFailure(Call<List<LocationZone>> call, Throwable t) {
                Toast.makeText(requireActivity(), "you are not in zone.....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showHome(String token) {
     //   progressBar.setVisibility(View.VISIBLE);
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<List<Settings>> call = path.getSettings(modifiedtoken);
    call.enqueue(new Callback<List<Settings>>() {
        @Override
        public void onResponse(Call<List<Settings>> call, Response<List<Settings>> response) {
            settings = response.body().get(0);
            CreatePref prefSettings = new CreatePref(requireActivity(),"hello");
            prefSettings.setString("setting",settings);
            if(!pref.getString("editbox").isEmpty())
            {
                relodeview();
            }

            Toast.makeText(requireActivity(), "success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<List<Settings>> call, Throwable t) {
            Toast.makeText(requireActivity(), "Failure..... ", Toast.LENGTH_SHORT).show();
        }
    });

            }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(false);


        }
        return dialog;
    }
    public void getdirections(String origin, String destination, List<AutoCompleteTextView> waypointlist)
    {
        waypoint_array = new String[waypointlist.size()];
        String waypoints ="";
        for (int i=0;i<waypointlist.size();i++)
        {
            if (i==0) {
                waypoints += waypointlist.get(i).getText().toString() ;
            }else {
                waypoints+=  "|"+waypointlist.get(i).getText().toString();
            }
            waypoint_array[i] = waypointlist.get(i).getText().toString();
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
                    sendDataToActivity(response.body(),cityzone);
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(requireActivity(), "direction Failed", Toast.LENGTH_SHORT).show();
            }
        });
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
    public static void hideKeyboard(Fragment fragment) {
        Activity activity = fragment.requireActivity();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window token from it.
        View view = fragment.getView();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void setMyDialogListener(MyDialogListener listener) {
        this.mListener = listener;
    }
    private void sendDataToActivity(DirectionsResponse data,LocationZone city) {
        if (mListener != null) {
            mListener.onDataReceived(data,cityzone,waypoint_array);
            dismiss(); // Close the dialog after sending data
        }
    }


}