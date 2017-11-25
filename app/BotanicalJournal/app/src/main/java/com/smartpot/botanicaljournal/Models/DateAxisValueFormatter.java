package com.smartpot.botanicaljournal.Models;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MG on 2017-11-20.
 */

public class DateAxisValueFormatter implements IAxisValueFormatter {
    private long referenceTimestamp; // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;

    public DateAxisValueFormatter(DateFormat dateFormat, long referenceTimestamp) {
        Log.d("DateAxisValueFormatter", "Called DateAxisFormatter");
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = dateFormat;
        this.mDate = new Date();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
        long convertedTimestamp = (long) value;
        Log.d("DateAxisValueFormatter", "Called getFormattedValue with Value: " + Long.toString(convertedTimestamp));
        // Retrieve original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        Log.d("DateAxisValueFormatter", "Original Timestamp: " + Long.toString(originalTimestamp));

        // Convert timestamp to format
        mDate = new Date(originalTimestamp);
        Log.d("DateAxisValueFormatter", "FormattedDate: " + (mDataFormat.format(mDate)));
        return mDataFormat.format(mDate);
    }

}
