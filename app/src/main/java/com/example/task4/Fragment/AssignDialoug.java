package com.example.task4.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.task4.DataModels.Country;
import com.example.task4.DataModels.DriverDetails;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.Interface.AssignDialougListner;
import com.example.task4.Interface.BottomsheetDialougListner;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignDialoug extends DialogFragment {
    RidesRespons.Ride ride;
    SharedPreferencesManager manager;
    List<DriverDetails> drivers;
    DriverDetails driver;
    LinearLayout layout;
    Button assign_selected, assign_nearest;
    RadioGroup radioGroup;
    private AssignDialougListner listner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assign_dialoug, container, false);
        layout = view.findViewById(R.id.layout_drivers);
        assign_selected = view.findViewById(R.id.assign_selected);
        assign_nearest = view.findViewById(R.id.assign_nearest);


        manager = new SharedPreferencesManager(requireActivity());
        String token = manager.getToken();
        if (!token.isEmpty()) {
            getAvaliableDrivers(token, ride.serviceTypeId, ride.cityId);
        }
        assign_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup != null) {

                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(requireActivity(), "Pleace Select Driver..!!", Toast.LENGTH_SHORT).show();
                    } else {

                        int id= radioGroup.getCheckedRadioButtonId();
                        listner.onDriverreceived(ride,drivers.get(id));
                        dismiss();
                    }
                }
            }
        });
        assign_nearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.onDriverreceived(ride,null);
                dismiss();
            }
        });
        return view;
    }

    public void setAssignListner(AssignDialougListner listener) {
        this.listner = listener;
    }

    private void getAvaliableDrivers(String token, String serviceTypeId, String cityId) {
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<List<DriverDetails>> call = path.getAllDrivers(modifiedtoken, serviceTypeId, cityId);
        call.enqueue(new Callback<List<DriverDetails>>() {
            @Override
            public void onResponse(Call<List<DriverDetails>> call, Response<List<DriverDetails>> response) {
                if (response.isSuccessful()) {
                    drivers = response.body();
                    showDrivers(drivers);

                }
            }

            @Override
            public void onFailure(Call<List<DriverDetails>> call, Throwable t) {

            }
        });
    }

    private void showDrivers(List<DriverDetails> drivers) {
        // Create a RadioGroup
        radioGroup = new RadioGroup(requireActivity());
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < drivers.size(); i++) {
            RadioButton radioButton = new RadioButton(requireActivity());
            radioButton.setText(drivers.get(i).getName());
            radioButton.setId(i);

            radioGroup.addView(radioButton);
        }
        layout.addView(radioGroup);
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
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    public void setcustomedata(List<RidesRespons.Ride> filteredlist, int position) {
        this.ride = filteredlist.get(position);
    }
}
