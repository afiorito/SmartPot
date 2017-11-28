package com.smartpot.botanicaljournal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartpot.botanicaljournal.Controllers.PlantController;
import com.smartpot.botanicaljournal.Helpers.VolleyCallback;
import com.smartpot.botanicaljournal.Models.Plant;
import com.smartpot.botanicaljournal.Views.NavigationActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anthony on 2017-11-12.
 */

public class DataRetriever extends BroadcastReceiver {

    RequestQueue requestQueue;
    PlantController pc;

    private void getMoistureValue(final String potId, final VolleyCallback callback) {
        String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/" + potId;

        //Make JSON Request
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log Response from Server
                        Log.i("TAG", response.toString());

                        try {
                            if (response.get("potId").equals(potId)) {
                                int moisture = response.getInt("moisture");
                                callback.onResponse(true, moisture);
                            }
                            else {
                                callback.onResponse(false, -1);
                            }
                        }
                        catch (Exception e) {
                            callback.onResponse(false, -1);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.toString());
                    }
                });

        requestQueue.add(jsObjRequest);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(requestQueue == null) {
            pc = new PlantController(context);
            requestQueue = Volley.newRequestQueue(context);
        }

        final String potId = intent.getStringExtra(NavigationActivity.ALARM_POT_ID);
        final long plantId = intent.getLongExtra(NavigationActivity.ALARM_PLANT_ID, 0);

        getMoistureValue(potId, new VolleyCallback() {
            @Override
            public void onResponse(boolean success, int moisture) {
                pc.addMoistureLevelForPlant(plantId, moisture);
            }
        });

        Log.i("TAG", "Alarm went off for: " + potId);
    }
}
