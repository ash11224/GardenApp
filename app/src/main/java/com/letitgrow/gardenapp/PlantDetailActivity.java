package com.letitgrow.gardenapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.letitgrow.gardenapp.MyContentProvider;
import com.letitgrow.gardenapp.PlantTable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ashley on 4/18/2015.
 */
public class PlantDetailActivity extends Activity{

    private TextView PlantTitleText;
  //  private TextView FavoriteLabel;
    private TextView PlantSpacingLabel;
    private TextView PlantSpacingText;
    private TextView PerSquareFootLabel;
    private TextView PerSquareFootText;
    private TextView PlantDepthLabel;
    private TextView PlantDepthText;
    private TextView SpringPlantingLabel;
    private TextView SpringPlantingText;
    private TextView FallPlantingLabel;
    private TextView FallPlantingText;
    private TextView CompanionsLabel;
    private TextView CompanionsText;
    private TextView NuisancesLabel;
    private TextView NuisancesText;
    private TextView HelpersLabel;
    private TextView HelpersText;
    private TextView PestsLabel;
    private TextView PestsText;

    private ImageView PlantPic;

    private ToggleButton  FavBtn;

    private Uri plantUri;

    Calendar FIRST_FROST_DATE = Calendar.getInstance();
    Calendar LAST_FROST_DATE = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.plant_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        PlantTitleText = (TextView) findViewById(R.id.plant_text_name);
       // FavoriteLabel = (TextView) findViewById(R.id.plant_label_favorite);
        PlantSpacingLabel = (TextView) findViewById(R.id.plant_label_spacing);
        PlantSpacingText = (TextView) findViewById(R.id.plant_text_spacing);
        PerSquareFootLabel = (TextView) findViewById(R.id.plant_label_sqft);
        PerSquareFootText = (TextView) findViewById(R.id.plant_text_sqft);
        PlantDepthLabel = (TextView) findViewById(R.id.plant_label_depth);
        PlantDepthText = (TextView) findViewById(R.id.plant_text_depth);
        SpringPlantingLabel = (TextView) findViewById(R.id.plant_label_spring);
        SpringPlantingText =(TextView) findViewById(R.id.plant_text_spring);
        FallPlantingLabel = (TextView) findViewById(R.id.plant_label_fall);
        FallPlantingText = (TextView) findViewById(R.id.plant_text_fall);
        CompanionsLabel = (TextView) findViewById(R.id.plant_label_companions);
        CompanionsText = (TextView) findViewById(R.id.plant_text_companions);
        NuisancesLabel = (TextView) findViewById(R.id.plant_label_nuisance);
        NuisancesText = (TextView) findViewById(R.id.plant_text_nuisance);
        HelpersLabel = (TextView) findViewById(R.id.plant_label_helpers);
        HelpersText = (TextView) findViewById(R.id.plant_text_helpers);
        PestsLabel = (TextView) findViewById(R.id.plant_label_pests);
        PestsText = (TextView) findViewById(R.id.plant_text_pests);

        PlantPic = (ImageView) findViewById(R.id.plant_pic);

        FavBtn = (ToggleButton) findViewById(R.id.toggleButton);

        if (!noFrostDates()){SetFrostDates();
        }


        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        plantUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            plantUri = extras
                    .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

            fillData(plantUri);
        }
    }

    private void fillData(Uri uri) {
        String str, str1, str2;
        double num1, num2;
        int int1, int2;

        String[] projection = { PlantDBHelper.COLUMN_PLANT,
                PlantDBHelper.COLUMN_SPACING,
                PlantDBHelper.COLUMN_DEPTH,
                PlantDBHelper.DAYS_MATURITY,
                PlantDBHelper.COLUMN_COMPANIONS,
                PlantDBHelper.COLUMN_NUISANCES,
                PlantDBHelper.COLUMN_HELPERS,
                PlantDBHelper.COLUMN_PESTS,
                PlantDBHelper.SPRING_BEG,
                PlantDBHelper.SPRING_END,
                PlantDBHelper.FALL_BEG,
                PlantDBHelper.FALL_END,
                PlantDBHelper.PIC_NAME,
                PlantDBHelper.COLUMN_FAVORITE };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            String fav = cursor.getString(cursor.getColumnIndexOrThrow(PlantDBHelper.COLUMN_FAVORITE));
            Typeface face = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
            FavBtn.setTypeface(face);
            //ToggleButton  FavBtn = (ToggleButton)findViewById(R.id.toggleButton);

            if (fav.contains("N")) {
                FavBtn.setChecked(false);
            }
            else {
                FavBtn.setChecked(true);
            }


            PlantTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_PLANT)));
           // FavoriteLabel.setText("Favorite: ");

            PlantSpacingLabel.setText("Plant Spacing: ");
            PerSquareFootLabel.setText("Plants per Sq Ft: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_SPACING));
            if (str == null){
                str = "No information available";
                PlantSpacingText.setText(String.format("%s", str));
                PerSquareFootText.setText(String.format("%s", str));
            } else if (isNumeric(str)){
                PlantSpacingText.setText(String.format("%s inches", str));
                num1 = Double.parseDouble(str);
                num2 = 12/num1;
                str = Double.toString(num2);
                PerSquareFootText.setText(String.format("%s", str));
            }

            PlantDepthLabel.setText("Seed Depth: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_DEPTH));
            PlantDepthText.setText(String.format("%s inches", str));

            SpringPlantingLabel.setText("Spring Planting: ");
            FallPlantingLabel.setText("Fall Planting: ");
            if (noFrostDates()){
                SpringPlantingText.setText("Lucky duck! You can plant all year!");
                FallPlantingText.setText("Lucky duck! You can plant all year!");
            } else {
                str1 = cursor.getString(cursor
                        .getColumnIndexOrThrow(PlantDBHelper.SPRING_BEG));
                str2 = cursor.getString(cursor
                        .getColumnIndexOrThrow(PlantDBHelper.SPRING_END));
                if (isInteger(str1)) {
                    int1 = Integer.parseInt(str1);
                } else int1 = 0;

                if (isInteger(str2)) {
                    int2 = Integer.parseInt(str2);
                } else int2 = 0;

                SpringPlantingText.setText(dateRange(LAST_FROST_DATE, int1, int2));

                str1 = cursor.getString(cursor
                        .getColumnIndexOrThrow(PlantDBHelper.FALL_BEG));
                str2 = cursor.getString(cursor
                        .getColumnIndexOrThrow(PlantDBHelper.FALL_END));
                if (isInteger(str1)) {
                    int1 = Integer.parseInt(str1);
                } else int1 = 0;

                if (isInteger(str2)) {
                    int2 = Integer.parseInt(str2);
                } else int2 = 0;

                FallPlantingText.setText(dateRange(FIRST_FROST_DATE, int1, int2));
            }

            CompanionsLabel.setText("Companions: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_COMPANIONS));
            CompanionsText.setText(String.format("%s", str));

            NuisancesLabel.setText("Nuisances: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_NUISANCES));
            NuisancesText.setText(String.format("%s", str));

            HelpersLabel.setText("Helpers: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_HELPERS));
            HelpersText.setText(String.format("%s", str));

            PestsLabel.setText("Pests: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.COLUMN_PESTS));
            PestsText.setText(String.format("%s", str));

            //str = cursor.getString(cursor
            //        .getColumnIndexOrThrow(PlantTable.PIC_NAME));
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantDBHelper.PIC_NAME));
            int resID = getResources().getIdentifier(str.toLowerCase(),
                    "drawable", getPackageName());

            PlantPic.setImageResource(resID);

            // always close the cursor
            cursor.close();
        }
    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        ContentValues values = new ContentValues();
        if (on) {
            values.put(PlantDBHelper.COLUMN_FAVORITE, "Y");
        } else {
            values.put(PlantDBHelper.COLUMN_FAVORITE, "N");
        }

        getContentResolver().update(plantUri, values, null, null);
    }


    private void makeToast() {
        Toast.makeText(PlantDetailActivity.this, "Please maintain a summary",
                Toast.LENGTH_LONG).show();
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        PlantPic.setImageDrawable(null);
    }

    public void SetFrostDates(){

        Date ffDate = FIRST_FROST_DATE.getTime();
        Date lfDate = LAST_FROST_DATE.getTime();
        /*SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String ffd_str = mySharedPreferences.getString("ffd", "");
        String lfd_str = mySharedPreferences.getString("lfd", "");*/

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String ffd_str = prefs.getString("ffd", "");
        String lfd_str = prefs.getString("lfd", "");

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

    public static String dateRange(Calendar frostDate, int beg, int end){
        String result = "Not recommended";

        if ((beg == 0) && (end == 0)){
            result = "Not recommended";
        } else {


            DateFormat format = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
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

            result = format.format(BEG.getTime()) + " through " + format.format(END.getTime());

        }
        return result;
    }

    public boolean noFrostDates(){
        Boolean none = false;
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this);
        String ffd_str = prefs.getString("ffd", "");
        String lfd_str = prefs.getString("lfd", "");

        if (ffd_str.equals(lfd_str)){
            none = true;
        }
        return none;
    }

}