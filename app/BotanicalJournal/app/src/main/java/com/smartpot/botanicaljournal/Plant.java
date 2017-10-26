package com.smartpot.botanicaljournal;

import java.util.Random;

/**
 * Created by MG on 2017-10-21.
 */

public class Plant {
    private String imagePath;
    private String name;
    private int moisture;
    private int time;

    Plant(String name){
        this.name = name;
        Random r = new Random();
        moisture = r.nextInt(100);
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public int getMoisture() {
        return moisture;
    }

    public int getTime() {
        return time;
    }
}
