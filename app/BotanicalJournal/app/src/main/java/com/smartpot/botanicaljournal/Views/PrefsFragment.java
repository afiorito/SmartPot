package com.smartpot.botanicaljournal.Views;

/**
 * Created by Jack on 2017-11-25.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.smartpot.botanicaljournal.R;

public class PrefsFragment extends PreferenceFragmentCompat { //creates the preferences/settings page

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      //  super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.settings, rootKey);
    }
}