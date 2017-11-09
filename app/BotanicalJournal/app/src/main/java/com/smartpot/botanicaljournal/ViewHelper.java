package com.smartpot.botanicaljournal;

/**
 * Created by MG on 2017-11-05.
 */

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

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
}
