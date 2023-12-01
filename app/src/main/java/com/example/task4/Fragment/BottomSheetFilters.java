package com.example.task4.Fragment;

import android.app.DatePickerDialog;
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
import java.util.Map;

public class BottomSheetFilters extends BottomSheetDialogFragment {
    List<AllVehicals> vehicals;
    Spinner vehicaltype,servicetype;
    TextView from,to;
    String v_type,s_type,d_from,d_to;
    BottomsheetDialougListner listner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filters,container,false);
        vehicaltype = view.findViewById(R.id.v_type);
        servicetype = view.findViewById(R.id.s_type);
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
            String[] vehicallist = new String[vehicals.size()];
            for (int i=0;i<=vehicals.size();i++)
            {
                if (i==0)
                {
                    vehicallist[i] = "";
                }
                vehicallist[i] = vehicals.get(i).getVehicleType();
            }
            ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, vehicallist);
            arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vehicaltype.setAdapter(arrayadapter);
        }
        Map<Integer,String> services = new HashMap<>();
        services.put(-1,"Cancel");
        services.put(0,"Hold");
        services.put(1,"Panding");
        services.put(2,"Assigning");
        services.put(3,"Accepted");
        services.put(4,"Arrived");
        services.put(5,"Picked");
        services.put(6,"Started");
        services.put(7,"Completed");

        String[] servicelist = {"","Cancel","Hold","Panding","Assigning","Accepted","Arrived","Picked","Started","Completed"};
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, servicelist);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servicetype.setAdapter(arrayadapter);

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
                        // Handle the selected date
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        // Update the TextView with the selected date

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

    public  void setVehicals(List<AllVehicals> vehicals)
    {
        this.vehicals = vehicals;
    }
    public void setMyDialogListener(BottomsheetDialougListner listener) {
        this.listner = listener;
    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        d_from = from.getText().toString();
        d_to=to.getText().toString();
        v_type = vehicaltype.getSelectedItem().toString();
        s_type=servicetype.getSelectedItem().toString();


        super.onDismiss(dialog);

    }
}
