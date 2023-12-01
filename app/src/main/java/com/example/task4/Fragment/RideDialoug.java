package com.example.task4.Fragment;

import android.app.Dialog;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.task4.DataModels.RidesRespons;
import com.example.task4.R;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RideDialoug extends  DialogFragment {
    List<RidesRespons.Ride> rides;
    int position;
    ImageView profile;
    LinearLayout layout;
    TextView txt_name,txt_phone,txt_email,txt_pickup,txt_dropoff,txt_etime,txt_edist,txt_time,txt_date,txt_price,txt_servicetype;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.ride_dialoug,container,false);
        txt_name=view.findViewById(R.id.txt_user_name_d);
        txt_phone = view.findViewById(R.id.txt_user_phone_d);
        txt_email=view.findViewById(R.id.txt_user_email_d);
        txt_pickup = view.findViewById(R.id.txt_pickup_d);
        txt_dropoff = view.findViewById(R.id.txt_dropoff_d);
        txt_etime = view.findViewById(R.id.txt_etime_d);
        txt_edist = view.findViewById(R.id.txt_edistance_d);
        txt_time = view.findViewById(R.id.txt_time_d);
        txt_date=view.findViewById(R.id.txt_date_d);
        txt_price = view.findViewById(R.id.txt_price_d);
        txt_servicetype = view.findViewById(R.id.txt_service_d);
        profile =  view.findViewById(R.id.profile_pic_d);
        layout = view.findViewById(R.id.linearlayout_stops);

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
            String path = "http://192.168.0.215:3000/" + currentride.user.getProfile();
            Picasso.get()
                    .load(path)
                    .into(profile);
            rideStops();
        }
        return view;
    }

    private void rideStops() {

        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());

        List<String> addressstrings = new ArrayList<>();
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
          //  dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER);

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
    public void setcustomedata(List<RidesRespons.Ride> rides,int position)
    {
        this.rides=rides;
        this.position=position;
    }
}
