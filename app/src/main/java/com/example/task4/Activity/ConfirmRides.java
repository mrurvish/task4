package com.example.task4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.task4.Adapter.CustomAdapter;
import com.example.task4.DataModels.AllVehicals;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.Fragment.BottomSheetFilters;
import com.example.task4.Fragment.RideDialoug;
import com.example.task4.Interface.BottomsheetDialougListner;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmRides extends AppCompatActivity implements BottomsheetDialougListner {
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

    int limit;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_rides);
        recyclerview = findViewById(R.id.recyclerview);
        manager = new SharedPreferencesManager(this);
        ridelist = new ArrayList<>();
        toolbar = findViewById(R.id.toolbarconfirm);
        filter = findViewById(R.id.image_filter);
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView search =(SearchView) searchItem.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredlist.clear();
                searchpage=1;
                searchtext = query;
                getallRequests(searchpage,token,query,servicetype,status,to,from);
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
                if (pagenum < rideresponse.pageCount) {
                    filteredlist.clear();
                    searchpage=1;
                        pagenum++;
                        getallRequests(pagenum, token, "", "", "", "", "");

                }}else {
                    filteredlist.clear();
                    searchpage=1;
                    getallRequests(pagenum, token, searchtext, servicetype, status, to, from);
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
            getallRequests(pagenum,token, "", "", "", "", "");
        }

        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                boolean val=view.canScrollVertically(1);
                if(searchtext.isEmpty()&&servicetype.isEmpty()&&status.isEmpty()&&to.isEmpty()&&from.isEmpty()) {
                    if (pagenum < rideresponse.pageCount) {
                        if (!val) {
                            pagenum++;
                            getallRequests(pagenum, token, "", "", "", "", "");
                        }
                    }
                }else {if (searchpage < rideresponse.pageCount) {
                        if (!val) {
                            searchpage++;
                            getallRequests(searchpage, token, searchtext, servicetype, status, to, from);
                        }
                    }
                }
            }
        });


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

    public void getallRequests(int page, String token,String searchstring,String vehicalstring, String statusstring, String dateto, String datefrom )
    {
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<RidesRespons> call = path.getallRides(modifiedtoken,String.valueOf(page),searchstring,vehicalstring,datefrom,dateto,statusstring);
        call.enqueue(new Callback<RidesRespons>() {
            @Override
            public void onResponse(Call<RidesRespons> call, Response<RidesRespons> response) {
                rideresponse = response.body();
                responselist.add(rideresponse);
                if (adapter==null) {
                    adapter = new CustomAdapter(ConfirmRides.this, ridelist);
                    recyclerview.setLayoutManager(new LinearLayoutManager(ConfirmRides.this));
                    recyclerview.setAdapter(adapter);
                    adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            openCustomDialog(position);

                        }
                    });

                }else {
                    adapter.notifyDataSetChanged();
                }
                if (searchstring.isEmpty()&&vehicalstring.isEmpty()&&statusstring.isEmpty()&&datefrom.isEmpty()&&dateto.isEmpty()) {
                    if (rideresponse != null && rideresponse.rides != null) {
                        totelpage=response.body().pageCount;
                        // Add the new rides to the existing list
                        ridelist.addAll(rideresponse.rides);
                        adapter.filterlist(ridelist);
                    }
                }else {
                    if (rideresponse != null && rideresponse.rides != null) {
                        totelfilterpage = response.body().pageCount;
                        // Add the new rides to the existing list
                        filteredlist.addAll(rideresponse.rides);
                        adapter.filterlist(filteredlist);
                    }
                }


            //adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<RidesRespons> call, Throwable t) {

            }
        });
    }
    private void openCustomDialog(int position) {
        RideDialoug customDialogFragment = new RideDialoug();
        customDialogFragment.setcustomedata(ridelist,position);

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
    public void onFilterRecived(String servicetype, String status, String from, String to) {
        this.servicetype = servicetype;
        this.status = status;
        this.to = to;
        this.from = from;
        getallRequests(searchpage,token,searchtext,servicetype,status,to,from);
    }
}