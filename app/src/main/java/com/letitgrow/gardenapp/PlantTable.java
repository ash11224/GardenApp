package com.letitgrow.gardenapp;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Ashley on 4/18/2015.
 */
public class PlantTable {

    // Database table
    public static final String TABLE_PLANTS = "PlantData";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLANT = "plantName";
    public static final String COLUMN_SPACING = "plantSpacing";
    public static final String COLUMN_DEPTH = "seedDepth";
    public static final String COLUMN_COMPANIONS = "companionPlants";
    public static final String COLUMN_FAVORITE = "favorite";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_PLANTS + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_PLANT + " text not null,"
            + COLUMN_SPACING+ " real,"
            + COLUMN_DEPTH + " real,"
            + COLUMN_COMPANIONS + " text,"
            + COLUMN_FAVORITE + " text DEFAULT 'N'"
            +");";

    private static final String DATABASE_INSERT = "INSERT INTO " +TABLE_PLANTS+
        "("+COLUMN_PLANT+", "+COLUMN_SPACING+", "+ COLUMN_DEPTH+") VALUES"
            +"('Artichoke',NULL , NULL), "
            +"('Asian Greens', 0.5, 12), "
            +"('Asparagus', 1.5, 18), "
            +"('Beans: snap and lima', 1.5, 4), "
            +"('Beets', 1.0, 2), "
            +"('Broccoli', 0.5, 24), "
            +"('Brussel Sprouts', 0.5, 24), "
            +"('Cabbage', 0.5, 24), "
            +"('Cantaloupe', 1.0, 36), "
            +"('Carrots', 0.5, 2), "
            +"('Cauliflower', 0.5, 24), "
            +"('Collards', 0.5, 12), "
            +"('Corn', 0.5, 12), "
            +"('Cucumber', 0.5, 12), "
            +"('Eggplant', 0.5, 24), "
            +"('Fava Beans', NULL, NULL), "
            +"('Garlic', 0.5, 4), "
            +"('Greens: cold season', 0.5, 12), "
            +"('Greens: warm season', 0.5, 12), "
            +"('Kale', 0.5, 12), "
            +"('Kohlrabi', 0.5, 6), "
            +"('Leeks', NULL, NULL), "
            +"('Lettuce', 0.5, 3), "
            +"('Mustard', 0.5,12), "
            +"('Okra', 1.0, 24), "
            +"('Onion: bulbing', 0.5, 3), "
            +"('Onion: bunching', 0.5, NULL), "
            +"('Peas: english, snap, snow', 3.0, 1), "
            +"('Peas: southern', 3.0, 36), "
            +"('Pepper', 0.5, 24), "
            +"('Potatoes: Irish', 4.0, 15), "
            +"('Potatoes: sweet', NULL, 16), "
            +"('Pumpkin', 0.5, 48), "
            +"('Radish', 0.5, 1), "
            +"('Shallots', 0.5, NULL), "
            +"('Spinach', 0.5, 4), "
            +"('Squash: summer', 0.5, 36), "
            +"('Squash: winter', 0.5, 48), "
            +"('Tomatoes', 0.5, 48), "
            +"('Turnip', 0.5, 3), "
            +"('Watermelon', 0.5, 72)"
            +";";

    public static void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_INSERT);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(PlantTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTS);
        onCreate(database);
    }
}