package com.example.task4.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.task4.DataModels.CityVihicals;
import com.example.task4.DataModels.DirectionsResponse;
import com.example.task4.DataModels.LocationZone;
import com.example.task4.DataModels.RideBody;
import com.example.task4.DataModels.User;
import com.example.task4.DataModels.UserCards;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookRide extends AppCompatActivity {
    Intent intent;
    LocationZone city;
    User user;
    DirectionsResponse response;
    Spinner spinner;
    TextView tv_dist,tv_time,tv_price,tv_date_chose,tv_time_chose;
    int hours,minutes,km;
    float fulldist;
    int fulltime;
    String[] types;
    List<CityVihicals> vehicallist;
    CheckBox check_shedule;
    RadioGroup payment_option;
    LinearLayout linearLayout;
    RadioButton cash,card;
    int fare;
    SharedPreferencesManager manager;
    Button btn;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ride);
        spinner=findViewById(R.id.vehicle_type);
        tv_dist = findViewById(R.id.txt_distant);
        tv_time=findViewById(R.id.txt_time);
        tv_price=findViewById(R.id.txt_price);
        check_shedule = findViewById(R.id.check_shedule);
        payment_option = findViewById(R.id.payment_options);
        cash = findViewById(R.id.radio_cash);
        card = findViewById(R.id.radio_card);
        btn = findViewById(R.id.btn_book);
        linearLayout = findViewById(R.id.shedule);
        toolbar = findViewById(R.id.toolbar_bookride);
        tv_date_chose = findViewById(R.id.txt_date_chose);
        tv_time_chose =findViewById(R.id.txt_time_chose);
        intent = getIntent();
        city = (LocationZone)intent.getSerializableExtra("city");
        user  = (User)intent.getSerializableExtra("user");
        response = (DirectionsResponse) intent.getSerializableExtra("bookride");
        toolbar.setTitle("Book Ride");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        if(response!= null)
        {
            int distance = 0;
            int time = 0;
            for (int i = 0;i<response.routes.get(0).legs.size();i++)
            {
                distance += response.routes.get(0).legs.get(i).distance.value;
                time +=response.routes.get(0).legs.get(i).duration.value;

            }
            fulltime = time/60;
            fulldist = distance/1000;
            hours = time/3600;
            minutes = (time % 3600) / 60;
            km = distance/1000;
        }
        SharedPreferencesManager manager = new SharedPreferencesManager(this);
        String email = manager.getEmail();
        String token = manager.getToken();
        if (!token.isEmpty()) {
            fetchVehicle(token,city.getName());
            if(user!=null)
            {
                checkForCards(token);
            }
        }

        tv_time.setText(hours + " Hours " + minutes +" Minutes " );
        tv_dist.setText(km + " KM");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              fare=  showpricing(i,fulldist,fulltime);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_option.getCheckedRadioButtonId() == cash.getId() || payment_option.getCheckedRadioButtonId() == card.getId())
                {
                    bookRide();


                }
                else {
                    Toast.makeText(BookRide.this, "Please select payment option", Toast.LENGTH_SHORT).show();
                }
            }
        });
        check_shedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });
        tv_date_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        tv_time_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimepicker();
            }
        });
    }
    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String formattedMonth = String.format(Locale.getDefault(), "%02d", monthOfYear + 1);
                        String formattedDay = String.format(Locale.getDefault(), "%02d", dayOfMonth);
                        String selectedDate = formattedDay + "/" + formattedMonth + "/" + year;
                        tv_date_chose.setText(selectedDate);
                    }
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();

    }
    private void showTimepicker()
    {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(BookRide.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        Calendar selectedTimeCalendar = Calendar.getInstance();
                        selectedTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTimeCalendar.set(Calendar.MINUTE, minute);

                        // Use SimpleDateFormat to format the time in 12-hour format with AM/PM
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String selectedTime = dateFormat.format(selectedTimeCalendar.getTime());


                        tv_time_chose.setText(selectedTime);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void checkForCards(String token) {
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        // Call<List<Country>> call = path.getGountries(modifiedtoken);
        UserCards.Id id = new UserCards.Id();
        id.setId(user.getId());
        Call<UserCards> call=path.getcards(modifiedtoken,id);
        call.enqueue(new Callback<UserCards>() {
            @Override
            public void onResponse(Call<UserCards> call, Response<UserCards> response) {
                UserCards cards = response.body();
                if(cards!=null)
                {
                    card.setEnabled(true);
                }else {
                    card.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<UserCards> call, Throwable t) {

            }
        });
    }

    private void fetchVehicle(String token, String name) {
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
       // Call<List<Country>> call = path.getGountries(modifiedtoken);
        Call<List<CityVihicals>> call=path.getCityvihicals(name,modifiedtoken);
        call.enqueue(new Callback<List<CityVihicals>>() {
            @Override
            public void onResponse(Call<List<CityVihicals>> call, Response<List<CityVihicals>> response) {

                 vehicallist= response.body();
                if (vehicallist != null) {
                    types = new String[vehicallist.size()];
                    for (int i = 0; i < vehicallist.size(); i++) {
                        types[i] = vehicallist.get(i).getVehicleType();
                    }
                    ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(BookRide.this, android.R.layout.simple_list_item_1, types);
                    arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayadapter);
                    fare =showpricing(0,fulldist,fulltime);
                }
            }

            @Override
            public void onFailure(Call<List<CityVihicals>> call, Throwable t) {

            }
        });

    }

    private int showpricing(int i, float km, float minute) {
        int minfare = vehicallist.get(i).getMinFare();
        int basePriceDistance = vehicallist.get(i).getBasePriceDistance();
        int basePrice = vehicallist.get(i).getBasePrice();
        int unitDistanceprice   = vehicallist.get(i).getUnitDistancePrice();
        int unitTimeprice = vehicallist.get(i).getUnitTimePrice();
        int fare=0;
        if (fulldist > basePriceDistance)
        {

            fare += basePrice + (km-basePriceDistance)*unitDistanceprice;

            fare += minute*unitTimeprice;

        }else {
            fare += basePrice;
            fare+=minute*unitTimeprice;
        }
        if (fare <minfare)
        {
            fare = minfare;
        }
tv_price.setText(String.valueOf(fare));
        //bookRide();
        return fare;
    }

    public  void bookRide()
    {
        RideBody body ;
        List<RideBody.Stops> stops = new ArrayList<>();
        for(int i=0;i<response.routes.get(0).legs.size()-1;i++)
        {
            stops.add(new RideBody.Stops(response.routes.get(0).legs.get(i).endloc.lat,response.routes.get(0).legs.get(i).endloc.lng));
        }
       // String[] array = stops.toArray(new RideBody.Stops[0]);
        int last = response.routes.get(0).legs.size()-1 ;
        int radio = payment_option.getCheckedRadioButtonId();
        String payment_method = "";
        if (radio == cash.getId())
        {
            payment_method = cash.getText().toString();
        }else {
            payment_method = card.getText().toString();
        }
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Gson gson = new Gson();
        String jsonString = gson.toJson(stops);
        // Format the date and time
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);
if (check_shedule.isChecked())
{
formattedDate=tv_date_chose.getText().toString();
formattedTime = tv_time_chose.getText().toString();
}
        body = new RideBody(user.getId(),city.getId(),vehicallist.get(spinner.getSelectedItemPosition()).getId(),user.getName(),response.routes.get(0).legs.get(0).start_address,jsonString,response.routes.get(0).legs.get(last).end_address,String.valueOf(fulldist),String.valueOf(fulltime),String.valueOf(fare),payment_method,formattedDate,formattedTime);
        manager = new SharedPreferencesManager(this);
        String token = manager.getToken();
        if (token.isEmpty())
        {
            return;
        }
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<RideBody.Response> call = path.bookRide(body,modifiedtoken);
        call.enqueue(new Callback<RideBody.Response>() {
            @Override
            public void onResponse(Call<RideBody.Response> call, Response<RideBody.Response> response) {

                RideBody.Response  respon= response.body();
                if(respon != null) {
                    if (respon.message.isEmpty())
                    {
                        Toast.makeText(BookRide.this, respon.error, Toast.LENGTH_SHORT).show();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookRide.this);

                        // Set the title and message for the dialog
                        builder.setTitle("Taxi")
                                .setMessage(response.body().message);

                        // Add an OK button and its click listener
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(BookRide.this, HomeActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        });

                        // Create and show the AlertDialog
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                   /* if (response.body().message.equals("Ride is created successfully !!")) {

                    }*/
                }
            }

            @Override
            public void onFailure(Call<RideBody.Response> call, Throwable t) {

            }
        });
    }


}