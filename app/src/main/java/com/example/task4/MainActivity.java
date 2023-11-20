package com.example.task4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public ExpantablelistviewAdapter adapter;
    List<String> title;
    HashMap<String,List<String>> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ExpandableListView list = findViewById(R.id.lv);
        addlist();
        adapter = new ExpantablelistviewAdapter(this,title,items);
        list.setAdapter(adapter);
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String po = title.get(groupPosition);
                List<String> childs = items.get(po);

                Toast.makeText(MainActivity.this, childs.get(childPosition), Toast.LENGTH_SHORT).show();
                parent.getChildAt(childPosition);
                return false;
            }
        });
        list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String po = title.get(groupPosition);
                Toast.makeText(MainActivity.this, po, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void addlist() {
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




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}