package com.smartpot.botanicaljournal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class PlantFragment extends Fragment {
    public static PlantFragment newInstance() {
        PlantFragment fragment = new PlantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant, container, false);

        // Create temporary plants
        ArrayList<Plant> plants = new ArrayList<>();
        for (int i = 1; i <= 5 ; i++){
            plants.add(new Plant("Plant " + i));
        }

        //Create plant adapter
        PlantAdapter plantAdapter = new PlantAdapter(getContext(), plants);

        //Get reference to ListView
        ListView plantView = view.findViewById(R.id.plantListView);
        LinearLayout noPlantsView =  view.findViewById(R.id.noPlantsView);

        //Set ListView layout
        plantView.setEmptyView(noPlantsView); // Set default layout when list is empty
        plantView.setAdapter(plantAdapter);

        // Replace main fragment with add plant fragment when button is clicked
        Button addPlantButton = (Button) view.findViewById(R.id.addPlantButton);
        addPlantButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        AddPlantFragment nextFrag= new AddPlantFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, nextFrag,"findThisFragment")
                                .addToBackStack(null)
                                .commit();

                    }
                }
        );

        return view;
    }
}