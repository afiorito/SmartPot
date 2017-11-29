package com.smartpot.botanicaljournal.Helpers;

import java.util.Date;

/**
 * Created by Anthony on 2017-11-10.
 */

public interface VolleyResponse {
    void onResponse(boolean success);
    void onResponse(boolean success, String potId, int moistureLevel, int waterLevel, Date time);
}
