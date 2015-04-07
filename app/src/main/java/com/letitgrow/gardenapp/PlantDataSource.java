package com.letitgrow.gardenapp; /**
 * Created by Ashley on 4/6/2015.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.letitgrow.gardenapp.plant;

public class PlantDataSource {

        // Database fields
        private SQLiteDatabase database;
        private SQLiteDBHelper dbHelper;
        private String[] allColumns = { SQLiteDBHelper.COLUMN_PLANT,
                SQLiteDBHelper.COLUMN_FAVORITE };

        public PlantDataSource(Context context) {
        dbHelper = new SQLiteDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Cursor queueAll(){
       // String[] columns = new String[]{COLUMN_PLANT, KEY_CONTENT};
        Cursor cursor = database.query(SQLiteDBHelper.TABLE_PLANTS, allColumns,
                null, null, null, null, null);

        return cursor;
    }

}
