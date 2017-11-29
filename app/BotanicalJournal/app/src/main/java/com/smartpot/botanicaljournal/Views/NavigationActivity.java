package com.smartpot.botanicaljournal.Views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.smartpot.botanicaljournal.Controllers.PlantController;
import com.smartpot.botanicaljournal.DataRetriever;
import com.smartpot.botanicaljournal.Models.Plant;
import com.smartpot.botanicaljournal.Helpers.PlantViewState;
import com.smartpot.botanicaljournal.R;

import java.util.ArrayList;
import java.util.Calendar;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TextView plantCountTextView;
    private PlantController pc;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        pc = new PlantController(this);

        setupAlarms();

        plantCountTextView = navigationView.getHeaderView(0).findViewById(R.id.plantCountTextView);
        updatePlantCount();


        // Set the sidebar navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updatePlantCount();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }


        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Display the main fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PlantFragment.newInstance());
        transaction.commit();
    }

    public final static String ALARM_POT_ID = "pot_id";
    public final static String ALARM_PLANT_ID = "plant_id";

    private void setupAlarms() {
        ArrayList<Plant> plants = pc.getPlants();

        for(Plant plant : plants) {
            if (!plant.getPotId().equals("")) {
                Log.i("TAG", "Setting Alarm For: " + plant.getPotId());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, DataRetriever.class);
                intent.putExtra(ALARM_POT_ID, plant.getPotId());
                intent.putExtra(ALARM_PLANT_ID, plant.getId());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
            }
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (id == R.id.myPlants){
            getSupportActionBar().setTitle(R.string.app_name);
            transaction.replace(R.id.frame_layout, PlantFragment.newInstance()).commit();
        }
        else if (id == R.id.addPlant) {
            ManagePlantFragment managePlantFragment = ManagePlantFragment.newInstance(PlantViewState.ADDPLANT);
            managePlantFragment.setPlant(new Plant());
            transaction.replace(R.id.frame_layout, managePlantFragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updatePlantCount() {
        long plantCount = pc.getPlantCount();
        String countString = "You have " + pc.getPlantCount() + " Plant";
        if(plantCount != 1) {
            countString += "s";
        }
        plantCountTextView.setText(countString);
    }
}

