package com.example.task4.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.task4.DataModels.Country;
import com.example.task4.Preference.CreatePref;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.DataModels.User;
import com.example.task4.DataModels.UserPhone;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateRides extends AppCompatActivity {
    SharedPreferencesManager manager;
    Spinner spinner, spin2;
    EditText phone_number;
    ImageView img;
    TextView tvname,tvemail,tvnum;
    private ProgressBar progressBar;
    List<Country> country;
    LinearLayout layout;
    ImageView btn,btn_clear;
    Button btn_next;
    String[] codes;
    MaterialToolbar toolbar;
    CreatePref pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rides);
        tvname= findViewById(R.id.txt_user_name);
        img = findViewById(R.id.profile_pic);
        tvemail = findViewById(R.id.txt_user_email);
        tvnum = findViewById(R.id.txt_user_phone);
        spinner = findViewById(R.id.spinner);
        layout = findViewById(R.id.user_card);
       progressBar = findViewById(R.id.p_bar);
       phone_number = findViewById(R.id.txt_num);
       btn = findViewById(R.id.btn_find_user);
       btn_clear=findViewById(R.id.btn_cleare_user);
       btn_next=findViewById(R.id.btn_next_createride);
       toolbar=findViewById(R.id.toolbar_createride);
        manager = new SharedPreferencesManager(this);
        pref=new CreatePref(this,"user");
        String token = manager.getToken();
        if (!token.isEmpty()) {
           // getcodes(token);
        } else {
            Toast.makeText(this, "token is empty", Toast.LENGTH_SHORT).show();
            //   showHome();
        }
    toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Create Ride");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
     btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String num = phone_number.getText().toString();
             String code = spinner.getSelectedItem().toString();
             if (checknumber(num) && !code.isEmpty())
             {
                 findUser(num,code);
             }
            // checknumber(num);
         }
     });
btn_clear.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        tvemail.setText("");
        tvname.setText("");
        tvnum.setText("");
        img.setImageResource(0);
        btn_next.setEnabled(false);
    }
});
    }

    private void findUser(String num, String code) {
        UserPhone phone = new UserPhone(code,num);
        manager = new SharedPreferencesManager(this);
        String token = manager.getToken();
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);

        Call<User> call = path.getUserbyNumber(token,phone);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideKeyboard(CreateRides.this);
             //    = response.body();

                if(response.isSuccessful())
                {
                    Toast.makeText(CreateRides.this, "succesful", Toast.LENGTH_SHORT).show();
                    tvemail.setText(response.body().getEmail());
                    tvname.setText(response.body().getName());
                    tvnum.setText(response.body().getPhone());
                    String base = "http://192.168.0.215:3000/";
                    Picasso.get()
                            .load(base + response.body().getProfile())
                            .into(img);


                    User user = response.body();
                    btn_next.setEnabled(true);
                    nextScreen(user);
                    pref.setString("userdata",response.body());

                }
                else {

                    Toast.makeText(CreateRides.this, "User not Found...!", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void nextScreen(User user) {

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(user.getEmail()!=null) {
                        Intent intent = new Intent(CreateRides.this, CreateRidesMap.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }
                }
            });


    }

    private boolean checknumber(String num) {
        if(num.length() == 10)
        {

            return true;}
        else {
            Toast.makeText(this, "Enter 10 Digit Number...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        manager = new SharedPreferencesManager(this);
        String token = manager.getToken();
        getcodes(token);
    }

    private void getcodes(String token) {
        progressBar.setVisibility(View.VISIBLE);
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<List<Country>> call = path.getGountries(modifiedtoken);
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                country = response.body();

                if (country != null) {
                    codes = new String[country.size()];
                    for (int i = 0; i < country.size(); i++) {
                        codes[i] = country.get(i).getCode();
                    }
                    ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(CreateRides.this, android.R.layout.simple_list_item_1, codes);
                    arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayadapter);
                }

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateRides.this, "Error!!!!!!!!!", Toast.LENGTH_SHORT).show();
                Log.d("createrides", t.getMessage());
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(CreateRides.this, "succesful", Toast.LENGTH_SHORT).show();
        tvemail.setText("");
        tvname.setText("");
        tvnum.setText("");
     img.setImageResource(0);
        btn_next.setEnabled(false);
    }
}