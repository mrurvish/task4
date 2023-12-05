package com.example.task4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.task4.Adapter.CustomAdapter;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RunningRequest extends AppCompatActivity {
    Socket socket;
    SharedPreferencesManager manager;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_request);
        try {
            socket = IO.socket("http://192.168.0.215:3000");
            socket.connect();
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    // This will be called when the socket is connected

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RunningRequest.this, "Socket is connected..", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        manager = new SharedPreferencesManager(this);
        token = manager.getToken();
        if (!token.isEmpty()) {

            getallRequests(1,token);
        }
        socket.on("rideAssigned", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
    }
    public  void getallRequests(int page,String token){
     /*   String modifiedtoken = "Bearer " + token;
        filteredlist = new ArrayList<>();
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        String status = "[1,2,3,4,5,6]";
        Call<RidesRespons> call = path.getallrides(modifiedtoken,String.valueOf(page),status);
        call.enqueue(new Callback<RidesRespons>() {
            @Override
            public void onResponse(Call<RidesRespons> call, Response<RidesRespons> response) {
                if (response.isSuccessful()) {
                    rideresponse = response.body();
                    totelpage = response.body().pageCount;
                    // Add the new rides to the existing list
                    ridelist.addAll(response.body().rides);
                    if (adapter == null) {
                        adapter = new CustomAdapter(RunningRequest.this, ridelist);
                        recyclerview.setLayoutManager(new LinearLayoutManager(RunningRequest.this));
                        recyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                openCustomDialog(position);

                            }

                            @Override
                            public void onAssignClick(int position) {
                                openAssignDialoug(position);
                            }
                        });

                    } else {
                        adapter.filterlist(ridelist);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<RidesRespons> call, Throwable t) {

            }
        });*/
    }
}