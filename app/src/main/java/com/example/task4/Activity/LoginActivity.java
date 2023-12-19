package com.example.task4.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.task4.DataModels.Admin;
import com.example.task4.DataModels.Login;
import com.example.task4.DataModels.LoginParameter;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button login_btn ;
    EditText email,password;

    SharedPreferencesManager manager;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       login_btn= findViewById(R.id.btn_login);
        email= findViewById(R.id.txt_email);
        password =findViewById(R.id.txt_password);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                       token = task.getResult();

                        // Log and toast
                       // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("token", token);
                        //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateText())
                {
                    checkUser();
                }
            }
        });
        manager = new SharedPreferencesManager(this);
        String token = manager.getToken();
        if (!token.isEmpty())
        {
            showHome();
        }

    }

    private void checkUser() {
        LoginParameter parameter = new LoginParameter(email.getText().toString(),password.getText().toString(),token);
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);

        Call<Login> call = path.getUser(parameter);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Toast.makeText(LoginActivity.this, "succesful", Toast.LENGTH_SHORT).show();
                email.setText(null);
                password.setText(null);
                Admin ad = response.body().getAdmin();
                manager = new SharedPreferencesManager(LoginActivity.this);
                manager.setEmail(ad.email);
                manager.setId(ad._id);
                manager.setToken(response.body().getToken());


                showHome();
                Log.d("response",response.body().getToken()+ad.getEmail());
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failure", Toast.LENGTH_SHORT).show();
             //   Log.d("response",t.getMessage(),t);
            }
        });
    }

    private void showHome() {
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        startActivity(intent);
        this.finish();

    }

    private Boolean validateText() {
        String emailtxt = email.getText().toString();
        String passwordtxt  = password.getText().toString();
        if (!emailtxt.isEmpty() && !passwordtxt.isEmpty() )
        {
            return  true;
        }
        else {
            return false;
        }
    }
}