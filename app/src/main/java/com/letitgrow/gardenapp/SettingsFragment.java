package com.letitgrow.gardenapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.TextView;
import android.widget.Toast;

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
        final ZonePickerPreference zpp = (ZonePickerPreference) findPreference("zone");
        ListPreference lp = (ListPreference) findPreference("calc_pref");
        EditTextPreference ep = (EditTextPreference) findPreference("zip");

        if (lp.findIndexOfValue(lp.getValue()) < 0) {
            lp.setValueIndex(0);
            lp.setValue("Let App Decide");
        }

        switch (Integer.parseInt(lp.getValue())) {
             case 0: ffd_p.setEnabled(false);
                   lfd_p.setEnabled(false);
                    ep.setEnabled(false);
                    zpp.setEnabled(false);
                 break;
             case 1: ffd_p.setEnabled(false);
                 lfd_p.setEnabled(false);
                 ep.setEnabled(true);
                    zpp.setEnabled(false);
                 break;
                case 2: ffd_p.setEnabled(false);
                    lfd_p.setEnabled(false);
                    ep.setEnabled(false);
                    zpp.setEnabled(true);
                    break;
                case 3: ffd_p.setEnabled(true);
                    lfd_p.setEnabled(true);
                    ep.setEnabled(false);
                    zpp.setEnabled(false);
                    break;

            }

        if (lp.getSummary().toString().equals("How frost dates are set")){
            lp.setSummary(lp.getEntry());
        }


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
        //super.onResume();
        // Set up a listener whenever a key changes

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        //super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ListPreference lp = (ListPreference) findPreference("calc_pref");
        CustomDatePreference ffd_p = (CustomDatePreference) findPreference("ffd");
        CustomDatePreference lfd_p = (CustomDatePreference) findPreference("lfd");
        ZonePickerPreference zpp = (ZonePickerPreference) findPreference("zone");
        EditTextPreference ep = (EditTextPreference) findPreference("zip");

        if (key.equals("calc_pref")) {
            lp.setSummary(lp.getEntry());
            switch (Integer.parseInt(lp.getValue())) {
                case 0: ffd_p.setEnabled(false);
                    lfd_p.setEnabled(false);
                    ep.setEnabled(false);
                    zpp.setEnabled(false);
                    break;
                case 1: ffd_p.setEnabled(false);
                    lfd_p.setEnabled(false);
                    ep.setEnabled(true);
                    zpp.setEnabled(false);
                    break;
                case 2: ffd_p.setEnabled(false);
                    lfd_p.setEnabled(false);
                    ep.setEnabled(false);
                    zpp.setEnabled(true);
                    break;
                case 3: ffd_p.setEnabled(true);
                    lfd_p.setEnabled(true);
                    ep.setEnabled(false);
                    zpp.setEnabled(false);
                    break;

            }
        }

        if (key.equals("zip")){
            int aZip = Integer.parseInt(ep.getText());
            ep.setSummary(ep.getText());
            switch (aZip){
                case 88888:
                    zpp.setValue(14);
                    break;
                case 77777:
                    zpp.setValue(12);
                    break;
                case 66666:
                    //Toast toast = Toast.makeText(this, "Invalid Zip", Toast.LENGTH_SHORT);
                    //toast.show();
                    break;

            }

        }

        if (key.equals("zone")) {
            switch (zpp.getValue()) {
                case 0: case 1:
                    ffd_p.setDate("2015.07.15");
                    lfd_p.setDate("2015.06.15");
                    break;
                case 2: case 3: case 4: case 5:
                    ffd_p.setDate("2015.08.15");
                    lfd_p.setDate("2015.05.15");
                    break;
                case 6: case 7:
                    ffd_p.setDate("2015.09.15");
                    lfd_p.setDate("2015.05.15");
                    break;
                case 8: case 9: case 10:
                case 11: case 12: case 13:
                    ffd_p.setDate("2015.10.15");
                    lfd_p.setDate("2015.04.15");
                    break;
                case 14: case 15:
                    ffd_p.setDate("2015.11.15");
                    lfd_p.setDate("2015.03.15");
                    break;
                case 16: case 17:
                    ffd_p.setDate("2015.12.15");
                    lfd_p.setDate("2015.02.15");
                    break;
                case 18: case 19:
                    ffd_p.setDate("2015.12.15");
                    lfd_p.setDate("2015.01.31");
                    break;
            }
        }
    }

    private void adjustPreferences(int selection){

    }
}
