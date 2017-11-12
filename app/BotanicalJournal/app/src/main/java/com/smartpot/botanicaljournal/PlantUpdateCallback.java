package com.smartpot.botanicaljournal;
import java.util.Date;

/**
 * Created by Fozail on 2017-11-11.
 */

public interface PlantUpdateCallback {
    void onResponse(boolean success, String potId, int index,  int moistureLevel, Date time);
}
