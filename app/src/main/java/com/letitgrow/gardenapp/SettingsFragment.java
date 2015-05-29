package com.letitgrow.gardenapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.letitgrow.gardenapp.CustomDatePreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ashley on 5/25/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String str;

        addPreferencesFromResource(R.layout.preferences);

        final CustomDatePreference dp = (CustomDatePreference) findPreference("keyname");

        if (dp.getSummary().toString().equals("Set Frost Dates")){
            DateFormat printFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Calendar today = Calendar.getInstance();
            String dte = printFormat.format(today.getTime());
            dp.setSummary(dte);
        }

    }


}
