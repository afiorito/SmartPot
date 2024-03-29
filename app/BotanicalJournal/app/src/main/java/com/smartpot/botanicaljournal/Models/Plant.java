package com.smartpot.botanicaljournal.Models;

import com.smartpot.botanicaljournal.Helpers.MoistureInterval;

import java.util.Date;

/**
 * Created by MG on 2017-10-21.
 */

public class Plant {

    private long id;
    private String potId = "";
    private String name;
    private String phylogeny;
    private Date birthDate;
    private String notes;
    private Date lastWatered;
    private int moistureLevel;
    private int waterLevel;
    private String imagePath;

    private MoistureInterval moistureInterval;
    private boolean potStatus;


    public Plant() {
        this.imagePath = "";
    }

    public Plant(long id, String name, String phylogeny, Date birthDate,
                 String notes, Date lastWatered, int moistureLevel, int waterLevel, String imagePath, String potId,
                    MoistureInterval moistureInterval, boolean potStatus) {
        this.id = id;
        this.name = name;
        this.phylogeny = phylogeny;
        this.birthDate = birthDate;
        this.notes = notes;
        this.lastWatered = lastWatered;
        this.moistureLevel = moistureLevel;
        this.waterLevel = waterLevel;
        if (potId == null)
            this.potId = "";
        else
            this.potId = potId;
        if (imagePath == null)
            this.imagePath = "";
        else
            this.imagePath = imagePath;
        this.moistureInterval = moistureInterval;
        this.potStatus = potStatus;
    }

    public Plant(String name, String phylogeny, Date birthDate, String notes) {
        this.name = name;
        this.phylogeny = phylogeny;
        this.birthDate = birthDate;
        this.notes = notes;
    }

    public Plant(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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
    public int getWaterLevel() {
        return waterLevel;
    }
    public String getPhylogeny() {
        return phylogeny;
    }
    public String getPotId() {
        return potId;
    }
    public MoistureInterval getMoistureInterval() {return moistureInterval;}
    public boolean isPotStatus() {return potStatus;}

    public void setPotId(String potId) {
        this.potId = potId;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhylogeny(String phylogeny) {
        this.phylogeny = phylogeny;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setLastWatered(Date lastWatered) {
        this.lastWatered = lastWatered;
    }
    public void setMoistureLevel(int moistureLevel) {
        this.moistureLevel = moistureLevel;
    }
    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setMoistureInterval(MoistureInterval moistureInterval) {this.moistureInterval = moistureInterval;}
    public void setPotStatus(boolean potStatus) {this.potStatus = potStatus;}
}
