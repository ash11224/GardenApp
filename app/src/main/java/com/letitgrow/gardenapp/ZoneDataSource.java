package com.letitgrow.gardenapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ashley on 7/5/2015.
 */
public class ZoneDataSource {
    // Database fields
    private static SQLiteDatabase database;
    private ZoneDateDBHelper dbHelper;
    private static String[] allColumns = { ZoneDateDBHelper.COLUMN_ID,
            ZoneDateDBHelper.COLUMN_ZONE, ZoneDateDBHelper.COLUMN_FFD,
            ZoneDateDBHelper.COLUMN_LFD };

    public ZoneDataSource(Context context) {
        dbHelper = new ZoneDateDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public static String GetFFD(String userInput) {
        String selection = ZipZoneDBHelper.COLUMN_ZONE+ " = '" + userInput.toUpperCase()+"'";
        Cursor cursor = database.query(ZoneDateDBHelper.TABLE_DATES, allColumns, selection ,
                null, null,null,null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String aDate = cursor.getString(2);
            cursor.close();
            return aDate;
        }
        cursor.close();
        return "not available";
    }

    public static String GetLFD(String userInput) {
        String selection = ZipZoneDBHelper.COLUMN_ZONE+ " = '" + userInput.toUpperCase()+"'";
        Cursor cursor = database.query(ZoneDateDBHelper.TABLE_DATES, allColumns, selection,
                null, null,null,null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String aDate = cursor.getString(3);
            cursor.close();
            return aDate;
        }
        cursor.close();
        return "not available";
    }

}

