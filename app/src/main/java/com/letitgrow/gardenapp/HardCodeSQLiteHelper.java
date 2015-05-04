package com.letitgrow.gardenapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ashley on 4/12/2015.
 */
public class HardCodeSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 24;

    private static String DB_NAME = "plantdatabase";
    //private static String DB_PATH = "/data/data/com.letitgrow.gardenapp/databases/";

    public HardCodeSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        PlantTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PlantTable.onUpgrade(db, oldVersion, newVersion);

    }
}