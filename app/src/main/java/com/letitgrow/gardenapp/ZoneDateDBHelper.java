package com.letitgrow.gardenapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ashley on 7/5/2015.
 */
public class ZoneDateDBHelper extends SQLiteOpenHelper {

    private static String ZD_PATH = "/data/data/com.letitgrow.gardenapp/databases/";
    private static String ZD_NAME = "dates.db";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ZONE = "zones";
    public static final String COLUMN_FFD = "ffd";
    public static final String COLUMN_LFD = "lfd";
    public static final String TABLE_DATES = "zonedates";
    private static final int DATABASE_VERSION = 4;

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public ZoneDateDBHelper(Context context) {

        super(context, ZD_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (!dbExist){
            this.getReadableDatabase();

            try {
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDataBase(){

        SQLiteDatabase pathExists = null;

        try{
            String myPath = ZD_PATH + ZD_NAME;
            pathExists = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

        }

        if(pathExists != null){
            pathExists.close();
        }

        return pathExists == null ? false : true;
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(ZD_NAME);
        String myPath = ZD_PATH + ZD_NAME;

        OutputStream myOutput = new FileOutputStream(myPath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = ZD_PATH + ZD_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ZoneDate.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");

        myContext.deleteDatabase(ZD_NAME);

        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}