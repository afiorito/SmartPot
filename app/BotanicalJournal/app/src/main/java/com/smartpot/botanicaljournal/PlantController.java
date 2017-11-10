package com.smartpot.botanicaljournal;

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

    PlantController(Context context) {

        this.context = context;
        handler = new DBHandler(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    public ArrayList<Plant> getPlants() {
        return handler.getPlants();
    }

    public void isValidSmartPot(final String potId, final VolleyCallback callback) {
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean createPlant(Plant plant) {
        // Do any checks here

        handler.addPlant(plant);

        return true;
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

    public boolean updateLastWatered(Plant plant, Date date) {

        handler.addLastWateredTimeForPlant(plant, date);

        return true;
    }


}
