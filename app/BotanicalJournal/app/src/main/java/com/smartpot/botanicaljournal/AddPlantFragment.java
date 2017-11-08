package com.smartpot.botanicaljournal;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddPlantFragment extends Fragment {

    // States
    static PlantViewState plantViewState;
    DisplayMode displayMode = DisplayMode.READ_ONLY;

    // Plant information
    Plant plant;

    // Reference to fields
    private ProfileField plantNameField;
    private ProfileField plantTypeField;
    private ProgressBar progressBar;
    private ProfileField lastWateredField;
    private EditText notesEditText;
    private TextView bDayField;
    private ImageView plantImage;
    private ImageView addImage;


    // References to layouts
    Menu menu;
    RelativeLayout moistureLayout;
    RelativeLayout lastWateredLayout;


    public static AddPlantFragment newInstance(PlantViewState state) {
        plantViewState = state;
        AddPlantFragment fragment = new AddPlantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_plant, container, false);

        setHasOptionsMenu(true);
        layoutSubviews(view);
        setDisplayMode();

        return view;
    }

    private void layoutSubviews(View view) {
        // Get References to views
        lastWateredLayout = view.findViewById(R.id.lastWateredLayout);
        moistureLayout = view.findViewById(R.id.moistureLayout);

        plantNameField = view.findViewById(R.id.plantName);
        plantTypeField = view.findViewById(R.id.plantType);
        progressBar = view.findViewById(R.id.progressBar);
        lastWateredField = view.findViewById(R.id.lastWatered);
        notesEditText = view.findViewById(R.id.notesEditText);
        bDayField = view.findViewById(R.id.bDay);
        plantImage = view.findViewById(R.id.plantImage);
        addImage = view.findViewById(R.id.addImage);

        // Set OnClickListener to birthday view
        bDayField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getFragmentManager(),"Date Picker");
            }}
        );
    }

    // Decide which plant details to display
    private void setDisplayMode() {
        switch(plantViewState){
            case ADDPLANT:
                displayMode = DisplayMode.WRITE; // Set display mode
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add A Plant"); // Set title of navigation bar

                setFieldVisibilities(View.GONE); // Hide moisture level and last watered layouts

                break;

            case EDITPLANT:
                displayMode = DisplayMode.WRITE;
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Editing " + plant.getName());

                setFieldVisibilities(View.VISIBLE);

                addImage.setVisibility(View.VISIBLE);

                break;

            case VIEWPLANT:
                displayMode = DisplayMode.READ_ONLY;
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(plant.getName());

                setFieldValues();
                setFieldVisibilities(View.VISIBLE);

                addImage.setVisibility(View.INVISIBLE);

                break;
        }
        setAllFieldModes(displayMode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_plant_menu, menu);
        this.menu = menu;

        // Since default icon is done icon, set to icon to edit icon if in read mode
        if (displayMode == displayMode.READ_ONLY)
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit));
    }

    // Changes view mode when change grades button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_editing:
                switch(plantViewState){
                    case ADDPLANT: //addImage should be gone
                        plantViewState = plantViewState.VIEWPLANT;
                        break;

                    case EDITPLANT:
                        plantViewState = plantViewState.VIEWPLANT;
                        break;

                    case VIEWPLANT:
                        plantViewState = plantViewState.EDITPLANT;
                        break;

                }
                if (displayMode == DisplayMode.WRITE)
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit));
                else
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_done));

                setDisplayMode();
                setAllFieldModes(displayMode);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Sets all profile fields to the correct mode
     * @param mode the mode to switch to
     */
    private void setAllFieldModes(DisplayMode mode) {
        plantNameField.setDisplayMode(mode);
        plantTypeField.setDisplayMode(mode);
        if (displayMode == DisplayMode.READ_ONLY) {
            bDayField.setClickable(false);
            notesEditText.setFocusableInTouchMode(false);
            notesEditText.setFocusable(false);
        }else{
            bDayField.setClickable(true);
            notesEditText.setFocusableInTouchMode(true);
            notesEditText.setFocusable(true);
            notesEditText.clearFocus();
        }
    }

    // Set the values for the profile fields
    private void setFieldValues(){
        plantImage.setImageDrawable(getContext().getDrawable(R.drawable.flower)); //CHANGE THIS TO PLANT IMG

        //ADD CHECK FOR EMPTY NAME
        plantNameField.setText(plant.getName());
        plantTypeField.setText(plant.getType());
        progressBar.setProgress(plant.getMoisture());
        lastWateredField.setText(plant.getLastWatered() + " ago");
        notesEditText.setText(plant.getNotes());
    }

    // Set the visibility of the last watered and moisture layouts
    private void setFieldVisibilities(int visibility){
        lastWateredLayout.setVisibility(visibility);
        moistureLayout.setVisibility(visibility);
    }

    public void setPlant(Plant plant){
        this.plant = plant;
    }

}