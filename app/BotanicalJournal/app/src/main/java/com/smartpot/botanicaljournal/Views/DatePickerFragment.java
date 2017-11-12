package com.smartpot.botanicaljournal.Views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.smartpot.botanicaljournal.Views.ManagePlantFragment;

import java.util.Calendar;

/**
 * Created by MG on 2017-11-05.
 */

public class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener {

    final Calendar c = Calendar.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Log.d("DatePickerFragment", Integer.toString(month));
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Set date text view to plant's birthday

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        ManagePlantFragment plantFragment = ((ManagePlantFragment) getTargetFragment());
        plantFragment.setDate(c.getTime());
    }

}