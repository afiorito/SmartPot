package com.smartpot.botanicaljournal.Views;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.smartpot.botanicaljournal.Models.Plant;
import com.smartpot.botanicaljournal.Helpers.PlantViewState;
import com.smartpot.botanicaljournal.R;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Set the sidebar navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        // Display the main fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PlantFragment.newInstance());
        transaction.commit();
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

    @SuppressWarnings("StatementWithEmptyBody")
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
        else if (id == R.id.settings) {
            getSupportActionBar().setTitle("Settings");
            transaction.replace(R.id.frame_layout, SettingsFragment.newInstance()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

