package com.smartpot.botanicaljournal.Views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.smartpot.botanicaljournal.Helpers.DisplayMode;
import com.smartpot.botanicaljournal.Models.DateAxisValueFormatter;
import com.smartpot.botanicaljournal.Models.GraphData;
import com.smartpot.botanicaljournal.Models.Plant;
import com.smartpot.botanicaljournal.Controllers.PlantController;
import com.smartpot.botanicaljournal.Helpers.PlantViewState;
import com.smartpot.botanicaljournal.R;
import com.smartpot.botanicaljournal.Helpers.ViewHelper;
import com.smartpot.botanicaljournal.Helpers.VolleyCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ManagePlantFragment extends Fragment {

    private PlantController pc;
    private View view;

    // States
    static PlantViewState plantViewState;
    DisplayMode displayMode = DisplayMode.READ_ONLY;
    private boolean moistureGraphState = false;

    // Plant information
    Plant plant;

    // Reference to fields
    private ProfileField plantNameField;
    private ProfileField plantPhylogenyField;
    private ProgressBar progressBar;
    private LineChart moistureGraph;
    private ProfileField lastWateredField;
    private EditText notesEditText;
    private EditText potIdEditText;
    private TextView bDayField;
    private ImageView plantImage;
    private ImageView addImage;
    private ImageButton cancelButton;
    private Button updateLastWateredButton;
    private Button settingsButton;
    private Button deletePlantButton;
    private ProgressBar loadingBar;


    // References to layouts
    Menu menu;
    RelativeLayout moistureLayout;
    RelativeLayout lastWateredLayout;
    RelativeLayout potIdLayout;
    RelativeLayout bDayLayout;
    SwipeRefreshLayout refreshLayout;


    public static ManagePlantFragment newInstance(PlantViewState state) {
        plantViewState = state;
        ManagePlantFragment fragment = new ManagePlantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pc = new PlantController(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_manage_plant, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        setMoistureGraph();
                        int value = pc.getMostRecentMoistureValue(plant);
                        Log.d("ManagePlantFragment", "Progress Bar Value: " + Integer.toString(value));
                        progressBar.setProgress(pc.getMostRecentMoistureValue(plant)); //get latest moisture level
                        refreshLayout.setRefreshing(false);

                    }
                }
        );

        setHasOptionsMenu(true);
        layoutSubviews();
        setDisplayMode();

        return view;
    }

    private void layoutSubviews() {
        // Get References to views
        lastWateredLayout = view.findViewById(R.id.lastWateredLayout);
        moistureLayout = view.findViewById(R.id.moistureLayout);
        moistureLayout.setOnClickListener(setMoistureGraphVisibility);
        potIdLayout = view.findViewById(R.id.potIdLayout);
        bDayLayout = view.findViewById(R.id.bDayLayout);
        plantNameField = view.findViewById(R.id.plantName);
        plantPhylogenyField = view.findViewById(R.id.plantPhylogeny);
        progressBar = view.findViewById(R.id.moistureBar);
        lastWateredField = view.findViewById(R.id.lastWatered);
        notesEditText = view.findViewById(R.id.notesEditText);
        potIdEditText = view.findViewById(R.id.potIdEditText);
        bDayField = view.findViewById(R.id.bDay);
        plantImage = view.findViewById(R.id.plantImage);
        addImage = view.findViewById(R.id.addImage);
        cancelButton = view.findViewById(R.id.cancelButton);
        updateLastWateredButton = view.findViewById(R.id.updateLastWateredButton);
        deletePlantButton = view.findViewById(R.id.deletePlantButton);
        settingsButton = view.findViewById(R.id.settingsButton);
        loadingBar = view.findViewById(R.id.loadingBar);
        loadingBar.setVisibility(View.GONE);

        cancelButton.setOnClickListener(clearDate);
        updateLastWateredButton.setOnClickListener(updateLastWatered);
        settingsButton.setOnClickListener(viewSettings);
        deletePlantButton.setOnClickListener(deletePlant);

        // Set OnClickListener to birthday view
        bDayField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(ManagePlantFragment.this, 0);
                newFragment.show(getActivity().getSupportFragmentManager(),"Date Picker");
            }}
        );

        // Set OnClickListener to Add Image
        plantImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dispatchTakePictureIntent();
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
                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(false);
                lastWateredLayout.setVisibility(View.GONE);
                settingsButton.setVisibility(View.GONE);
                deletePlantButton.setVisibility(View.GONE);

                if(!pc.isNetworkAvailable()) {
                    potIdEditText.setText("No Internet Connection");
                    potIdEditText.setEnabled(false);
                }

                break;

            case EDITPLANT:
                displayMode = DisplayMode.WRITE;

                addImage.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                bDayLayout.setVisibility(View.VISIBLE);
                potIdLayout.setVisibility(View.VISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
                deletePlantButton.setVisibility(View.VISIBLE);

                if(!pc.isNetworkAvailable()) {
                    potIdEditText.setText("No Internet Connection");
                    potIdEditText.setEnabled(false);
                }

                refreshLayout.setRefreshing(false);
                refreshLayout.setEnabled(false);
                if(plant.getPotId().equals("")) moistureLayout.setVisibility(View.GONE);
                else {
                    moistureLayout.setVisibility(View.VISIBLE);
                }

                break;

            case VIEWPLANT:
                displayMode = DisplayMode.READ_ONLY;
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(plant.getName());

                setFieldValues();

                lastWateredLayout.setVisibility(View.VISIBLE);
                potIdLayout.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                settingsButton.setVisibility(View.GONE);
                deletePlantButton.setVisibility(View.GONE);
                if(plant.getBirthDate() == null) bDayLayout.setVisibility(View.GONE);
                else bDayLayout.setVisibility(View.VISIBLE);
                if(plant.getPotId().equals("")) {
                    moistureLayout.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setEnabled(false);
                } else{
                    moistureLayout.setVisibility(View.VISIBLE);
                    refreshLayout.setEnabled(true);
                }

                addImage.setVisibility(View.GONE);

                break;
        }
        setAllFieldModes(displayMode);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_plant_menu, menu);
        this.menu = menu;

        // Since default icon is done icon, sect to icon to edit icon if in read mode
        if (displayMode == displayMode.READ_ONLY)
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit));
    }

    // Changes view mode when change grades button is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_editing:
                switch(plantViewState){
                    case ADDPLANT:
                    case EDITPLANT:
                        final String potId = potIdEditText.getText().toString();
                        loadingBar.setVisibility(View.VISIBLE);
                        pc.isValidSmartPot(potId, new VolleyCallback() {
                            @Override
                            public void onResponse(boolean success) {
                                if(success) plant.setPotId(potId);
                                else {
                                    Toast.makeText(getContext(), "Pot ID is not valid.", Toast.LENGTH_LONG).show();
                                    plant.setPotId("");
                                }
                                loadingBar.setVisibility(View.GONE);
                                performPlantOperation();
                                updateDisplayMode();

                            }
                        });
                        break;

                    case VIEWPLANT:
                        plantViewState = plantViewState.EDITPLANT;
                        updateDisplayMode();
                        break;

                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateDisplayMode() {
        if (displayMode == DisplayMode.WRITE)
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_edit));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_done));

        setDisplayMode();
        setAllFieldModes(displayMode);
    }

    private void performPlantOperation() {
        updatePlantValues();
        switch(plantViewState) {
            case ADDPLANT:
                long id = pc.createPlant(plant);
                if(id != -1) {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.getMenu().getItem(1).setChecked(false);
                    plant.setId(id);
                    plantViewState = plantViewState.VIEWPLANT;
                }
                break;
            case EDITPLANT:
                if (pc.updatePlant(plant)) {
                    plantViewState = plantViewState.VIEWPLANT;
                }
                break;
        }
    }

    private void updatePlantValues() {
        plant.setName(plantNameField.getText());
        plant.setPhylogeny(plantPhylogenyField.getText());
        plant.setNotes(notesEditText.getText().toString());

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

    RelativeLayout.OnClickListener setMoistureGraphVisibility = new RelativeLayout.OnClickListener() {
        @Override
        public void onClick(View v){
            if (moistureGraphState == false) {
                progressBar.setProgress(setMoistureGraph());
                moistureGraph.setVisibility(View.VISIBLE);
            } else moistureGraph.setVisibility(View.GONE);
            moistureGraphState =! moistureGraphState;
        }
    };

    /**
     * Sets up the moisture graph and returns latest moisture value
     */
    private int setMoistureGraph(){
        ArrayList<GraphData> moistureValues = pc.getMoistureLevels(plant); // Get moisture values here
        int nbrValues = moistureValues.size();
        moistureGraph = view.findViewById(R.id.moistureGraph);

        int nbrValuesDisplayed = 5; //nbr values to display
        ArrayList<Entry> entries = new ArrayList<>();

        // Get reference value to use timestamp as a float value
        long reference = moistureValues.get(nbrValues - nbrValuesDisplayed - 1).getDate();
        for (int i= (nbrValues - nbrValuesDisplayed); i < nbrValues ; i++) { // get last values
            // turn data into Entries for graph data
            long date = moistureValues.get(i).getDate() - reference;
            entries.add(new Entry(date, moistureValues.get(i).getValue()));
        }

        // Set Dataset
        LineDataSet dataSet = new LineDataSet(entries, "Moisture Levels"); // add entries to dataset
        dataSet.setDrawValues(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(getResources().getDrawable(R.drawable.graph_gradient));

        // Set Graph with Dataset
        LineData lineData = new LineData(dataSet);
        moistureGraph.setData(lineData);
        moistureGraph.invalidate(); // refresh chart

        //Format Graph
        moistureGraph.getDescription().setEnabled(false);
        moistureGraph.getLegend().setEnabled(false);
        moistureGraph.getAxisRight().setEnabled(false);
        moistureGraph.setScaleEnabled(false);
        moistureGraph.getData().setHighlightEnabled(false);
        moistureGraph.setExtraOffsets(0, 0, 20, 0);

        //Set X-Axis
        XAxis xAxis = moistureGraph.getXAxis();
        xAxis.setTextSize(8);
        xAxis.setTextColor(getResources().getColor(R.color.hintColor));
        //moistureGraph.setVisibleXRangeMaximum(nbrValues); //WISH THIS WORKED
        //moistureGraph.setVisibleXRangeMinimum(nbrValues);
        //moistureGraph.setVisibleXRange(1, 2);
        xAxis.setLabelCount(nbrValuesDisplayed, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        IAxisValueFormatter xAxisFormatter = new DateAxisValueFormatter(new SimpleDateFormat("yy'/'MM'/'dd"), reference); // Set format here if updating at different intervals
        xAxis.setValueFormatter(xAxisFormatter);

        //Set Y-Axis
        moistureGraph.animateY(1000, Easing.EasingOption.EaseOutBack);
        moistureGraph.getAxisLeft().setAxisMaximum(100);
        moistureGraph.getAxisLeft().setAxisMinimum(0);
        moistureGraph.getAxisLeft().setTextColor(getResources().getColor(R.color.hintColor));

        return moistureValues.get(nbrValues -1).getValue();
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
            Date newDate = new Date();
            pc.updateLastWatered(plant, newDate);
            plant.setLastWatered(newDate);
            lastWateredField.setText(ViewHelper.formatLastWateredTime(plant.getLastWatered()));

        }
    };

    Button.OnClickListener viewSettings = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            SettingsFragment settingsFragment = SettingsFragment.newInstance();
            settingsFragment.setPlantId(plant.getId());
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, settingsFragment,"findThisFragment")
                    .addToBackStack(null)
                    .commit();
        }
    };

    Button.OnClickListener deletePlant = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            pc.deletePlant(plant);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
            NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setChecked(true);
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
            plantImage.setClickable(false);
            notesEditText.setFocusableInTouchMode(false);
            notesEditText.setFocusable(false);
        }else{
            bDayField.setClickable(true);
            plantImage.setClickable(true);
            notesEditText.setFocusableInTouchMode(true);
            notesEditText.setFocusable(true);
            notesEditText.clearFocus();
        }
    }

    // Set the values for the profile fields
    private void setFieldValues(){
           //ADD CHECK FOR EMPTY NAME
        plantNameField.setText(plant.getName());
        plantPhylogenyField.setText(plant.getPhylogeny());
        progressBar.setProgress(plant.getMoistureLevel());
        String date = formatDate(plant.getBirthDate());
        bDayField.setText(date.equals("") ? "Enter Birthday" : date);
        lastWateredField.setText(ViewHelper.formatLastWateredTime(plant.getLastWatered()));
        notesEditText.setText(plant.getNotes());
        potIdEditText.setText(plant.getPotId());
        if (!plant.getImagePath().isEmpty()){
            Picasso.with(getContext()).load(new File(plant.getImagePath())).into(plantImage);
        }
        else
            plantImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_plant_profile)); //set flower by default
    }

    public void setPlant(Plant plant){
        this.plant = plant;
    }

    /**
     * Starts camera and saves image
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Called after image was taken from camera
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String imageFileName = "IMG_" + timeStamp;
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            Bundle extras = data.getExtras();
            Bitmap thumbnail = (Bitmap) extras.get("data");
            plantImage.setImageBitmap(thumbnail);
            try {
                File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                File oldImage = new File(plant.getImagePath());
                if(oldImage.exists()) {
                    oldImage.delete();
                }
                FileOutputStream out = new FileOutputStream(imageFile);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                plant.setImagePath(imageFile.getAbsolutePath());
                Log.i("TAG", plant.getImagePath());
            } catch (Exception e) {}

        }
    }

}