package com.smartpot.botanicaljournal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Anthony on 2017-11-12.
 */

public class DataRetriever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "Alarm went off");
    }
}
