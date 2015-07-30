package com.letitgrow.gardenapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.letitgrow.gardenapp.CustomDatePreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ashley on 5/25/2015.
 */
public class SettingsFragment extends PreferenceFragment
 implements SharedPreferences.OnSharedPreferenceChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    //private ZipDataSource datasource;
    //private ZoneDataSource zdatasource;

    ///Location API Variables
    protected boolean mAddressRequested;

    GoogleApiClient mGoogleApiClient;
    //TextView mLatitudeText;
    //TextView mLongitudeText;

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    protected String mAddressOutput;
    // protected TextView mLocationAddressTextView; //
    protected static final String TAG = "main-activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean ZipChanged = false;

        addPreferencesFromResource(R.layout.preferences);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final CustomDatePreference ffd_p = (CustomDatePreference) findPreference("ffd");
        final CustomDatePreference lfd_p = (CustomDatePreference) findPreference("lfd");
        final ZonePickerPreference zpp = (ZonePickerPreference) findPreference("zone");
        ListPreference lp = (ListPreference) findPreference("calc_pref");
        EditTextPreference ep = (EditTextPreference) findPreference("zip");

        if (lp.findIndexOfValue(lp.getValue()) < 0) {
            lp.setValueIndex(0);
            lp.setValue("Let App Decide");
            lp.setSummary(lp.getEntry());
        }

        setDisabledPrefs(Integer.parseInt(lp.getValue()),ffd_p,lfd_p, zpp, ep);


        if (lp.getSummary().toString().equals("How frost dates are set")){
            lp.setSummary(lp.getEntry());
        }

        String ZSP = prefs.getString("zip", "NONE");
        if (!ZSP.equals("NONE")){
            if (!ep.getText().equals(ZSP)){
                ZipChanged = true;
                //ep.setText(ZSP);
            }
        }
        if (ZipChanged){
            ep.setText(ZSP);
           lp.setValueIndex(0);
           lp.setValue("Let App Decide");
           lp.setSummary(lp.getEntry());
           }

        if (ep.getSummary().toString().equals("Zip Code")){
            ep.setSummary(ZSP);
        }


        if (ffd_p.getSummary().toString().equals("Set Frost Dates")){
            DateFormat printFormat = new SimpleDateFormat("MMMM dd");
            Calendar today = Calendar.getInstance();
            String dte = printFormat.format(today.getTime());
            ffd_p.setSummary(dte);
        }

        if (lfd_p.getSummary().toString().equals("Set Frost Dates")){
            DateFormat printFormat = new SimpleDateFormat("MMMM dd");
            Calendar today = Calendar.getInstance();
            String dte = printFormat.format(today.getTime());
            lfd_p.setSummary(dte);
        }

        buildGoogleApiClient();
        mGoogleApiClient.connect();


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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        ListPreference lp = (ListPreference) findPreference("calc_pref");
        CustomDatePreference ffd_p = (CustomDatePreference) findPreference("ffd");
        CustomDatePreference lfd_p = (CustomDatePreference) findPreference("lfd");
        ZonePickerPreference zpp = (ZonePickerPreference) findPreference("zone");
        EditTextPreference ep = (EditTextPreference) findPreference("zip");

        if (key.equals("calc_pref")) {
            lp.setSummary(lp.getEntry());
            int index = Integer.parseInt(lp.getValue());
            setDisabledPrefs(index, ffd_p, lfd_p, zpp, ep);
            if (index == 0) {


                if (mGoogleApiClient.isConnected() && mLastLocation != null) {
                    startIntentService();
                }
                mAddressRequested = true;

           }
        }

        if (key.equals("zip")){
          int aZip = Integer.parseInt(ep.getText());

          ZipDataSource datasource = new ZipDataSource(getActivity());
          datasource.open();

          if (datasource.ZipExists(aZip)){
              //String aZipStr = String.format("%05d", aZip);
              String ZS = String.valueOf(aZip);
              String aZipStr = "00000".substring(ZS.length())+ ZS;

              editor.putString("zip", aZipStr);
              editor.commit();
              ep.setText(aZipStr);
              ep.setSummary(aZipStr);

              String aZone = datasource.GetZone(aZip).toUpperCase();

              zpp.setValueFromZoneText(datasource.GetZone(aZip));
              int value = zpp.getValue();

              editor.putInt("zone", value);
              editor.commit();

          }
          else {
             Toast.makeText(getActivity(), "Invalid Zip", Toast.LENGTH_SHORT).show();
          }

        }

        if (key.equals("zone")) {
          String aZone = zpp.getDisplayValue();

          if (zpp.isEnabled()){
              ep.setSummary("Zip Code");
          }

          ZoneDataSource zdatasource = new ZoneDataSource(getActivity());
          zdatasource.open();

          String aFFD = zdatasource.GetFFD(aZone);
          String aLFD = zdatasource.GetLFD(aZone);

          editor.putString("ffd", aFFD);
          editor.putString("lfd", aLFD);
          editor.commit();

          ffd_p.setDate(aFFD);
          lfd_p.setDate(aLFD);

            DateFormat printFormat = new SimpleDateFormat("MMMM dd");
            String dte = printFormat.format(ffd_p.getDate().getTime());
            ffd_p.setSummary(dte);

            dte = printFormat.format(lfd_p.getDate().getTime());
            lfd_p.setSummary(dte);
        }

        if (key.equals("ffd") || key.equals("lfd")) {
            if (ffd_p.isEnabled()|| lfd_p.isEnabled()){
                ep.setSummary("Zip Code");
                zpp.setSummary("Zone");
            }

        }

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

    ////////////////////////////////Location API Related///////////////
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);  //retrieves last location from API
        if (mLastLocation != null) {

            if (!Geocoder.isPresent()) {
                Toast.makeText(getActivity(), R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }

            String lat = String.valueOf(mLastLocation.getLatitude());
            String lon = String.valueOf(mLastLocation.getLongitude());

            //in case UI tries to get address before API connects
            if (mAddressRequested) {
                startIntentService();
            }

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        //mGoogleApiClient.connect();
    }


    protected synchronized void buildGoogleApiClient() {
        //Connects to GooglePlay API, adds Locations services to API
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void startIntentService() {
        //starts intent JUST for the fetch address service
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);


    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            final String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            final SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            final String allPrefs = prefs.getString("zip", "");

            final EditTextPreference ep = (EditTextPreference) findPreference("zip");


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        /*  SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("zip",mAddressOutput);

                        editor.commit();}*/
                        ep.setText(mAddressOutput);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
                builder.setMessage(mAddressOutput+" has been detected as your zipcode.  Can we use it for your frost dates?")
                        .setTitle(R.string.dialog_title);
                AlertDialog dialog = builder.create();
                dialog.show();




        }
    }

}
