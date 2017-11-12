package com.smartpot.botanicaljournal;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class PlantFragment extends Fragment {

    protected static final String TAG = "Plant Fragment";

    private PlantController pc;

    private ArrayList<Plant> plants;

    PlantAdapter plantAdapter = null;

    public static PlantFragment newInstance() {
        return new PlantFragment();
    }

    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant, container, false);

        // Initialize Database Controller
        pc = new PlantController(getContext());

        // Load plants from the database
        plants = pc.getPlants();
//        plants.add(new Plant(0, "Scammy", "Scammer", new Date(), "", new Date(), 0));

        // Create plant adapter
        plantAdapter = new PlantAdapter(getContext(), plants);

        updatePlants();

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("TAG", "onRefresh called from SwipeRefreshLayout");

                        // Update plant data here
                        refreshLayout.setRefreshing(false);
                    }
                }
        );

        //Get reference to ListView
        ListView plantView = view.findViewById(R.id.plantListView);
        LinearLayout noPlantsView =  view.findViewById(R.id.noPlantsView);

        //Set ListView layout
        plantView.setEmptyView(noPlantsView); // Set default layout when list is empty
        plantView.setAdapter(plantAdapter);

        //Set OnClickListener to ListView Item
        plantView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id){
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.getMenu().getItem(0).setChecked(false);
                Plant plant = (Plant)parent.getAdapter().getItem(position);
                AddPlantFragment addPlantFragment= AddPlantFragment.newInstance(PlantViewState.VIEWPLANT);
                addPlantFragment.setPlant(plant);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, addPlantFragment).addToBackStack(null).commit();
            }
        });

        // Replace main fragment with add plant fragment when button is clicked
        Button addPlantButton = view.findViewById(R.id.addPlantButton);
        addPlantButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().getItem(1).setChecked(true);
                        AddPlantFragment addPlantFragment = AddPlantFragment.newInstance(PlantViewState.ADDPLANT);
                        addPlantFragment.setPlant(new Plant());
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, addPlantFragment).addToBackStack(null).commit();
                    }
                }
        );

        return view;
    }


    protected void updatePlants()
    {

        for(int i =0; i < plants.size(); i++)
        {
            //What Plant to update in Array
            int plantIndex = i;

            //Pot ID
            String tempPotId = plants.get(i).getPotId();

            //If potID is not null, make request to Server
            if (tempPotId != "")
            {
                pc.updatePlant(plantIndex, tempPotId, new PlantUpdateCallback() {
                    @Override
                    public void onResponse(boolean success, String potId, int index, int moistureLevel, Date timeStamp)
                    {
                        if (success)
                        {
                            plants.get(index).setMoistureLevel(moistureLevel);
                            plants.get(index).setLastWatered(timeStamp);
                            plantAdapter.notifyDataSetChanged();
                        }
                        else
                            {
                            Log.e(TAG, "Couldn't get Data for: " + potId);
                        }
                    }
                });
            }

        }
    }


}