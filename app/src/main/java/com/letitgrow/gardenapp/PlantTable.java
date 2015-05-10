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
            +"('Artichoke',NULL , NULL,'', '', '', '',-9,-1,-10,-4, 'ART'), "
            +"('Asian Greens', 0.5, 12,'', '', '', '',-25,-1,-11,17, 'ACA'), "
            +"('Asparagus', 1.5, 18,'', '', '', '',-9,-1,0,0, 'ASP'), "
            +"('Beans: snap and lima', 1.5, 4,'', '', '', '',1,8,-14,-10, 'BEA'), "
            +"('Beets', 1.0, 2,'', '', '', '',-7,2,-12,-2, 'BEE'), "
            +"('Broccoli', 0.5, 24,'', '', '', '',-7,-1,-12,-3, 'BRO'), "
            +"('Brussel Sprouts', 0.5, 24,'', '', '', '',0,0,-12,-3, 'BRU'), "
            +"('Cabbage', 0.5, 24,'', '', '', '',-7,-1,-12,-3, 'CAB'), "
            +"('Cantaloupe', 1.0, 36,'', '', '', '',2,14,0,0, 'UNK'), " //////
            +"('Carrots', 0.5, 2,'', '', '', '',-7,0,-12,-2, 'CAR'), "
            +"('Cauliflower', 0.5, 24,'', '', '', '',-7,-1,-12,-3, 'CAU'), "
            +"('Chard: Swiss', 0.5, 48,'', '', '', '',-7,7,-12,-2, 'GRN'), "
            +"('Collards', 0.5, 12,'', '', '', '',-7,-1,-12,-2, 'GRN'), "
            +"('Corn', 0.5, 12,'', '', '', '',0,7,-17,-13, 'COR'), "
            +"('Cucumber', 0.5, 12,'', '', '', '',0,6,-15,-11, 'CUC'), "
            +"('Eggplant', 0.5, 24,'', '', '', '',0,6,-17,-13, 'EGG'), "
            +"('Fava Beans', NULL, NULL,'', '', '', '',-9,-5,-7,-2, 'SPE'), "
            +"('Garlic', 0.5, 4,'', '', '', '',0,0,-9,1, 'GAR'), "
            +"('Greens: cold season', 0.5, 12,'', '', '', '',-22,2,-9,21, 'GRN'), "
            +"('Greens: warm season', 0.5, 12,'', '', '', '',0,21,0,0, 'GRN'), "
            +"('Kale', 0.5, 12,'', '', '', '',-7,-1,-11,1, 'UNK'), "
            +"('Kohlrabi', 0.5, 6,'', '', '', '',-7,0,-12,-2, 'KOH'), "
            +"('Leeks', NULL, NULL,'', '', '', '',-8,-2,-11,-3, 'LEE'), "
            +"('Lettuce', 0.5, 3,'', '', '', '',-23,1,-9,20, 'GRN'), "
            +"('Mustard', 0.5,12,'', '', '', '',-7,2,-12,-2, 'GRN'), "
            +"('Okra', 1.0, 24,'', '', '', '',3,17,0,0, 'OKR'), "
            +"('Onion: bulbing', 0.5, 3,'', '', '', '',-8,-4,0,0, 'OBL'), "
            +"('Onion: bunching', 0.5, NULL,'', '', '', '',0,0,-8,-3, 'OBN'), "
            +"('Peas: english, snap, snow', 3.0, 1,'', '', '', '',-7,-3,-11,-6, 'PEA'), "
            +"('Peas: southern', 3.0, 36,'', '', '', '',3,17,0,0, 'SPE'), "
            +"('Pepper', 0.5, 24,'', '', '', '',1,9,-17,-13, 'PEP'), "
            +"('Potatoes: Irish', 4.0, 15,'', '', '', '',-6,-1,-14,-11, 'POT'), "
            +"('Potatoes: sweet', NULL, 16,'', '', '', '',4,15,0,0, 'POS'), "
            +"('Pumpkin', 0.5, 48,'', '', '', '',0,16,0,0, 'PUM'), "
            +"('Radish', 0.5, 1,'', '', '', '',-25,3,-11,22, 'RAD'), "
            +"('Shallots', 0.5, NULL,'', '', '', '',0,0,-9,1, 'OBL'), "
            +"('Spinach', 0.5, 4,'', '', '', '',-23,-1,-9,17, 'SPN'), "
            +"('Squash: summer', 0.5, 36,'', '', '', '',0,7,-17,-10, 'SQS'), "
            +"('Squash: winter', 0.5, 48,'', '', '', '',0,12,-17,-13, 'SQW'), "
            +"('Tomatoes', 0.5, 48,'', '', '', '',0,6,-18,-14, 'TOM'), "
            +"('Turnip', 0.5, 3,'', '', '', '', -7,3,-12,-1, 'TUR'), "
            +"('Watermelon', 0.5, 72,'', '', '', '',3,13,0,0, 'UNK')"
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