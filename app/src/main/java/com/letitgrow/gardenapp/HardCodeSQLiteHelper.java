package com.letitgrow.gardenapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ashley on 4/12/2015.
 */
public class HardCodeSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_PLANTS = "PlantData";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLANT = "PLANT";
    public static final String COLUMN_FAVORITE = "FAVORITE";

    private static final int DATABASE_VERSION = 4;

    private static String DB_NAME = "plantdatabase";
    private static String DB_PATH = "/data/data/com.letitgrow.gardenapp/databases/";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_PLANTS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PLANT + " text not null,"
            + COLUMN_FAVORITE + " DEFAULT 'N'"
            +");";

    public HardCodeSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HardCodeSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        onCreate(db);
    }

}