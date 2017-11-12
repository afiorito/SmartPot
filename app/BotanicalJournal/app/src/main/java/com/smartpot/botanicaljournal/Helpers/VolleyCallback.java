package com.smartpot.botanicaljournal.Helpers;

import java.util.Date;

/**
 * Created by Anthony on 2017-11-12.
 */

public class VolleyCallback implements VolleyResponse {
    @Override
    public void onResponse(boolean success) {}

    @Override
    public void onResponse(boolean success, String potId, int moistureLevel, Date time) {}
}
