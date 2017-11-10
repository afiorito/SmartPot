package com.smartpot.botanicaljournal;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.format.DateUtils;
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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

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

                addImage.setVisibility(View.GONE);

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

    private String formatLastWateredTime(Date lastWatered) {
        if(lastWatered == null) return "Never";
        long time = new Date(lastWatered.getTime()).getTime();
        long now = System.currentTimeMillis();

        long difference = now - time;

        CharSequence ago;

        // if difference is greater than a year
        if (difference > 32659200000L)
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

        // if difference is greater than a week
        else if (difference > 604800000L)
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE);

        else
            ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

        return ago.toString();
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
            setFieldValues();
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
        lastWateredField.setText(formatLastWateredTime(plant.getLastWatered()));
        notesEditText.setText(plant.getNotes());
        potIdEditText.setText(plant.getPotId());
        if (!plant.getImagePath().isEmpty()){
            Bitmap bitmap = BitmapFactory.decodeFile(plant.getImagePath());
            plantImage.setImageBitmap(bitmap);
        }
        else
            plantImage.setImageDrawable(getContext().getDrawable(R.drawable.flower)); //set flower by default
    }

    public void setPlant(Plant plant){
        this.plant = plant;
    }


    //_______________________________TO TAKE AND SAVE PLANT PICTURE (move elsewhere?)___________________
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Log.d("MainActivity", "called dispatchTakePictureIntent");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(), "com.smartpot.botanicaljournal.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Called after image was taken from camera
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            addPhotoToGallery();
            Bitmap bitmap = BitmapFactory.decodeFile(plant.getImagePath());
            plantImage.setImageBitmap(bitmap);
        }
    }

    // Create an image file name
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save file path
        String mCurrentPhotoPath = imageFile.getAbsolutePath();
        plant.setImagePath(imageFile.getAbsolutePath());
        Log.d ("YEEEEEE", mCurrentPhotoPath);
        return imageFile;
    }

    //Add the picture to the photo gallery. Must be called on all camera images or they will disappear once taken.
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(plant.getImagePath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


}