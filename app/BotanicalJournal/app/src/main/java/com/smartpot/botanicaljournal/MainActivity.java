package com.smartpot.botanicaljournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create temporary plants
        ArrayList<Plant> plants = new ArrayList<>();
        for (int i = 1; i <= 3 ; i++){
            plants.add(new Plant("Plant " + i));
        }

        //Create plant adapter
        PlantAdapter plantAdapter =
                new PlantAdapter(this, plants);

        //Get reference to ListView
        ListView plantView = (ListView) findViewById(R.id.plantListView);
        LinearLayout noPlantsView = (LinearLayout) findViewById(R.id.noPlantsView);

        //Set ListView layout
        plantView.setEmptyView(noPlantsView); // Set default layout when list is empty
        plantView.setAdapter(plantAdapter);

    }
}