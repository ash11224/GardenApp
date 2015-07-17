package com.letitgrow.gardenapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Date;

/**
 * Created by Ashley on 5/25/2015.
 */
public class SettingsFragment extends PreferenceFragment
 implements SharedPreferences.OnSharedPreferenceChangeListener {
    //private ZipDataSource datasource;
    //private ZoneDataSource zdatasource;

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

        setDisabledPrefs(Integer.parseInt(lp.getValue()),ffd_p,lfd_p, zpp, ep);


        if (lp.getSummary().toString().equals("How frost dates are set")){
            lp.setSummary(lp.getEntry());
        }

        if (!ep.getText().equals("99999")){
            ep.setSummary(ep.getText());
        }

        String str1 = ffd_p.getDate().toString();
        String str2 = ffd_p.getSummary().toString();
        String str3 = str1;


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
            setDisabledPrefs(Integer.parseInt(lp.getValue()),ffd_p,lfd_p, zpp, ep);
        }

        if (key.equals("zip")){
          int aZip = Integer.parseInt(ep.getText());
          //ep.setSummary(ep.getText());



          ZipDataSource datasource = new ZipDataSource(getActivity());
          datasource.open();

          if (datasource.ZipExists(aZip)){
              ep.setText(String.valueOf(aZip));
              ep.setSummary(String.valueOf(aZip));
              zpp.setValueFromZoneText(datasource.GetZone(aZip));
          }
          else {
             Toast.makeText(getActivity(), "Invalid Zip", Toast.LENGTH_SHORT).show();
          }

        }

        if (key.equals("zone")) {
          String aZone = zpp.getDisplayValue();

          ZoneDataSource zdatasource = new ZoneDataSource(getActivity());
          zdatasource.open();

          String aDate =  zdatasource.GetFFD(aZone);

          ffd_p.setDate(aDate);
          lfd_p.setDate(zdatasource.GetLFD(aZone));

        }

     /*   if (key.equals("ffd")){
          //  String ffd_str = mySharedPreferences.getString("ffd", "");
           // String lfd_str = mySharedPreferences.getString("lfd", "");
            DateFormat printFormat = new SimpleDateFormat("MMMM dd");
            Date aDate = ffd_p.getDate().getTime();
            String dte = printFormat.format(aDate);
            ffd_p.setSummary(dte);

        }

        if (key.equals("lfd")){
            DateFormat printFormat = new SimpleDateFormat("MMMM dd, yyyy");
            Calendar theDate = lfd_p.getDate();
            String dte = printFormat.format(theDate);
            lfd_p.setSummary(dte);

        }*/
    }


    private void adjustPreferences(int selection){

    }
    private void setDisabledPrefs(int num, CustomDatePreference aFFD, CustomDatePreference aLFD,
                                  ZonePickerPreference aZPP, EditTextPreference aEP){
        switch (num) {
            case 0: aFFD.setEnabled(false);
                    aLFD.setEnabled(false);
                    aEP.setEnabled(false);
                    aZPP.setEnabled(false);
                    break;
            case 1: aFFD.setEnabled(false);
                    aLFD.setEnabled(false);
                    aEP.setEnabled(true);
                    aZPP.setEnabled(false);
                    break;
            case 2: aFFD.setEnabled(false);
                    aLFD.setEnabled(false);
                    aEP.setEnabled(false);
                    aZPP.setEnabled(true);
                    break;
            case 3: aFFD.setEnabled(true);
                    aLFD.setEnabled(true);
                    aEP.setEnabled(false);
                    aZPP.setEnabled(false);
                    break;
        }

    }

    //ListPreference lp = (ListPreference) findPreference("calc_pref");

}
