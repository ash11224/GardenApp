package com.letitgrow.gardenapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import android.app.ListActivity;
import android.view.View;
import android.widget.TextView;
//import com.letitgrow.gardenapp.SQLiteDBHelper;


public class MainActivity extends ListActivity {
    private PlantDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HardCodeSQLiteHelper myDbHelper;
        new HardCodeSQLiteHelper(this);
       // myDbHelper = new HardCodeSQLiteHelper(this);

    /*   use this if going back to static DB
     try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch(SQLException sqle){
            try {
                throw sqle;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }*/

        datasource = new PlantDataSource(this);
        datasource.open();
      //  datasource.dropTable();
        datasource.populateTable();

        List<plant> values = datasource.getAllPlants();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        final ArrayAdapter<plant> adapter = new ArrayAdapter<plant>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);


        /* ListView lv = getListView();

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                //String product = ((TextView) view).getText().toString();
                Integer plantID = adapter.getItemId();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), plantDetail.class);
                // sending data to new activity
                i.putExtra("plantID", plantID);
                startActivity(i);

            }
        }); */


    }


    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
