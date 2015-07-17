package com.letitgrow.gardenapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashley on 7/5/2015.
 */
public class ZipDataSource {
    // Database fields
    private static SQLiteDatabase database;
    private ZipZoneDBHelper dbHelper;
    private static String[] allColumns = { ZipZoneDBHelper.COLUMN_ID,
            ZipZoneDBHelper.COLUMN_ZIP, ZipZoneDBHelper.COLUMN_ZONE };

    public ZipDataSource(Context context) {
        dbHelper = new ZipZoneDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public static boolean ZipExists(int userInput) {
        Cursor cursor = database.query(ZipZoneDBHelper.TABLE_ZONES, allColumns, ZipZoneDBHelper.COLUMN_ZIP+ " = " + userInput,
                 null, null,null,null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static String GetZone(int userInput) {
        Cursor cursor = database.query(ZipZoneDBHelper.TABLE_ZONES, allColumns, ZipZoneDBHelper.COLUMN_ZIP+ " = " + userInput,
                null, null,null,null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String aZone = cursor.getString(2);
            cursor.close();
            return aZone;
        }
        cursor.close();
        return "not available";
    }

}
