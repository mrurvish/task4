package com.example.task4.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.task4.DataModels.AllVehicals;
import com.example.task4.Interface.BottomsheetDialougListner;
import com.example.task4.Interface.MyDialogListener;
import com.example.task4.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomSheetFilters extends BottomSheetDialogFragment {
    List<AllVehicals> vehicals;
    Spinner vehicaltype,status;
    TextView from,to;
    String v_type,s_type,d_from,d_to;
    BottomsheetDialougListner listner;
    Map<String,Integer> services;
    int activity;

    @SuppressLint("SuspiciousIndentation")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filters,container,false);
        vehicaltype = view.findViewById(R.id.v_type);
        status = view.findViewById(R.id.s_type);
        from = view.findViewById(R.id.from);
        to = view.findViewById(R.id.to);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(0);
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(1);
            }
        });
        if (vehicals!=null)
        {
            String[] vehicallist = new String[vehicals.size()+1];
            for (int i=0;i<=vehicals.size();i++)
            {
                if (i==0)
                {
                    vehicallist[i] = "";
                }else {
                    vehicallist[i] = vehicals.get(i-1).getVehicleType();
                }
            }
            ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, vehicallist);
            arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vehicaltype.setAdapter(arrayadapter);
        }
      services = new HashMap<>();
        services.put("Cancel",-1);
        services.put("Hold",0);
        services.put("Panding",1);
        services.put("Assigning",2);
        services.put("Accepted",3);
        services.put("Arrived",4);
        services.put("Picked",5);
        services.put("Started",6);
        services.put("Completed",7);

        if (activity==0) {
            String[] servicelist = {"", "Cancel", "Hold", "Panding", "Assigning", "Accepted", "Arrived", "Picked", "Started", "Completed"};
            ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, servicelist);
            arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status.setAdapter(arrayadapter);
        }
        else {
            String[] servicelist = {"", "Cancel", "Completed"};
            ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, servicelist);
            arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            status.setAdapter(arrayadapter);
        }


        return view;
    }



    private void showDatePicker(int i) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String formattedMonth = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);
                        String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        String selectedDate = formattedDay + "/" + formattedMonth + "/" + year;
                        updateDateTextView(selectedDate,i);
                    }
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();

    }

    private void updateDateTextView(String selectedDate, int i) {
        if (i==0) {
            from.setText(selectedDate);
        }
        else {
            to.setText(selectedDate);
        }
    }

    public  void setVehicals(List<AllVehicals> vehicals,int i)
    {
        this.vehicals = vehicals;
        this.activity = i;
    }
    public void setMyDialogListener(BottomsheetDialougListner listener) {
        this.listner = listener;
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        d_from = from.getText().toString();
        d_to=to.getText().toString();
        v_type = vehicaltype.getSelectedItem().toString();
        s_type=status.getSelectedItem().toString();
        if (!s_type.isEmpty()){
            s_type=services.get(s_type).toString();
        }
        listner.onFilterRecived(v_type,s_type,d_from,d_to);


        super.onDismiss(dialog);

    }
}
