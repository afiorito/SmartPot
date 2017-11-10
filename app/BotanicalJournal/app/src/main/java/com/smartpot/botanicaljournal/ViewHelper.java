package com.smartpot.botanicaljournal;

/**
 * Created by MG on 2017-11-05.
 */

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;

public class ViewHelper {
    public static int dpToPixels(Context ctx, int dp) {
        final float scale = ctx.getResources().getDisplayMetrics().density;

        return (int) (dp * scale + 0.5f);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static String formatLastWateredTime(Date lastWatered) {
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
}
