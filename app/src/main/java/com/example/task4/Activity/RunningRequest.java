package com.example.task4.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.task4.Adapter.CSVGenerator;
import com.example.task4.Adapter.RunningRequestAdapter;
import com.example.task4.DataModels.Feedback;
import com.example.task4.DataModels.Payment;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RunningRequest extends AppCompatActivity {
    Socket socket;
    SharedPreferencesManager manager;
    public  String token;
    RunningRequestAdapter adapter;
    public int pagenum=1,filterpagenum=1;
    RecyclerView recyclerview;
    List<RidesRespons> responselist=new ArrayList<>();
    List<RidesRespons.Ride> ridelist=new ArrayList<>();
    List<RidesRespons.Ride> filteredlist=new ArrayList<>();
    RidesRespons rideresponse;
    int totelpage;
    Toolbar toolbar;


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

        recyclerview = findViewById(R.id.recyclerview1);
        manager = new SharedPreferencesManager(this);
        token = manager.getToken();
        toolbar = findViewById(R.id.toolbar_runningrequest);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("Running Requests");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunningRequest.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (!token.isEmpty()) {

            getallRequests(1,token);
        }
        socket.on("rideAssigned", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(() -> {
                    String json= args[0].toString();
                    Gson gson = new Gson();
                    RidesRespons.Ride rideData = gson.fromJson(json, RidesRespons.Ride.class);
                    //adapter.updatestatus(rideData);
                    ridelist.add(rideData);
                    adapter.filterlist(ridelist);



                  //  Toast.makeText(RunningRequest.this,  args[0].toString(), Toast.LENGTH_SHORT).show();
                });
            }
        });
        socket.on("driverTimeout", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json= args[0].toString();
                        Gson gson = new Gson();
                        RidesRespons rideData = gson.fromJson(json, RidesRespons.class);
                       ridelist = adapter.removeRide(rideData.ride);

                    }
                });
            }
        });
        socket.on("rideAccepted", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json= args[0].toString();
                        Gson gson = new Gson();
                        RidesRespons.Ride rideData = gson.fromJson(json, RidesRespons.Ride.class);
                        ridelist = adapter.updateRide(rideData);
                    }
                });
            }
        });
        socket.on("rideRejected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json= args[0].toString();
                        Gson gson = new Gson();
                        RidesRespons.Ride rideData = gson.fromJson(json, RidesRespons.Ride.class);
                        ridelist = adapter.removeRide(rideData);
                    }
                });
            }
        });
        socket.on("statusUpdated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json= args[0].toString();
                        Gson gson = new Gson();
                        RidesRespons.Ride rideData = gson.fromJson(json, RidesRespons.Ride.class);
                        ridelist = adapter.updateRide(rideData);

                    }
                });
            }
        });
        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RunningRequest.this, args.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    public  void getallRequests(int page,String token){
       String modifiedtoken = "Bearer " + token;
        filteredlist = new ArrayList<>();
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        String status = "[2,3,4,5,6]";
        Call<RidesRespons> call = path.getallrides(modifiedtoken,String.valueOf(page),status);
        call.enqueue(new Callback<RidesRespons>() {
            @Override
            public void onResponse(Call<RidesRespons> call, Response<RidesRespons> response) {
                if (response.isSuccessful()) {
                    rideresponse = response.body();
                    totelpage = response.body().pageCount;
                    // Add the new rides to the existing list

                    ridelist.addAll(response.body().rides);
                }
                    if (adapter == null) {
                        adapter = new RunningRequestAdapter(RunningRequest.this, ridelist);
                        recyclerview.setLayoutManager(new LinearLayoutManager(RunningRequest.this));
                        recyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new RunningRequestAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                            }

                            @Override
                            public void onAssignClick(int position) {
                                Gson gson = new Gson();
                                JsonObject rideobj = gson.toJsonTree(ridelist.get(position)).getAsJsonObject();
                                JsonObject ride = new JsonObject();
                                ride.add("ride",rideobj);
                                socket.emit("requestAcceptedByDriver",ride);
                            }

                            @Override
                            public void onNextClick(int position) {
                                if (ridelist.get(position).status==6)
                                {
                                    if (ridelist.get(position).paymentType.equals("card"))
                                    {
                                        Payment.PaymentBody body = new Payment.PaymentBody(ridelist.get(position).id);
                                        deductPayment(token,body);
                                        //dsdsd
                                        Toast.makeText(RunningRequest.this, "before complet", Toast.LENGTH_SHORT).show();
                                    }
                                    Gson gson = new Gson();
                                    JsonObject rideobj = gson.toJsonTree(ridelist.get(position)).getAsJsonObject();
                                    JsonObject ride = new JsonObject();
                                    ride.add("ride",rideobj);
                                    showFeedbackDialog(RunningRequest.this,ridelist.get(position).id,rideobj);
                                }
                                else {
                                    Gson gson = new Gson();
                                    JsonObject rideobj = gson.toJsonTree(ridelist.get(position)).getAsJsonObject();
                                    JsonObject ride = new JsonObject();
                                    ride.add("ride",rideobj);
                                    socket.emit("updateRideStatus",rideobj);
                                }
                            }

                            @Override
                            public void oncancelClick(int position) {
                                if (ridelist.get(position).assignSelected)
                                {
                                    Gson gson = new Gson();
                                    JsonObject rideobj = gson.toJsonTree(ridelist.get(position)).getAsJsonObject();
                                    JsonObject ride = new JsonObject();
                                    ride.add("ride",rideobj);
                                    socket.emit("selectedDriverRejectRide",ride);
                                }else {
                                    Gson gson = new Gson();
                                    JsonObject rideobj = gson.toJsonTree(ridelist.get(position)).getAsJsonObject();
                                    JsonObject ride = new JsonObject();
                                    ride.add("ride",rideobj);
                                    socket.emit("nearestDriverRejectRide",ride);
                                }
                            }
                        });

                    } else {
                        adapter.filterlist(ridelist);
                        adapter.notifyDataSetChanged();
                    }

            }

            @Override
            public void onFailure(Call<RidesRespons> call, Throwable t) {

            }
        });
    }
    public void deductPayment(String token, Payment.PaymentBody body){
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<Payment> call = path.deductPayment(modifiedtoken,body);
        call.enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful())
                {
                    Toast.makeText(RunningRequest.this,"amount "+ response.body().getAmount()+" is deduct!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
    public  void showFeedbackDialog(Context context, String id, JsonObject rideobj) {
        // Inflate the layout for the dialog
        View view = LayoutInflater.from(context).inflate(R.layout.feedback_dialog_layout, null);

        // Find views in the layout
        final EditText feedbackEditText = view.findViewById(R.id.feedback_text);
        final RatingBar ratingBar = view.findViewById(R.id.feedback_rating);

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);

        // Set positive button
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the feedback submission here
                String feedback = feedbackEditText.getText().toString();
                int rating =(int) ratingBar.getRating();
                // You can process or send the feedback and rating to a server, database, etc.
                Feedback feedbackobj = new Feedback(id,String.valueOf(rating),feedback);
                submitFeedback(feedbackobj);
            }
        });

        // Set negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancellation if needed
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                socket.emit("updateRideStatus",rideobj);
            }
        });

        // Show the AlertDialog
        builder.create().show();
    }

    private  void submitFeedback(Feedback feedbackobj) {
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<Feedback.FeedbackResponce> call = path.submitFeedback(modifiedtoken,feedbackobj);
        call.enqueue(new Callback<Feedback.FeedbackResponce>() {
            @Override
            public void onResponse(Call<Feedback.FeedbackResponce> call, Response<Feedback.FeedbackResponce> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(RunningRequest.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Feedback.FeedbackResponce> call, Throwable t) {

            }
        });
    }

}