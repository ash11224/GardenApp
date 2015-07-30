package com.letitgrow.gardenapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import android.app.ListActivity;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.location.LocationServices;
import com.letitgrow.gardenapp.ZonePickerPreference;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ListActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int RESULT_SETTINGS = 1;
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;
    boolean ListFavorites;

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

  //  protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
  //  protected static final String LOCATION_ADDRESS_KEY = "location-address";
    ///////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_list);

        this.getListView().setDividerHeight(2);
        BuildStaticDBs();

        fillData();
        registerForContextMenu(getListView());
        int resourceID = R.layout.preferences;
        PreferenceManager.setDefaultValues(this, resourceID, false);

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        ListFavorites = prefs.getBoolean("ShowFavs", false);

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("zip")){

                    int aZip = Integer.parseInt(prefs.getString("zip", ""));
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("calc_pref","0");
                    ZipDataSource datasource = new ZipDataSource(MainActivity.this);
                    datasource.open();

                    if (datasource.ZipExists(aZip)){
                        String aZone = datasource.GetZone(aZip).toUpperCase();

                        List<String> myZones = null;

                        if (myZones == null) {
                            myZones = Arrays.asList((getResources().getStringArray(R.array.zoneArray)));
                        }
                        int value = myZones.indexOf(aZone);

                        editor.putInt("zone",value);

                        ZoneDataSource zdatasource = new ZoneDataSource(MainActivity.this);
                        zdatasource.open();

                        String aFFD = zdatasource.GetFFD(aZone);
                        String aLFD = zdatasource.GetLFD(aZone);

                        editor.putString("ffd",aFFD);
                        editor.putString("lfd",aLFD);

                        editor.commit();

                    }

                    
                }
            }
        };

        prefs.registerOnSharedPreferenceChangeListener(listener);




        buildGoogleApiClient();
        mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
          startIntentService();
        }
        mAddressRequested = true;

    }

    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  MenuInflater inflater = getMenuInflater();
      //  inflater.inflate(R.menu.menu_settings, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingsActivity.class);
        startActivityForResult(intent, 0);

        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/"
                        + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

  /*  private void createTodo() {
        Intent i = new Intent(this, PlantDetailActivity.class);
        startActivity(i);
    }*/

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, PlantDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE, todoUri);

        startActivity(i);
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { PlantDBHelper.COLUMN_PLANT };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.plant_row, null, from,
                to, 0);

        setListAdapter(adapter);
    }

    private void BuildStaticDBs(){

        ZipZoneDBHelper myZZHelper;
        myZZHelper = new ZipZoneDBHelper(this);

        try {
            myZZHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            myZZHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        ZoneDateDBHelper myZDHelper;
        myZDHelper = new ZoneDateDBHelper(this);

        try {
            myZDHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            myZDHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }
        PlantDBHelper myPDHelper;
        myPDHelper = new PlantDBHelper(this);

        try {
            myPDHelper.createDataBase();
        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }
        try {
            myPDHelper.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }




    }

    public void onPlantNowClicked(View view){

        Intent i = new Intent(this, PlantNowActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/");
        i.putExtra(MyContentProvider.CONTENT_TYPE, todoUri);
        i.putExtra("favPushed", ListFavorites);
        startActivityForResult(i, Activity.RESULT_OK);
    }

    public void onMainToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        SharedPreferences.Editor editor = prefs.edit();

        ContentValues values = new ContentValues();
        if (on) {
            ListFavorites = true;

        } else {
            ListFavorites = false;
        }
        editor.putBoolean("ShowFavs", ListFavorites);
        editor.commit();

        getLoaderManager().restartLoader(0, null, this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String SELECTION;

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        ListFavorites = prefs.getBoolean("ShowFavs", false);

        ToggleButton aTglBtn = (ToggleButton) findViewById(R.id.mainToggleButton);
        aTglBtn.setChecked(ListFavorites);

        if (ListFavorites){
            SELECTION = PlantDBHelper.COLUMN_FAVORITE+"= 'Y'";
        }
        else SELECTION = null;

        String[] projection = { PlantDBHelper.COLUMN_ID, PlantDBHelper.COLUMN_PLANT };

        String SORT = PlantDBHelper.COLUMN_PLANT;
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, SELECTION, null, SORT);
        return cursorLoader;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
         public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        // data is not available anymore, delete reference
        getLoaderManager().restartLoader(0, null, this);
    }
    @Override
    protected void onStop() {
        Log.w(TAG, "App stopped");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG, "App destoryed");

        super.onDestroy();
    }
    ////////////////////////////////Location API Related///////////////
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);  //retrieves last location from API
        if (mLastLocation != null) {

            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
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
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void startIntentService() {
        //starts intent JUST for the fetch address service
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            final String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            final SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            final String allPrefs = prefs.getString("zip", "");

            if (!allPrefs.equals(mAddressOutput)){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("zip",mAddressOutput);

                        editor.commit();
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
    //////////////////////////////////////////////
}


