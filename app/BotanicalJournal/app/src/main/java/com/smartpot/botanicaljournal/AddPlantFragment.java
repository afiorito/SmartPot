package com.smartpot.botanicaljournal;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPlantFragment extends Fragment {

    private PlantController pc;

    // States
    static PlantViewState plantViewState;
    DisplayMode displayMode = DisplayMode.READ_ONLY;

    // Plant information
    Plant plant;

    // Reference to fields
    private ProfileField plantNameField;
    private ProfileField plantPhylogenyField;
    private ProgressBar progressBar;
    private ProfileField lastWateredField;
    private EditText notesEditText;
    private EditText potIdEditText;
    private TextView bDayField;
    private ImageView plantImage;
    private ImageView addImage;
    private ImageButton cancelButton;
    private Button updateLastWateredButton;
    private Button deletePlantButton;


    // References to layouts
    Menu menu;
    RelativeLayout moistureLayout;
    RelativeLayout lastWateredLayout;
    RelativeLayout potIdLayout;
    RelativeLayout bDayLayout;


    public static AddPlantFragment newInstance(PlantViewState state) {
        plantViewState = state;
        AddPlantFragment fragment = new AddPlantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pc = new PlantController(getContext());
        Log.i("TAG", plant.getBirthDate() + "");
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
        potIdLayout = view.findViewById(R.id.potIdLayout);
        bDayLayout = view.findViewById(R.id.bDayLayout);

        plantNameField = view.findViewById(R.id.plantName);
        plantPhylogenyField = view.findViewById(R.id.plantPhylogeny);
        progressBar = view.findViewById(R.id.progressBar);
        lastWateredField = view.findViewById(R.id.lastWatered);
        notesEditText = view.findViewById(R.id.notesEditText);
        potIdEditText = view.findViewById(R.id.potIdEditText);
        bDayField = view.findViewById(R.id.bDay);
        plantImage = view.findViewById(R.id.plantImage);
        addImage = view.findViewById(R.id.addImage);
        cancelButton = view.findViewById(R.id.cancelButton);
        updateLastWateredButton = view.findViewById(R.id.updateLastWateredButton);
        deletePlantButton = view.findViewById(R.id.deletePlantButton);

        cancelButton.setOnClickListener(clearDate);
        updateLastWateredButton.setOnClickListener(updateLastWatered);
        deletePlantButton.setOnClickListener(deletePlant);
        // Set OnClickListener to birthday view
        bDayField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddPlantFragment.this, 0);
                newFragment.show(getActivity().getSupportFragmentManager(),"Date Picker");
            }}
        );
    }

    // Decide which plant details to display
    private void setDisplayMode() {
        switch(plantViewState){
            case ADDPLANT:
                displayMode = DisplayMode.WRITE; // Set display mode
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add A Plant"); // Set title of navigation bar

                moistureLayout.setVisibility(View.GONE);
                lastWateredLayout.setVisibility(View.GONE);

                break;

            case EDITPLANT:
                displayMode = DisplayMode.WRITE;
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Editing " + plant.getName());


                addImage.setVisibility(View.VISIBLE);
                bDayLayout.setVisibility(View.VISIBLE);
                potIdLayout.setVisibility(View.VISIBLE);

                if(plant.getPotId().equals("")) moistureLayout.setVisibility(View.GONE);
                else moistureLayout.setVisibility(View.VISIBLE);

                break;

            case VIEWPLANT:
                displayMode = DisplayMode.READ_ONLY;
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(plant.getName());

                setFieldValues();

                lastWateredLayout.setVisibility(View.VISIBLE);
                potIdLayout.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                if(plant.getBirthDate() == null) bDayLayout.setVisibility(View.GONE);
                else bDayLayout.setVisibility(View.VISIBLE);
                if(plant.getPotId().equals("")) moistureLayout.setVisibility(View.GONE);
                else moistureLayout.setVisibility(View.VISIBLE);



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
                        updatePlantValues();
                        if(pc.createPlant(plant)) {
                            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                            navigationView.getMenu().getItem(1).setChecked(false);
                            plantViewState = plantViewState.VIEWPLANT;
                        }
                        break;

                    case EDITPLANT:
                        updatePlantValues();
                        if (pc.updatePlant(plant)) {
                            plantViewState = plantViewState.VIEWPLANT;
                        }
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

    private void updatePlantValues() {
        plant.setName(plantNameField.getText());
        plant.setPhylogeny(plantPhylogenyField.getText());
        plant.setNotes(notesEditText.getText().toString());
        plant.setImagePath("");
        plant.setPotId(potIdEditText.getText().toString());
    }

    public void setDate(Date date) {
        plant.setBirthDate(date);
        bDayField.setText(formatDate(date));

    }

    private String formatDate(Date date) {
        if(date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

            return dateFormat.format(date);
        }

        return "";
    }

    ImageButton.OnClickListener clearDate = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            plant.setBirthDate(null);
            bDayField.setText("Enter Birthday");
        }
    };

    Button.OnClickListener updateLastWatered = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("TAG", "CLick");
        }
    };

    Button.OnClickListener deletePlant = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            pc.deletePlant(plant);
            getActivity().onBackPressed();
        }
    };

    /**
     * Sets all profile fields to the correct mode
     * @param mode the mode to switch to
     */
    private void setAllFieldModes(DisplayMode mode) {
        plantNameField.setDisplayMode(mode);
        plantPhylogenyField.setDisplayMode(mode);
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
        plantPhylogenyField.setText(plant.getPhylogeny());
        progressBar.setProgress(plant.getMoistureLevel());
        String date = formatDate(plant.getBirthDate());
        bDayField.setText(date.equals("") ? "Enter Birthday" : date);
        lastWateredField.setText(plant.getLastWatered() + " ago");
        notesEditText.setText(plant.getNotes());
        potIdEditText.setText(plant.getPotId());

    }

    public void setPlant(Plant plant){
        this.plant = plant;
    }

}