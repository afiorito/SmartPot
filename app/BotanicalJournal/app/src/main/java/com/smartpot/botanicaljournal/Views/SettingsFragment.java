package com.smartpot.botanicaljournal.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.smartpot.botanicaljournal.Controllers.PlantController;
import com.smartpot.botanicaljournal.R;

public class SettingsFragment extends Fragment {

    View view;
    private PlantController pc;

    long plantId;

    RelativeLayout moistureIntervalLayout;
    TextView moistureIntervalValue;
    Switch potStatusSwitch;
    TextView potStatusValue;

    final CharSequence[] intervals = {"30 minutes", "Hourly", "Daily", "Weekly"};

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        pc = new PlantController(getContext());
        layoutSubviews();

        return view;
    }

    private void layoutSubviews() {

        moistureIntervalLayout = view.findViewById(R.id.moistureIntervalLayout);
        moistureIntervalLayout.setOnClickListener(setMoistureInterval);
        moistureIntervalValue = view.findViewById(R.id.moistureIntervalValue);
        switch(pc.getMoistureInterval(plantId)){
            case THIRTYMINS: moistureIntervalValue.setText(intervals[0]); break;
            case HOURLY: moistureIntervalValue.setText(intervals[1]); break;
            case DAILY: moistureIntervalValue.setText(intervals[2]); break;
            case WEEKLY: moistureIntervalValue.setText(intervals[3]); break;
            default: moistureIntervalValue.setText(intervals[0]); break;
        }
        potStatusSwitch = view.findViewById(R.id.potStatusSwitch);
        potStatusValue = view.findViewById(R.id.potStatusValue);
        if (pc.getPotStatus(plantId)) {
            potStatusSwitch.setChecked(true);
            potStatusValue.setText("On");
        }else{
            potStatusSwitch.setChecked(false);
            potStatusValue.setText("Off");
        }
        potStatusSwitch = view.findViewById(R.id.potStatusSwitch);
        potStatusSwitch.setOnCheckedChangeListener(setSwitch);

    }

    // When the moisture interval layout is clicked, a dialog is created and shown. The selected value is saved in the db.
    RelativeLayout.OnClickListener setMoistureInterval = new RelativeLayout.OnClickListener() {
        @Override
        public void onClick(View v){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // Create dialog to chose moisture interval
            builder.setTitle("Set the Moisture Data Interval");
            builder.setSingleChoiceItems(intervals, 4, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int index) {
                    pc.setMoistureInterval(plantId, index);
                    moistureIntervalValue.setText(intervals[index]);
                }
            });
            builder.create().show();
        }
    };

    // Check the sate of the switch and save it to the db
    CompoundButton.OnCheckedChangeListener setSwitch = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked == true) potStatusValue.setText("On");
            else potStatusValue.setText("Off");
            pc.setPotStatus(plantId, isChecked);
        }
    };

    void setPlantId(long plantId){this.plantId = plantId;}
}