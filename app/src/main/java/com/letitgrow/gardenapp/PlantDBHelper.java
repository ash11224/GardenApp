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
 * Created by Ashley on 7/8/2015.
 */
public class PlantDBHelper extends SQLiteOpenHelper {

    private static String PD_PATH = "/data/data/com.letitgrow.gardenapp/databases/";
    private static String PD_NAME = "plants6.db";

    public static final String TABLE_PLANTS = "PlantDB";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLANT = "plantName";
    public static final String COLUMN_SPACING = "plantSpacing";
    public static final String COLUMN_DEPTH = "seedDepth";
    public static final String DAYS_MATURITY = "daysToMaturity";
    public static final String COLUMN_COMPANIONS = "companionPlants";
    public static final String COLUMN_NUISANCES = "nuisancePlants";
    public static final String COLUMN_HELPERS = "helperBugs";
    public static final String COLUMN_PESTS = "pestBugs";
    public static final String SPRING_BEG = "springBeg";
    public static final String SPRING_END = "springEnd";
    public static final String FALL_BEG = "fallBeg";
    public static final String FALL_END = "fallEnd";
    public static final String PIC_NAME = "picName";
    public static final String COLUMN_FAVORITE = "favorite";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public PlantDBHelper(Context context) {

        super(context, PD_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {
        boolean dbPathExist = checkDataBase();

        if (!dbPathExist){
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
            String myPath = PD_PATH + PD_NAME;
            pathExists = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }catch(SQLiteException e){

        }

        if(pathExists != null){
            pathExists.close();
        }

        return pathExists == null ? false : true;
    }

    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(PD_NAME);
        String myPath = PD_PATH + PD_NAME;

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
        String myPath = PD_PATH + PD_NAME;
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
        Log.w(plant.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");


    myContext.deleteDatabase(PD_NAME);

        try {
            createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}