package com.letitgrow.gardenapp;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Ashley on 4/18/2015.
 */
public class MyContentProvider extends ContentProvider{


    // database
    //private HardCodeSQLiteHelper database;
    private PlantDBHelper database;

    // used for the UriMacher
    private static final int PLANTS_ALL = 10;
    private static final int PLANTS_ALL_ID = 20;

    private static final String AUTHORITY = "com.letitgrow.gardenapp.contentprovider";

    private static final String BASE_PATH = "plants";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/plants";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/plant";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PLANTS_ALL);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PLANTS_ALL_ID);
    }

    @Override
    public boolean onCreate() {
        database = new PlantDBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(PlantDBHelper.TABLE_PLANTS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PLANTS_ALL:
                break;

            case PLANTS_ALL_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(PlantDBHelper.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        database = new PlantDBHelper(getContext());
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        long id = 0;
        switch (uriType) {
            case PLANTS_ALL:
                id = sqlDB.insert(PlantDBHelper.TABLE_PLANTS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PLANTS_ALL:
                rowsDeleted = sqlDB.delete(PlantDBHelper.TABLE_PLANTS, selection,
                        selectionArgs);
                break;
            case PLANTS_ALL_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PlantDBHelper.TABLE_PLANTS,
                            PlantDBHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PlantDBHelper.TABLE_PLANTS,
                            PlantDBHelper.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PLANTS_ALL:
                rowsUpdated = sqlDB.update(PlantDBHelper.TABLE_PLANTS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PLANTS_ALL_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(PlantDBHelper.TABLE_PLANTS,
                            values,
                            PlantDBHelper.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(PlantDBHelper.TABLE_PLANTS,
                            values,
                            PlantDBHelper.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
            String[] available = {  PlantDBHelper.COLUMN_ID,
                    PlantDBHelper.COLUMN_PLANT,
                    PlantDBHelper.COLUMN_SPACING,
                    PlantDBHelper.COLUMN_DEPTH,
                    PlantDBHelper.COLUMN_COMPANIONS,
                    PlantDBHelper.DAYS_MATURITY,
                    PlantDBHelper.COLUMN_NUISANCES,
                    PlantDBHelper.COLUMN_HELPERS,
                    PlantDBHelper.COLUMN_PESTS,
                    PlantDBHelper.SPRING_BEG,
                    PlantDBHelper.SPRING_END,
                    PlantDBHelper.FALL_BEG,
                    PlantDBHelper.FALL_END,
                    PlantDBHelper.PIC_NAME,
                    PlantDBHelper.COLUMN_FAVORITE
            };
            if (projection != null) {
                HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
                HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
                // check if all columns which are requested are available
                if (!availableColumns.containsAll(requestedColumns)) {
                    throw new IllegalArgumentException("Unknown columns in projection");
                }
            }
        }
}
