package com.example.task4.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task4.Adapter.CSVGenerator;
import com.example.task4.Adapter.PDFGenerator;
import com.example.task4.Adapter.RideHistoryAdapter;
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

public class RideHistory extends AppCompatActivity implements BottomsheetDialougListner {
    public int pagenum = 1, filterpagenum = 1, searchpage = 1;
    SharedPreferencesManager manager;
    RecyclerView recyclerview;
    Toolbar toolbar;
    RidesRespons rideresponse;
    List<RidesRespons> responselist = new ArrayList<>();
    List<RidesRespons.Ride> ridelist = new ArrayList<>();
    List<RidesRespons.Ride> filteredlist = new ArrayList<>();
    List<RidesRespons.Ride> downloadlist = new ArrayList<>();
    RideHistoryAdapter adapter;
    String searchtext = "", servicetype = "", from = "", to = "", status = "";
    LinearLayout filter_list;
    String token;
    int totelpage, totelfilterpage;
    ImageView filter, download;
    List<AllVehicals> vehicals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);
        recyclerview = findViewById(R.id.recyclerview_history);
        manager = new SharedPreferencesManager(this);
        ridelist = new ArrayList<>();
        toolbar = findViewById(R.id.toolbarhistory);
        filter = findViewById(R.id.image_filter_history);
        filter_list = findViewById(R.id.filter_list_history);
        Menu menu = toolbar.getMenu();
        toolbar.setTitle("Confirm Ride");
        download = findViewById(R.id.download);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView search = (SearchView) searchItem.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredlist.clear();
                searchpage = 1;
                searchtext = query;
                getsearchrequest(searchpage, token, query, servicetype, status, to, from);
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
                if (servicetype.isEmpty() && status.isEmpty() && to.isEmpty() && from.isEmpty()) {
                    if (pagenum < totelpage) {
                        filteredlist.clear();

                        pagenum++;
                        getallRequests(pagenum, token);

                    }
                } else {
                    filteredlist.clear();
                    searchpage = 1;
                    getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
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
            getallRequests(pagenum, token);
        }
        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                boolean val = view.canScrollVertically(1);
                if (searchtext.isEmpty() && servicetype.isEmpty() && status.isEmpty() && to.isEmpty() && from.isEmpty()) {
                    if (pagenum < totelpage) {
                        if (!val) {
                            pagenum++;
                            getallRequests(pagenum, token);
                        }
                    }
                } else {

                    if (searchpage < totelfilterpage) {
                        if (!val) {
                            searchpage++;
                            getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                        }
                    }
                }
            }
        });

    }


    public void getsearchrequest(int page, String token, String searchstring, String vehicalstring, String statusstring, String dateto, String datefrom) {
        String modifiedtoken = "Bearer " + token;
        String st = "[-1,7]";


        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<RidesRespons> call = path.searchrides(modifiedtoken, String.valueOf(page), searchstring, vehicalstring, datefrom, dateto, statusstring, st);
        call.enqueue(new Callback<RidesRespons>() {
            @Override
            public void onResponse(Call<RidesRespons> call, Response<RidesRespons> response) {
                if (response.isSuccessful()) {
                    rideresponse = response.body();
                    totelfilterpage = response.body().pageCount;
                    filteredlist.addAll(response.body().rides);
                    adapter.filterlist(filteredlist);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(RideHistory.this, "No rides found", Toast.LENGTH_SHORT).show();
                }
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<RidesRespons> call, Throwable t) {

            }
        });
    }

    public void getallRequests(int page, String token) {
        String modifiedtoken = "Bearer " + token;
        filteredlist = new ArrayList<>();
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        String status = "[-1,7]";
        Call<RidesRespons> call = path.getallrides(modifiedtoken, String.valueOf(page), status);
        call.enqueue(new Callback<RidesRespons>() {
            @Override
            public void onResponse(Call<RidesRespons> call, Response<RidesRespons> response) {
                if (response.isSuccessful()) {
                    rideresponse = response.body();
                    totelpage = response.body().pageCount;
                    // Add the new rides to the existing list
                    ridelist.addAll(response.body().rides);
                    if (adapter == null) {
                        adapter = new RideHistoryAdapter(RideHistory.this, ridelist);
                        recyclerview.setLayoutManager(new LinearLayoutManager(RideHistory.this));
                        recyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new RideHistoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                openCustomDialog(position);

                            }

                            @Override
                            public void onAssignClick(int position) {
                                //openAssignDialoug(position);
                            }

                            @Override
                            public void onCancelclick(int position) {
                                //cancelRide(position);
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

    public void getvehicals(String token) {
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<List<AllVehicals>> call = path.getAllVehicals(modifiedtoken);
        call.enqueue(new Callback<List<AllVehicals>>() {
            @Override
            public void onResponse(Call<List<AllVehicals>> call, Response<List<AllVehicals>> response) {
                if (response.isSuccessful()) {
                    vehicals = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<AllVehicals>> call, Throwable t) {

            }
        });
    }

    private void showBottomSheetDialog() {
        BottomSheetFilters bottomsheetfilters = new BottomSheetFilters();
        bottomsheetfilters.setVehicals(vehicals, 1);
        bottomsheetfilters.setMyDialogListener(this);
        bottomsheetfilters.show(getSupportFragmentManager(), "filters");

    }

    private void openCustomDialog(int position) {
        RideDialoug customDialogFragment = new RideDialoug();
        if (searchtext.isEmpty() && servicetype.isEmpty() && status.isEmpty() && to.isEmpty() && from.isEmpty()) {
            customDialogFragment.setcustomedata(ridelist, position);
        } else {
            customDialogFragment.setcustomedata(filteredlist, position);
        }
        customDialogFragment.show(getSupportFragmentManager(), "CustomDialogFragment");
    }

    @Override
    public void onFilterRecived(String servicetype, String status, String from, String to) {
        this.servicetype = servicetype;
        this.status = status;
        this.to = to;
        this.from = from;
        filteredlist = new ArrayList<>();
        searchpage = 1;
        getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
        filter_list.removeAllViews();
        showfilter();
    }

    private void showfilter() {
        if (!servicetype.isEmpty()) {
            LinearLayout linearLayout0 = new LinearLayout(RideHistory.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7, 7, 7, 7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(RideHistory.this);

            tv.setText(servicetype);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );


            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(RideHistory.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv, 0);
            linearLayout0.addView(image, 1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);
                servicetype = "";
                if (status.isEmpty() && from.isEmpty() && to.isEmpty()) {
                    getallRequests(pagenum, token);
                } else {
                    searchpage = 1;
                    filteredlist = new ArrayList<>();
                    getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                }
            });
        }
        if (!status.isEmpty()) {
            LinearLayout linearLayout0 = new LinearLayout(RideHistory.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7, 7, 7, 7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(RideHistory.this);

            tv.setText(status);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(RideHistory.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv, 0);
            linearLayout0.addView(image, 1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);
                status = "";
                if (servicetype.isEmpty() && from.isEmpty() && to.isEmpty()) {
                    getallRequests(pagenum, token);
                } else {
                    searchpage = 1;
                    filteredlist = new ArrayList<>();
                    getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                }

            });

        }
        if (!from.isEmpty()) {
            LinearLayout linearLayout0 = new LinearLayout(RideHistory.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7, 7, 7, 7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(RideHistory.this);

            tv.setText("From:" + from);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(RideHistory.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv, 0);
            linearLayout0.addView(image, 1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);
                from = "";
                to = "";
                if (status.isEmpty() && servicetype.isEmpty()) {
                    getallRequests(pagenum, token);
                } else {
                    searchpage = 1;
                    filteredlist = new ArrayList<>();
                    getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                }

            });
        }
        if (!from.isEmpty() && !to.isEmpty()) {
            LinearLayout linearLayout0 = new LinearLayout(RideHistory.this);
            linearLayout0.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout0.setBackgroundResource(R.drawable.round_shape);
            linearLayout0.setPadding(7, 7, 7, 7);
            linearLayout0.setGravity(Gravity.CENTER);
            TextView tv = new TextView(RideHistory.this);

            tv.setText("To:" + to);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            tv.setLayoutParams(tvParams);
            ImageView image = new ImageView(RideHistory.this);
            image.setImageResource(R.drawable.ic_baseline_close_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_VERTICAL;
            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);
            linearLayout0.addView(tv, 0);
            linearLayout0.addView(image, 1);
            filter_list.addView(linearLayout0);
            image.setOnClickListener(view -> {
                filter_list.removeView(linearLayout0);

                to = "";
                if (status.isEmpty() && servicetype.isEmpty() && from.isEmpty()) {
                    getallRequests(pagenum, token);
                } else {
                    searchpage = 1;
                    filteredlist = new ArrayList<>();
                    getsearchrequest(searchpage, token, searchtext, servicetype, status, to, from);
                }

            });
        }

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.download, popupMenu.getMenu());
        final int MENU_ITEM_1 = R.id.csv;
        final int MENU_ITEM_2 = R.id.pdf;
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == MENU_ITEM_1) {

                    downloadRideHistory(0);


                }
                if (item.getItemId() == MENU_ITEM_2) {

                    downloadRideHistory(1);
                }

                return false;
            }
        });

        popupMenu.show();
    }

    public void downloadRideHistory(int i) {
        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        String status = "[-1,7]";
        Call<List<RidesRespons.Ride>> call = path.fileDownload(modifiedtoken, status);
        call.enqueue(new Callback<List<RidesRespons.Ride>>() {
            @Override
            public void onResponse(Call<List<RidesRespons.Ride>> call, Response<List<RidesRespons.Ride>> response) {
                if (response.isSuccessful()) {
                    downloadlist = response.body();
                    if (i == 0) {
                        CSVGenerator csv = new CSVGenerator(getContentResolver());
                        csv.generateCSV(downloadlist);
                    }
                    if (i == 1) {
                        PDFGenerator pdf = new PDFGenerator(getContentResolver());

                        pdf.generatePDF(downloadlist, "hello");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RidesRespons.Ride>> call, Throwable t) {
                Toast.makeText(RideHistory.this, "failed.....", Toast.LENGTH_SHORT).show();
            }
        });
    }
}