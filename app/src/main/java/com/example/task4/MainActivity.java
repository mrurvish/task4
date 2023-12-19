package com.example.task4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.task4.Activity.HomeActivity;
import com.example.task4.Activity.LoginActivity;
import com.example.task4.Preference.SharedPreferencesManager;

public class MainActivity extends AppCompatActivity {

    SharedPreferencesManager manager;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        manager = new SharedPreferencesManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // on below line we are
                // creating a new intent


                String token = manager.getToken();
                if (!token.isEmpty())
                {
                     i = new Intent(MainActivity.this, HomeActivity.class);
                }else{
                    i = new Intent(MainActivity.this, LoginActivity.class);
                }
                // on below line we are
                // starting a new activity.
                startActivity(i);

                // on the below line we are finishing
                // our current activity.
                finish();
            }
        }, 2000);
    }

}