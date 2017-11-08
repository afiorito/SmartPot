package com.smartpot.botanicaljournal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MG on 2017-11-05.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Log.d("DatePickerFragment", Integer.toString(month));
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
    }


    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Set date text view to plant's birthday

        String date = Integer.toString(year)+ "-" + Integer.toString((month +1)) + "-" +Integer.toString(day);
        Log.d("DatePickerFragment", "date = " + date);

        SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat writeFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

        try {
            date = writeFormat.format(readFormat.parse(date));
        }catch(ParseException e){
            e.printStackTrace();
        }

        Log.v("DatePickerFragment",date);

        TextView bDay = (TextView) getActivity().findViewById(R.id.bDay);
        bDay.setText(date);
        Log.v("DatePickerFragment", "Converted the date format");
    }

}