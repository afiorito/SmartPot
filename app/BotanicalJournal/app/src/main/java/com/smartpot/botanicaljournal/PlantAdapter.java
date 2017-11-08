package com.smartpot.botanicaljournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MG on 2017-10-21.
 */

public class PlantAdapter extends ArrayAdapter {


    PlantAdapter(Context context, ArrayList<Plant> plants){
        super(context, R.layout.plant_row, plants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View plantRow = inflater.inflate(R.layout.plant_row, parent, false);

        //Get plant info
        String plantName = ((Plant)getItem(position)).getName();
        int moisture = ((Plant)getItem(position)).getMoisture();

        // Get reference to layout elements
        TextView plantNameTextView = plantRow.findViewById(R.id.plantName);
        ImageView plantImage = plantRow.findViewById(R.id.plantImage);
        ProgressBar progressBar = plantRow.findViewById(R.id.progressBar);
        //ProfileField lastWateredText = plantRow.findViewById(R.id.lastWatered);

        // Set layout elements
        plantNameTextView.setText(plantName);
        plantImage.setImageDrawable(getContext().getDrawable(R.drawable.flower));
        progressBar.setProgress(moisture);
        //lastWateredText.setText("5 hours ago");

        return plantRow;
    }
}
