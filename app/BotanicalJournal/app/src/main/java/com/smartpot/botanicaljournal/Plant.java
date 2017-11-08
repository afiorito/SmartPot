package com.smartpot.botanicaljournal;

import java.util.Date;
import java.util.Random;

/**
 * Created by MG on 2017-10-21.
 */

public class Plant {

    private long id;
    private String name;
    private String phylogeny;
    private Date birthDate;
    private String notes;
    private Date lastWatered;
    private int moistureLevel;
    private String imagePath;



    Plant(long id, String name, String phylogeny, Date birthDate, String notes, Date lastWatered, int moistureLevel) {
        this.id = id;
        this.name = name;
        this.phylogeny = phylogeny;
        this.birthDate = birthDate;
        this.notes = notes;
        this.lastWatered = lastWatered;
        this.moistureLevel = moistureLevel;
    }

    Plant(String name, String phylogeny, Date birthDate, String notes) {
        this.name = name;
        this.phylogeny = phylogeny;
        this.birthDate = birthDate;
        this.notes = notes;
    }

    Plant(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public Date getLastWatered() {
        return lastWatered;
    }

    public int getMoistureLevel() {
        return moistureLevel;
    }

    public String getPhylogeny() {
        return phylogeny;
    }
}
