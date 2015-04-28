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
    public static final String COLUMN_NUISANCES = "nuisancePlants";
    public static final String COLUMN_HELPERS = "helperBugs";
    public static final String COLUMN_PESTS = "pestBugs";
    public static final String SPRING_BEG = "springBeg";
    public static final String SPRING_END = "springEnd";
    public static final String FALL_BEG = "fallBeg";
    public static final String FALL_END = "fallEnd";
    public static final String PIC_NAME = "picName";
    public static final String COLUMN_FAVORITE = "favorite";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE_PLANTS + "("
            + COLUMN_ID + " integer primary key, "
            + COLUMN_PLANT + " text not null,"
            + COLUMN_SPACING+ " real,"
            + COLUMN_DEPTH + " real,"
            + COLUMN_COMPANIONS + " text,"
            + COLUMN_NUISANCES + " text,"
            + COLUMN_HELPERS + " text,"
            + COLUMN_PESTS + " text,"
            + SPRING_BEG  + " integer,"
            + SPRING_END + " integer,"
            + FALL_BEG + " integer,"
            + FALL_END + " integer,"
            + PIC_NAME + " text,"
            + COLUMN_FAVORITE + " text DEFAULT 'N'"
            +");";

    private static final String DATABASE_INSERT = "INSERT INTO " +TABLE_PLANTS+"("
            +COLUMN_PLANT+", "+COLUMN_DEPTH+", "+ COLUMN_SPACING
            +", "+COLUMN_COMPANIONS+", "+ COLUMN_NUISANCES+", "+COLUMN_HELPERS+", "+ COLUMN_PESTS
            +", "+SPRING_BEG +", "+ SPRING_END+", "+FALL_BEG+", "+ FALL_END+", "+ PIC_NAME
            +") VALUES"
            +"('Artichoke',NULL , NULL,'', '', '', '',0,0,-12,-5, 'ART'), "
            +"('Asian Greens', 0.5, 12,'', '', '', '',-9,-2,-11,5, 'ACA'), "
            +"('Asparagus', 1.5, 18,'', '', '', '',-9,-2,0,0, 'ASP'), "
            +"('Beans: snap and lima', 1.5, 4,'', '', '', '',2,8,-15,-11, 'BEA'), "
            +"('Beets', 1.0, 2,'', '', '', '',-7,2,-12,-3, 'BEE'), "
            +"('Broccoli', 0.5, 24,'', '', '', '',-7,-2,-12,-4, 'BRO'), "
            +"('Brussel Sprouts', 0.5, 24,'', '', '', '',0,0,-12,-4, 'BRU'), "
            +"('Cabbage', 0.5, 24,'', '', '', '',-7,-2,-12,-4, 'CAB'), "
            +"('Cantaloupe', 1.0, 36,'', '', '', '',3,16,0,0, 'UNK'), " //////
            +"('Carrots', 0.5, 2,'', '', '', '',-8,-1,-12,-3, 'CAR'), "
            +"('Cauliflower', 0.5, 24,'', '', '', '',-8,-2,-12,-4, 'CAU'), "
            +"('Chard: Swiss', 0.5, 48,'', '', '', '',-7,7,-12,-3, 'GRN'), "
            +"('Collards', 0.5, 12,'', '', '', '',-7,-2,-12,-3, 'GRN'), "
            +"('Corn', 0.5, 12,'', '', '', '',1,7,-17,-14, 'COR'), "
            +"('Cucumber', 0.5, 12,'', '', '', '',1,6,-15,-12, 'CUC'), "
            +"('Eggplant', 0.5, 24,'', '', '', '',1,6,-17,-13, 'EGG'), "
            +"('Fava Beans', NULL, NULL,'', '', '', '',-9,-6,-7,-3, 'SPE'), "
            +"('Garlic', 0.5, 4,'', '', '', '',0,0,-9,1, 'GAR'), "
            +"('Greens: cold season', 0.5, 12,'', '', '', '',-9,2,-9,5, 'GRN'), "
            +"('Greens: warm season', 0.5, 12,'', '', '', '',1,21,0,0, 'GRN'), "
            +"('Kale', 0.5, 12,'', '', '', '',-7,-2,-11,1, 'UNK'), "
            +"('Kohlrabi', 0.5, 6,'', '', '', '',-7,-1,-12,-3, 'KOH'), "
            +"('Leeks', NULL, NULL,'', '', '', '',-8,-3,-9,-6, 'LEE'), "
            +"('Lettuce', 0.5, 3,'', '', '', '',-9,1,-9,5, 'GRN'), "
            +"('Mustard', 0.5,12,'', '', '', '',-7,2,-12,-3, 'GRN'), "
            +"('Okra', 1.0, 24,'', '', '', '',4,17,0,0, 'OKR'), "
            +"('Onion: bulbing', 0.5, 3,'', '', '', '',-8,-5,0,0, 'OBL'), "
            +"('Onion: bunching', 0.5, NULL,'', '', '', '',-11,-4,0,0, 'OBN'), "
            +"('Peas: english, snap, snow', 3.0, 1,'', '', '', '',-7,-4,-11,-7, 'PEA'), "
            +"('Peas: southern', 3.0, 36,'', '', '', '',4,17,0,0, 'SPE'), "
            +"('Pepper', 0.5, 24,'', '', '', '',2,9,14,17, 'PEP'), "
            +"('Potatoes: Irish', 4.0, 15,'', '', '', '',-6,-2,-14,-12, 'POT'), "
            +"('Potatoes: sweet', NULL, 16,'', '', '', '',5,15,0,0, 'POS'), "
            +"('Pumpkin', 0.5, 48,'', '', '', '',1,16,0,0, 'PUM'), "
            +"('Radish', 0.5, 1,'', '', '', '',-9,3,-11,5, 'RAD'), "
            +"('Shallots', 0.5, NULL,'', '', '', '',0,0,-9,1, 'OBL'), "
            +"('Spinach', 0.5, 4,'', '', '', '',-9,-2,-9,5, 'SPN'), "
            +"('Squash: summer', 0.5, 36,'', '', '', '',1,7,-17,-11, 'SQS'), "
            +"('Squash: winter', 0.5, 48,'', '', '', '',1,12,-17,-14, 'SQW'), "
            +"('Tomatoes', 0.5, 48,'', '', '', '',1,6,-18,-15, 'TOM'), "
            +"('Turnip', 0.5, 3,'', '', '', '', -7,3,-12,-1, 'TUR'), "
            +"('Watermelon', 0.5, 72,'', '', '', '',4,13,0,0, 'UNK')"
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