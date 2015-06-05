package com.letitgrow.gardenapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.TextView;

import com.letitgrow.gardenapp.CustomDatePreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ashley on 5/25/2015.
 */
public class SettingsFragment extends PreferenceFragment
 implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String str;


        addPreferencesFromResource(R.layout.preferences);
        final CustomDatePreference ffd_p = (CustomDatePreference) findPreference("ffd");
        final CustomDatePreference lfd_p = (CustomDatePreference) findPreference("lfd");

        if (ffd_p.getSummary().toString().equals("Set Frost Dates")){
            DateFormat printFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Calendar today = Calendar.getInstance();
            String dte = printFormat.format(today.getTime());
            ffd_p.setSummary(dte);
        }

        if (lfd_p.getSummary().toString().equals("Set Frost Dates")){
            DateFormat printFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Calendar today = Calendar.getInstance();
            String dte = printFormat.format(today.getTime());
            lfd_p.setSummary(dte);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SwitchPreference sp = (SwitchPreference) findPreference("zone_date_pref");
        CustomDatePreference ffd_p = (CustomDatePreference) findPreference("ffd");
        CustomDatePreference lfd_p = (CustomDatePreference) findPreference("lfd");

        if (key.equals("zone_date_pref")){
           ffd_p.setEnabled(!sp.isChecked());
           lfd_p.setEnabled(!sp.isChecked());

        }
    }
}
