package com.example.task4.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.task4.Adapter.CreateChat;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.Adapter.ExpantablelistviewAdapter;
import com.example.task4.DataModels.Menu;
import com.example.task4.R;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 2;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public ExpantablelistviewAdapter adapter;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //  TextView tv = findViewById(R.id.textView3);
        SharedPreferencesManager manager = new SharedPreferencesManager(this);
        String email = manager.getEmail();
        String token = manager.getToken();
        //tv.setText("email:"+email+"  "+"Token:"+token);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ExpandableListView list = findViewById(R.id.lv);
        Menu menu = new Menu();
        adapter = new ExpantablelistviewAdapter(this, menu.title, menu.items);
        list.setAdapter(adapter);
        View header=getLayoutInflater().inflate(R.layout.nav_header, null);
        list.addHeaderView(header);
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Menu menu = new Menu();

                 String po = menu.title.get(groupPosition);
                 List<String> childs = menu.items.get(po);
                assert childs != null;
                String child = childs.get(childPosition);
                if (childs == null)
                {
                    navigate(po);
                }else {
                    navigate(child);
                }
                 //Toast.makeText(MainActivity.this, childs.get(childPosition), Toast.LENGTH_SHORT).show();
                 parent.getChildAt(childPosition);
                return false;
            }
        });
        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //  String po = title.get(groupPosition);
                // Toast.makeText(MainActivity.this, po, Toast.LENGTH_SHORT).show();
                Menu menu = new Menu();
                String click = menu.title.get(groupPosition);
                if (click.equals("Users"))
                {
                    intent = new Intent(HomeActivity.this,UserActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        checkLocationPermission();
       /* CreateChat chat = new CreateChat();
        chat.seendMessage("123656");
        chat.receiveMessage("123656");*/
    }

    private void navigate(String po) {
       switch (po)
       {
           case "Create Ride":
               intent = new Intent(HomeActivity.this, CreateRides.class);
               break;
           case "Confirmed Rides":
               intent = new Intent(HomeActivity.this,ConfirmRides.class);
               break;
           case "Running request":
               intent = new Intent(HomeActivity.this,RunningRequest.class);
               break;
           case "Ride History":
               intent = new Intent(HomeActivity.this,RideHistory.class);
               break;
           default:
               intent = null;
               System.out.println("Invalid option!");
               break;
       }
       if (intent!=null) {
           startActivity(intent);

       }
    }

  /*  private void addlist() {
        title = new ArrayList<String>();
        items = new HashMap<String,List<String>>();
        title.add("Rides");
        title.add("Users");
        title.add("Drivers");
        title.add("Pricing");
        title.add("Settings");

        List<String> rides = new ArrayList<String>();
        rides.add("Create Ride");
        rides.add("Confirmed Rides ");
        rides.add(" Ride History");
        items.put(title.get(0),rides);

        List<String> drivers = new ArrayList<String>();
        drivers.add("List");
        drivers.add(" Running request");
        items.put(title.get(2),drivers);

        List<String> pricing = new ArrayList<String>();
        pricing.add("Country");
        pricing.add("City");
        pricing.add("Vehicle type");
        pricing.add("Vehicle Pricing");
        items.put(title.get(3),pricing);




    }*/
  private void checkLocationPermission() {
      String[] permissions = {
              Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.MANAGE_EXTERNAL_STORAGE
      };
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          // Request location permission
          ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_LOCATION);
      }

      // Check if storage permissions are not granted
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
              || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          // Request storage permissions
          ActivityCompat.requestPermissions(this,permissions, MY_PERMISSIONS_REQUEST_STORAGE);
      }
  }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer if it's open
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Handle the back press as you normally would
            super.onBackPressed();
        }
    }
}