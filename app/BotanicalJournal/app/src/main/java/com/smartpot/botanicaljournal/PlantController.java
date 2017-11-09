package com.smartpot.botanicaljournal;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anthony on 2017-11-08.
 */

public class PlantController {
    private final DBHandler handler;

    PlantController(Context context) {
        handler = new DBHandler(context);
    }

    public ArrayList<Plant> getPlants() {
        return handler.getPlants();
    }

    public boolean createPlant(Plant plant) {
        // Do any checks here

        handler.addPlant(plant);

        return true;
    }

    public boolean updatePlant(Plant plant) {
        // do any checks here

        handler.updatePlant(plant);

        return true;
    }

    public boolean deletePlant(Plant plant) {

        handler.deletePlant(plant.getId());

        return true;
    }

    public boolean updateLastWatered(Plant plant, Date date) {

        handler.addLastWateredTimeForPlant(plant, date);

        return true;
    }


}
