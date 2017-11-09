package com.smartpot.botanicaljournal;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class PlantFragment extends Fragment {

    private PlantController pc;

    public static PlantFragment newInstance() {
        return new PlantFragment();
    }

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
        ArrayList<Plant> plants = pc.getPlants();
//        plants.add(new Plant(0, "Scammy", "Scammer", new Date(), "", new Date(), 0));

        // Create plant adapter
        PlantAdapter plantAdapter = new PlantAdapter(getContext(), plants);

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
}