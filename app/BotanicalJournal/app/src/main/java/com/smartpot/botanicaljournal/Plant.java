package com.smartpot.botanicaljournal;

import java.util.Random;

/**
 * Created by MG on 2017-10-21.
 */

public class Plant {
    private String imagePath;
    private String name;
    private String type;
    private int moisture;
    private String lastWatered;
    private String birthday;
    private String notes;

    Plant(){
        name = "Plant Name";
        type = "Some fancy type";
        Random r = new Random();
        moisture = 0;
        lastWatered = "0 hours";
        birthday = "-";
        notes = "";
    }


    Plant(String name){
        this.name = name;
        type = "Some fancy type";
        Random r = new Random();
        moisture = r.nextInt(100);
        lastWatered = "5 hours";
        birthday = "-";
        notes = "";
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMoisture() {
        return moisture;
    }

    public String getLastWatered() {
        return lastWatered;
    }

    public String getBirthday() {return birthday;}

    public String getNotes() {return notes;}
}
