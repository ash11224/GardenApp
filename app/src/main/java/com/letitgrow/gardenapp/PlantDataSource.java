package com.letitgrow.gardenapp; /**
 * Created by Ashley on 4/6/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PlantDataSource {

    // Database fields
    private SQLiteDatabase database;
    private HardCodeSQLiteHelper dbHelper;
    private String[] allColumns = { HardCodeSQLiteHelper.COLUMN_ID
            ,HardCodeSQLiteHelper.COLUMN_PLANT
            ,HardCodeSQLiteHelper.COLUMN_FAVORITE
            };

    private static final String DATABASE_POPULATE =
            "INSERT OR REPLACE INTO "+HardCodeSQLiteHelper.TABLE_PLANTS+"("+HardCodeSQLiteHelper.COLUMN_PLANT+") VALUES"+
            "('Artichoke'),('Asian greens'),('Asparagus'),('Beans: bush'),('Beans: pole'),('Beets');"
            ;
    private static final String DATABASE_DROP = "DROP TABLE IF EXISTS "+HardCodeSQLiteHelper.TABLE_PLANTS;

    public PlantDataSource(Context context) {
        dbHelper = new HardCodeSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

      public void close() {
        dbHelper.close();
    }

    public void populateTable() {
        database.execSQL(DATABASE_POPULATE);
    }

  /*  public void dropTable() {
        database.execSQL(DATABASE_DROP);
    }*/

    public List<plant> getAllPlants() {
        List<plant> plantList = new ArrayList<plant>();

        Cursor cursor = database.query(HardCodeSQLiteHelper.TABLE_PLANTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            plant localPlant = cursorToPlant(cursor);
            plantList.add(localPlant);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return plantList;
    }

    private plant cursorToPlant(Cursor cursor) {
        plant localPlant = new plant();
        localPlant.setId(cursor.getLong(0));
        localPlant.setPlantName(cursor.getString(1));
        return localPlant;
    }



}
