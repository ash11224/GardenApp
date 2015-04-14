package com.letitgrow.gardenapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Ashley on 4/6/2015.
 */

//Using an already existing database rather than hard coding each value in

public class SQLiteDBHelper extends SQLiteOpenHelper{
        //The Android's default system path of your application database.

        public static final String TABLE_PLANTS = "PlantData";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PLANT = "PLANT";
        public static final String COLUMN_FAVORITE = "FAVORITE";

        private static final int DATABASE_VERSION = 1;

        private static String DB_NAME = "plantdata.sqlite";
        private static String DB_PATH = "/data/data/com.letitgrow.gardenapp/databases/";


        private SQLiteDatabase myDataBase;

        private final Context myContext;

        public SQLiteDBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    //empty database to be written over by plant database
        public void createDataBase() throws IOException{

            boolean dbExists = checkDataBase();

            if(dbExists){
                //do nothing - database already exist
            }else{

                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }

            this.close();

        }

        private boolean checkDataBase(){
            boolean dbExists = false;
            SQLiteDatabase checkDB = null;
            try{
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            }catch(SQLiteException e){
                //database does't exist yet.
            }
            if(checkDB != null){
                dbExists = true;
                checkDB.close();
            }else dbExists = false;

            return dbExists; //checkDB != null ? true : false;
        }

        //copies from asset folder to new database to be accessed
        private void copyDataBase() throws IOException{

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            boolean initialiseDatabase = (new File(outFileName)).exists();

            if (initialiseDatabase == false) {
                //Open local db as the input stream
                InputStream myInput = myContext.getAssets().open(DB_NAME);



                //Open the empty db as the output stream
                OutputStream myOutput = new FileOutputStream(outFileName); //

                //transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];//
                int length;//
                while ((length = myInput.read(buffer)) > 0) {//
                    myOutput.write(buffer, 0, length); //
                }

                //Close the streams
                myOutput.flush(); //
                myOutput.close(); //
                myInput.close();  //
            }
        }

        public void openDataBase() throws SQLException{
            //Open the database
            String myPath = DB_PATH + DB_NAME;
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


        }

        // Add your public helper methods to access and get content from the database.
        // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
        // to you to create adapters for your views.

}