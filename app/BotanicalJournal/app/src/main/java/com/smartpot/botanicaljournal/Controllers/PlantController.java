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
import com.smartpot.botanicaljournal.Models.DBHandler;
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
        //        String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/pot1";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://jsonplaceholder.typicode.com/posts/1", null, new Response.Listener<JSONObject>() {

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
                            callback.onResponse(true);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", error.getMessage());

                    }
                });
        requestQueue.add(jsObjRequest);
    }

    public void updatePlant(final int potIndex, final String potId, final VolleyCallback callback)
    {
        // URL to Access API for specific Plant
        // use this in production
        // String url = "https://qrawi86kkd.execute-api.us-east-1.amazonaws.com/prod/smartpot/" + potId;
        String url = "https://jsonplaceholder.typicode.com/posts/1";

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

                                callback.onResponse(true, potId, moistureLevel, time);
                            }
                            else {
                                callback.onResponse(false, potId, -1, null);
                            }
                        }
                        catch (Exception e)
                        {
                            callback.onResponse(false, potId, -1, null);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, error.toString());
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

        handler.addLastWateredTimeForPlant(plant, date);

        return true;
    }


}
