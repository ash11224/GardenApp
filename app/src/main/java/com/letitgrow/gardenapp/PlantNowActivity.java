package com.letitgrow.gardenapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import com.letitgrow.gardenapp.CustomDatePreference;

public class PlantNowActivity extends Activity {
    private TextView DateText;

    private SQLiteDatabase db;
    private Uri plantUri;

    public boolean NowFavorites;

    DateFormat printFormat = new SimpleDateFormat("E, MMM dd, yyyy");
    Calendar today = Calendar.getInstance();
    Calendar FIRST_FROST_DATE = Calendar.getInstance();
    Calendar LAST_FROST_DATE = Calendar.getInstance();

    final ArrayList<String> list = new ArrayList<String>();
    final ArrayList<String> id_list = new ArrayList<String>();
    ListView listview;

    Intent backIntent = new Intent();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_plant_now);

        Bundle extras = getIntent().getExtras();

        DateText = (TextView) findViewById(R.id.plantNow);
        String dte = printFormat.format(today.getTime());
        DateText.setText(dte);

        listview = (ListView) findViewById(R.id.list_PlantNow);

        SetFrostDates();

        // check from the saved Instance
        plantUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyContentProvider.CONTENT_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            plantUri = extras
                    .getParcelable(MyContentProvider.CONTENT_TYPE);

            NowFavorites = extras.getBoolean("favPushed");
            ToggleButton aTglBtn = (ToggleButton) findViewById(R.id.mainToggleButtonNow);
            aTglBtn.setChecked(NowFavorites);

            fillData(plantUri);

        }

    }

    private void fillData(Uri plantUri) {

        String SELECTION;

        if (NowFavorites){
            SELECTION = PlantTable.COLUMN_FAVORITE+"= 'Y'";
        }
        else SELECTION = null;

        String[] projection = { PlantTable.COLUMN_ID,
                PlantTable.COLUMN_PLANT,
                PlantTable.SPRING_BEG,
                PlantTable.SPRING_END,
                PlantTable.FALL_BEG,
                PlantTable.FALL_END,
                PlantTable.COLUMN_FAVORITE };

        Cursor cur = getContentResolver().query(plantUri, projection, SELECTION, null,
                null);

        list.clear();
        id_list.clear();

        if (cur != null){
            while (cur.moveToNext()) {
                Boolean include = false;
                String name = (cur.getString(cur.getColumnIndexOrThrow(PlantTable.COLUMN_PLANT)));
                String str1, str2, str3, str4, str_id;
                Integer int1, int2, int3, int4;

                str1 = cur.getString(cur.getColumnIndexOrThrow(PlantTable.SPRING_BEG));
                str2 = cur.getString(cur.getColumnIndexOrThrow(PlantTable.SPRING_END));
                if (isInteger(str1))  { int1 = Integer.parseInt(str1); }
                else int1 = 0;
                if (isInteger(str2)){  int2 = Integer.parseInt(str2);  }
                else int2 = 0;

                str3 = cur.getString(cur.getColumnIndexOrThrow(PlantTable.FALL_BEG));
                str4 = cur.getString(cur.getColumnIndexOrThrow(PlantTable.FALL_END));
                if (isInteger(str3)){int3 = Integer.parseInt(str3);}
                else int3 = 0;
                if (isInteger(str4)){ int4 = Integer.parseInt(str4); }
                else int4 = 0;

                str_id = cur.getString(cur.getColumnIndexOrThrow(PlantTable.COLUMN_ID));

                if (inRange(today,LAST_FROST_DATE,int1, int2)){include = true; }    //SPRING
                if (inRange(today,FIRST_FROST_DATE ,int3, int4)){ include = true; }  //FALL

                if (include){
                    list.add(name);
                    id_list.add(str_id);}

                // cur.moveToNext();
            }
            cur.close();

        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                int id_int = Integer.parseInt(id_list.get((int) id));
                final String item = (String) parent.getItemAtPosition(position);
                Intent i = new Intent(PlantNowActivity.this, PlantDetailActivity.class);
                Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id_int);
                i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE, todoUri);

                startActivity(i);

            }

        });

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public void onAddDayClicked(View view){
        today.add(Calendar.DAY_OF_MONTH,1);

        String dte = printFormat.format(today.getTime());
        DateText.setText(dte);

        fillData(plantUri);
    }

    public void onLessDayClicked(View view){
        today.add(Calendar.DAY_OF_MONTH,-1);

        String dte = printFormat.format(today.getTime());
        DateText.setText(dte);

        fillData(plantUri);
    }

    public void onNowToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        ContentValues values = new ContentValues();
        if (on) {
            NowFavorites = true;
        } else {
            NowFavorites = false;
        }

        fillData(plantUri);

       // getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        backIntent.putExtra("favNowPushed", NowFavorites);
        setResult(Activity.RESULT_OK, backIntent);
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
          //  Log.i(LOG_TAG, "Back pressed");
            backIntent.putExtra("favNowPushed", NowFavorites);
            setResult(Activity.RESULT_OK, backIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plant_now, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    public void SetFrostDates(){

        Date ffDate = FIRST_FROST_DATE.getTime();
        Date lfDate = LAST_FROST_DATE.getTime();
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String ffd_str = mySharedPreferences.getString("ffd", "");
        String lfd_str = mySharedPreferences.getString("lfd", "");
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
        try {
            ffDate = format.parse(ffd_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            lfDate = format.parse(lfd_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FIRST_FROST_DATE.setTime(ffDate);
        LAST_FROST_DATE.setTime(lfDate);

    }

    public static boolean inRange(Calendar day, Calendar frostDate, int beg, int end){
        boolean okToPlant = false;

        if ((beg == 0) && (end == 0)){
            okToPlant = false;
        } else {

            int today = (day.get(Calendar.YEAR) * 10000) +
                    ((day.get(Calendar.MONTH) + 1) * 100) +
                    (day.get(Calendar.DAY_OF_MONTH));


            Calendar BEG = Calendar.getInstance();
            Calendar END = Calendar.getInstance();

            BEG.set(Calendar.YEAR, frostDate.get(Calendar.YEAR));
            BEG.set(Calendar.MONTH, frostDate.get(Calendar.MONTH)); //zero based
            BEG.set(Calendar.DAY_OF_MONTH, frostDate.get(Calendar.DAY_OF_MONTH));

            END.set(Calendar.YEAR, frostDate.get(Calendar.YEAR));
            END.set(Calendar.MONTH, frostDate.get(Calendar.MONTH)); //zero based
            END.set(Calendar.DAY_OF_MONTH, frostDate.get(Calendar.DAY_OF_MONTH));

            BEG.add(Calendar.DAY_OF_MONTH, (int) (beg * 7.5));
            END.add(Calendar.DAY_OF_MONTH, (int) (end * 7.5));

            int BegDate = (BEG.get(Calendar.YEAR) * 10000) +
                    ((BEG.get(Calendar.MONTH) + 1) * 100) +
                    (BEG.get(Calendar.DAY_OF_MONTH));

            int EndDate = (END.get(Calendar.YEAR) * 10000) +
                    ((END.get(Calendar.MONTH) + 1) * 100) +
                    (END.get(Calendar.DAY_OF_MONTH));

            if ((BegDate <= today) && (today <= EndDate)) {
                okToPlant = true;
            }

        }
        return okToPlant;
    }

}
