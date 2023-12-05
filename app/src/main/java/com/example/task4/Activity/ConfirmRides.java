package com.example.task4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task4.Adapter.CustomAdapter;
import com.example.task4.DataModels.AllVehicals;
import com.example.task4.DataModels.DriverDetails;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.Fragment.AssignDialoug;
import com.example.task4.Fragment.BottomSheetFilters;
import com.example.task4.Fragment.RideDialoug;
import com.example.task4.Interface.AssignDialougListner;
import com.example.task4.Interface.BottomsheetDialougListner;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;
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

public class ConfirmRides extends AppCompatActivity implements BottomsheetDialougListner, AssignDialougListner {
    SharedPreferencesManager manager;
    RecyclerView recyclerview;
   Toolbar toolbar;
    RidesRespons rideresponse;
    List<RidesRespons> responselist=new ArrayList<>();
    List<RidesRespons.Ride> ridelist=new ArrayList<>();
    List<RidesRespons.Ride> filteredlist=new ArrayList<>();
    CustomAdapter adapter;
    ProgressBar pbar;
    ImageView filter;
    public int pagenum=1,filterpagenum=1,searchpage=1;
    int totelpage,totelfilterpage;
    NestedScrollView nestedSV;
    String searchtext="",servicetype="",from="",to="",status="";
    List<AllVehicals> vehicals;
    LinearLayout filter_list;
   Socket socket;
    String TAG = "soket";

    int limit;
    String token;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_rides);

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
                            Toast.makeText(ConfirmRides.this, "Socket is connected..", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


            recyclerview = findViewById(R.id.recyclerview);
        manager = new SharedPreferencesManager(this);
        ridelist = new ArrayList<>();
        toolbar = findViewById(R.id.toolbarconfirm);
        filter = findViewById(R.id.image_filter);
        filter_list= findViewById(R.id.filter_list);
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView search =(SearchView) searchItem.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredlist.clear();
                searchpage=1;
                searchtext = query;
                getsearchrequest(searchpage,token,query,servicetype,status,to,from);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //filter(newText);
                return false;
            }

        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                // Perform your action when the close button is pressed
                // For example, clear the search results or do something else
                searchtext = "";
                if (servicetype.isEmpty()&&status.isEmpty()&&to.isEmpty()&&from.isEmpty())
                {
                if (pagenum < totelpage) {
                    filteredlist.clear();

                        pagenum++;
                        getallRequests(pagenum, token);

                }}else {
                    filteredlist.clear();
                    searchpage=1;
                    getsearchrequest(pagenum, token, searchtext, servicetype, status, to, from);
                }
                // Return true if you want to consume the event, false otherwise
                return false;
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });

         token = manager.getToken();
        if (!token.isEmpty()) {
            getvehicals(token);
            getallRequests(pagenum,token);
        }

        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                boolean val=view.canScrollVertically(1);
                if(searchtext.isEmpty()&&servicetype.isEmpty()&&status.isEmpty()&&to.isEmpty()&&from.isEmpty()) {
                    if (pagenum < totelpage) {
                        if (!val) {
                            pagenum++;
                            getallRequests(pagenum, token);
                        }
                    }
                }else {

                    if (searchpage < totelfilterpage) {
                        if (!val) {
                            searchpage++;
                            getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                        }
                    }
                }
            }
        });


        //listning to socket


    }

    private void showBottomSheetDialog() {
        BottomSheetFilters bottomsheetfilters = new BottomSheetFilters();
        bottomsheetfilters.setVehicals(vehicals);
        bottomsheetfilters.setMyDialogListener(this);
        bottomsheetfilters.show(getSupportFragmentManager(), "filters");

    }

   /* private void filter(String newText) {
        List<RidesRespons.Ride> filteredlist= new ArrayList<>();
        for (RidesRespons.Ride ride : ridelist)
        {
            if (ride.userName.toLowerCase().contains(newText)) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(ride);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterlist(filteredlist);
        }
    }*/

    public void getsearchrequest(int page, String token,String searchstring,String vehicalstring, String statusstring, String dateto, String datefrom )
    {
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<RidesRespons> call = path.searchrides(modifiedtoken,String.valueOf(page),searchstring,vehicalstring,datefrom,dateto,statusstring);
        call.enqueue(new Callback<RidesRespons>() {
            @Override
            public void onResponse(Call<RidesRespons> call, Response<RidesRespons> response) {
                if (response.isSuccessful()) {
                    rideresponse = response.body();
                    totelfilterpage = response.body().pageCount;
                    filteredlist.addAll(response.body().rides);
                    adapter.filterlist(filteredlist);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(ConfirmRides.this, "No rides found", Toast.LENGTH_SHORT).show();
                }
            //adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RidesRespons> call, Throwable t) {

            }
        });
    }
    public  void getallRequests(int page,String token){
        String modifiedtoken = "Bearer " + token;
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
                        adapter = new CustomAdapter(ConfirmRides.this, ridelist);
                        recyclerview.setLayoutManager(new LinearLayoutManager(ConfirmRides.this));
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
        });
    }

    private void openAssignDialoug(int position) {
        AssignDialoug assignDialoug = new AssignDialoug();
        assignDialoug.setAssignListner(this);
        if(searchtext.isEmpty()&&servicetype.isEmpty()&&status.isEmpty()&&to.isEmpty()&&from.isEmpty()) {
            assignDialoug.setcustomedata(ridelist, position);
        }
        else {
            assignDialoug.setcustomedata(filteredlist,position);
        }
        assignDialoug.show(getSupportFragmentManager(),"assign");
    }

    private void openCustomDialog(int position) {
        RideDialoug customDialogFragment = new RideDialoug();
        if(searchtext.isEmpty()&&servicetype.isEmpty()&&status.isEmpty()&&to.isEmpty()&&from.isEmpty()) {
            customDialogFragment.setcustomedata(ridelist, position);
        }
        else {
            customDialogFragment.setcustomedata(filteredlist,position);
        }
        customDialogFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
    }
    public void getvehicals(String token){
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<List<AllVehicals>> call = path.getAllVehicals(modifiedtoken);
        call.enqueue(new Callback<List<AllVehicals>>() {
            @Override
            public void onResponse(Call<List<AllVehicals>> call, Response<List<AllVehicals>> response) {
                if (response.isSuccessful())
                {
                    vehicals = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<AllVehicals>> call, Throwable t) {

            }
        });
    }
    @Override
    public void onDriverreceived(RidesRespons.Ride ride, DriverDetails Driver) {
        DriverDetails.Combined combined = new DriverDetails.Combined(ride,Driver);
        Object obj = combined;
        Gson gson = new Gson();
        JsonObject rideobj = gson.toJsonTree(ride).getAsJsonObject();
        JsonObject driverobj = gson.toJsonTree(Driver).getAsJsonObject();
        JsonObject combinedObj = new JsonObject();
        combinedObj.add("ride",rideobj);
        combinedObj.add("driver",driverobj);

        socket.emit("assignToSelectedDriver",combinedObj);
        socket.on("error", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(() -> Toast.makeText(ConfirmRides.this, args.toString(), Toast.LENGTH_SHORT).show());

            }
        });
        socket.on("rideAssigned", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(() -> {
                    String json= args[0].toString();
                    Gson gson = new Gson();
                    RidesRespons.Ride rideData = gson.fromJson(json, RidesRespons.Ride.class);
                    adapter.updatestatus(rideData);


                    Toast.makeText(ConfirmRides.this,  args[0].toString(), Toast.LENGTH_SHORT).show();
                });
            }
        });
        //Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onFilterRecived(String servicetype, String status, String from, String to) {
        this.servicetype = servicetype;
        this.status = status;
        this.to = to;
        this.from = from;
        filteredlist = new ArrayList<>();
        getsearchrequest(searchpage,token,searchtext,servicetype,status,to,from);
filter_list.removeAllViews();
       showfilter();


    }

    private void showfilter() {
        if (!servicetype.isEmpty())
        {
            LinearLayout linearLayout0 = new LinearLayout(ConfirmRides.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7,7,7,7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(ConfirmRides.this);

            tv.setText(servicetype);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );


            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(ConfirmRides.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv,0);
            linearLayout0.addView(image,1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);
                servicetype="";
                if (status.isEmpty()&&from.isEmpty()&&to.isEmpty())
                {
                    getallRequests(pagenum,token);
                }else {
                    searchpage=1;
                    filteredlist=new ArrayList<>();
                    getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                }
            });
        }
        if (!status.isEmpty())
        {
            LinearLayout linearLayout0 = new LinearLayout(ConfirmRides.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7,7,7,7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(ConfirmRides.this);

            tv.setText(status);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(ConfirmRides.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv,0);
            linearLayout0.addView(image,1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);
                status="";
                if (servicetype.isEmpty()&&from.isEmpty()&&to.isEmpty()){
                    getallRequests(pagenum,token);
                }else {
                    searchpage=1;
                    filteredlist=new ArrayList<>();
                    getsearchrequest(searchpage,token,searchtext,servicetype,status,to,from);
                }

            });

        }
        if (!from.isEmpty())
        {
            LinearLayout linearLayout0 = new LinearLayout(ConfirmRides.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7,7,7,7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(ConfirmRides.this);

            tv.setText("From:"+from);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(ConfirmRides.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv,0);
            linearLayout0.addView(image,1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);
                from="";
                to ="";
                if (status.isEmpty()&&servicetype.isEmpty()){
                    getallRequests(pagenum,token);
                }else {
                    searchpage=1;
                    filteredlist=new ArrayList<>();
                    getsearchrequest(searchpage,token,searchtext,servicetype,status,to,from);
                }

            });
        }
        if (!from.isEmpty()&&!to.isEmpty())
        {
            LinearLayout linearLayout0 = new LinearLayout(ConfirmRides.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7,7,7,7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(ConfirmRides.this);

            tv.setText("To:"+to);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(ConfirmRides.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv,0);
            linearLayout0.addView(image,1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);

                to ="";
                if (status.isEmpty()&&servicetype.isEmpty()&&from.isEmpty()){
                    getallRequests(pagenum,token);
                }else {
                    searchpage=1;
                    filteredlist=new ArrayList<>();
                    getsearchrequest(searchpage,token,searchtext,servicetype,status,to,from);
                }

            });
        }

    }


}