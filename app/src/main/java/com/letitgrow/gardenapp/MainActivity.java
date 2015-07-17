package com.letitgrow.gardenapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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


import java.io.IOException;

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
        ListFavorites = false;
        fillData();
        registerForContextMenu(getListView());
        int resourceID = R.layout.preferences;
        PreferenceManager.setDefaultValues(this, resourceID, false);

        //     mResultReceiver = new AddressResultReceiver(new Handler());

        BuildStaticDBs();

        //     mAddressRequested = false;
        //     mAddressOutput = "";
        //    updateValuesFromBundle(savedInstanceState);
        //   buildGoogleApiClient();

        //  if (checkIfGooglePlayServicesAreAvailable()) {
        //Get Access to the google service api
             buildGoogleApiClient();
            mGoogleApiClient.connect();
            if (mGoogleApiClient.isConnected() && mLastLocation != null) {
                startIntentService();
            }
            mAddressRequested = true;
            //  updateUIWidgets();
       // }

        // }
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

    private void createTodo() {
        Intent i = new Intent(this, PlantDetailActivity.class);
        startActivity(i);
    }

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
        ContentValues values = new ContentValues();
        if (on) {
           ListFavorites = true;
        } else {
           ListFavorites = false;
        }

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ListFavorites = data.getBooleanExtra("favNowPushed", false);
            ToggleButton aTglBtn = (ToggleButton) findViewById(R.id.mainToggleButton);
            aTglBtn.setChecked(ListFavorites);
        }

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

        if (ListFavorites){
            SELECTION = PlantDBHelper.COLUMN_FAVORITE+"= 'Y'";
        }
        else SELECTION = null;

        String[] projection = { PlantDBHelper.COLUMN_ID, PlantDBHelper.COLUMN_PLANT };
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, SELECTION, null, null);
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
/*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    } */

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
/*
    private boolean checkIfGooglePlayServicesAreAvailable() {
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            //GooglePlayServicesUtil.getErrorDialog(errorCode, (RecentSightings) this, 0).show();
            return false;
        }
        return true;
    }



    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
               // displayAddressOutput();
            }
        }
    }  */

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            String blah = "blah";

        }
    }
    //////////////////////////////////////////////
}


