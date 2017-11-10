package com.smartpot.botanicaljournal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by MG on 2017-10-21.
 */

public class PlantAdapter extends ArrayAdapter {

    Context context;

    PlantAdapter(Context context, ArrayList<Plant> plants){
        super(context, R.layout.plant_row, plants);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.plant_row, parent, false);
        }

        Plant plant = (Plant)getItem(position);

        //Get plant info
        String plantName = plant.getName();
        int moisture = plant.getMoistureLevel();
        String imagePath = plant.getImagePath();

        // Get reference to layout elements
        TextView plantNameTextView = convertView.findViewById(R.id.plantName);
        ImageView plantImage = convertView.findViewById(R.id.plantImage);
        TextView levelText = convertView.findViewById(R.id.levelText);
        TextView lastWateredValue = convertView.findViewById(R.id.lastWateredValue);
        ProgressBar moistureBar = convertView.findViewById(R.id.moistureBar);
        if(plant.getPotId().equals("")) {
            levelText.setVisibility(View.INVISIBLE);
            moistureBar.setVisibility(View.INVISIBLE);
        }

        // Set layout elements
        plantNameTextView.setText(plantName);
        lastWateredValue.setText(ViewHelper.formatLastWateredTime(plant.getLastWatered()));

        if (imagePath.isEmpty()){
            plantImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_plant_profile));
        } else {
            Picasso.with(context).load(new File(plant.getImagePath())).into(plantImage);
        }
        moistureBar.setProgress(moisture);

        return convertView;
    }
}
