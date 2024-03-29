package com.smartpot.botanicaljournal.Controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.smartpot.botanicaljournal.Helpers.MoistureInterval;
import com.smartpot.botanicaljournal.Models.DBHandler;
import com.smartpot.botanicaljournal.Models.Data;
import com.smartpot.botanicaljournal.Models.Plant;
import com.smartpot.botanicaljournal.Helpers.VolleyCallback;
import com.smartpot.botanicaljournal.Helpers.VolleyResponse;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anthony on 2017-11-08.
 */

public class PlantController {
    private final DBHandler handler;
    RequestQueue requestQueue;
    Context context;

    protected static final String TAG = "PlantController";

    public PlantController(Context context) {

        this.context = context;
        handler = new DBHandler(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    public ArrayList<Plant> getPlants() {
        return handler.getPlants();
    }

    public void isValidSmartPot(final String potId, final VolleyResponse callback) {
        // use for production, don't use this url for testing
        String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/" + potId;
        if(!isNetworkAvailable()) {
            callback.onResponse(false);
            return;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TAG", response.toString());
                        try {
                            if(response.get("potId").equals(potId)) {
                                callback.onResponse(true);
                            } else {
                                callback.onResponse(false);
                            }
                        } catch (Exception e) {
                            // change this to true if you want potId to be valid
                            callback.onResponse(false);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onResponse(false);
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    public void updatePlant(final String potId, final VolleyCallback callback) {
        // URL to Access API for specific Plant
        // use this in production
        String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/" + potId;

        //Make JSON Request
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //Log Response from Server
                        Log.i(TAG, response.toString());

                        try {
                            if (response.get("potId").equals(potId)) {
                                //Get UNIX Timestamp from String (Assume API sends proper Data) as it doesn't parse
                                Date time = new Date(Long.valueOf(response.getString("lastWatered")));
                                //Get the Moisture Level
                                int moistureLevel = response.getInt("moisture");
                                int waterLevel = response.getInt("waterLevel");

                                callback.onResponse(true, potId, moistureLevel, waterLevel, time);
                            }
                            else {
                                callback.onResponse(false, potId, -1, -1, null);
                            }
                        }
                        catch (Exception e)
                        {
                            callback.onResponse(false, potId, -1, -1, null);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        requestQueue.add(jsObjRequest);
    }

    private void toggleSmartPot(String potId, int running) {
        String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/" + potId + "/running";

        final JSONObject body = new JSONObject();
        try {
            body.put("running", running);
        } catch(Exception e) {

        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, body, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", error.getMessage());

                    }
                });
        requestQueue.add(jsObjRequest);
    }

    private void updateWatering(String potId) {
        String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/" + potId + "/watering";

        final JSONObject body = new JSONObject();
        try {
            body.put("watering", 1);
        } catch(Exception e) {

        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, url, body, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", error.getMessage());

                    }
                });
        requestQueue.add(jsObjRequest);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public long createPlant(Plant plant) {
        // Do any checks here

        long id = handler.addPlant(plant);

        return id;
    }

    public boolean updatePlant(Plant plant) {
        // do any checks here

        handler.updatePlant(plant);

        return true;
    }

    public boolean deletePlant(Plant plant) {

        File image = new File(plant.getImagePath());
        if(image.exists()) {
            image.delete();
        }
        handler.deletePlant(plant.getId());

        return true;
    }

    public long getPlantCount() {
        return handler.countPlants();
    }

    public boolean updateLastWatered(Plant plant, Date date) {
        if(!plant.getPotId().equals("")) {
            updateWatering(plant.getPotId());
        } else {
            handler.addLastWateredTimeForPlant(plant, date);
        }
        return true;
    }

    public ArrayList<Data> getMoistureLevels(Plant plant) {
        return handler.getMoistureLevels(plant.getId());
    }

    public int getMostRecentMoistureValue(Plant plant) {
        return handler.getMostRecentMoistureValue(plant.getId());
    }

    public void setMoistureInterval(long id, int interval) {
        handler.setMoistureInterval(id, interval);
    }

    public MoistureInterval getMoistureInterval(long id) {
        return handler.getMoistureInterval(id);
    }

    public void addMoistureLevelForPlant(long plantId, int value) {
        handler.addMoistureLevelForPlant(new Plant(plantId), value);
    }


    public void setPotStatus(Plant p, int status) {
        toggleSmartPot(p.getPotId(), status);
        handler.setPotStatus(p.getId(), status);
    }

    public boolean getPotStatus(long id) {
        return handler.getPotStatus(id);
    }
}
