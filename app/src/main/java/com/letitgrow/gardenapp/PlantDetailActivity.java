package com.letitgrow.gardenapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Ashley on 4/18/2015.
 */
public class PlantDetailActivity extends Activity{

    private TextView PlantTitleText;
    private TextView FavoriteLabel;
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
    private TextView PicText;

    private Uri plantUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.plant_detail);


        PlantTitleText = (TextView) findViewById(R.id.plant_text_name);
        FavoriteLabel = (TextView) findViewById(R.id.plant_label_favorite);
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
        PicText = (TextView) findViewById(R.id.pic_name);


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

      /*  confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });*/
    }

    private void fillData(Uri uri) {
        String str, str1, str2;
        double num1, num2;
        int int1, int2;

        String[] projection = { PlantTable.COLUMN_PLANT,
                PlantTable.COLUMN_SPACING,
                PlantTable.COLUMN_DEPTH,
                PlantTable.COLUMN_COMPANIONS,
                PlantTable.COLUMN_NUISANCES,
                PlantTable.COLUMN_HELPERS,
                PlantTable.COLUMN_PESTS,
                PlantTable.SPRING_BEG,
                PlantTable.SPRING_END,
                PlantTable.FALL_BEG,
                PlantTable.FALL_END,
                PlantTable.PIC_NAME,
                PlantTable.COLUMN_FAVORITE };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
        /*    String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_DEPTH));

            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
           }*/

            String fav = cursor.getString(cursor.getColumnIndexOrThrow(PlantTable.COLUMN_FAVORITE));
            ToggleButton  tglbtn = (ToggleButton)findViewById(R.id.toggleButton);

            if (fav.contains("N")) {
                tglbtn.setChecked(false);
            }
            else {
                tglbtn.setChecked(true);
            }


            PlantTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_PLANT)));
            FavoriteLabel.setText("Favorite: ");

            PlantSpacingLabel.setText("Plant Spacing: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_SPACING));
            PlantSpacingText.setText(String.format("%s inches", str));

            PerSquareFootLabel.setText("Plants per Sq Ft: ");
            if (isNumeric(str)){
                num1 = Double.parseDouble(str);
                num2 = 12/num1;
                str = Double.toString(num2);
            }
            else str = "No information available";

            PerSquareFootText.setText(String.format("%s", str));

            PlantDepthLabel.setText("Seed Depth: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_DEPTH));
            PlantDepthText.setText(String.format("%s inches", str));

            SpringPlantingLabel.setText("Spring Planting: ");
            str1 = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.SPRING_BEG));
            str2 = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.SPRING_END));
            if (isInteger(str1)){
                int1 = Integer.parseInt(str1);
            }
            else int1 = 0;

            if (isInteger(str2)){
                int2 = Integer.parseInt(str1);
            }
            else int2 = 0;

            if ((int1 == 0) && (int2 == 0)){
               SpringPlantingText.setText("Not Recommended");
            }
            else SpringPlantingText.setText(String.format("%s to %s weeks after last frost", str1,str2));

            FallPlantingLabel.setText("Fall Planting: ");
            str1 = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.FALL_BEG));
            str2 = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.FALL_END));
            if (isInteger(str1)){
                int1 = Integer.parseInt(str1);
            }
            else int1 = 0;

            if (isInteger(str2)){
                int2 = Integer.parseInt(str1);
            }
            else int2 = 0;

            if ((int1 == 0) && (int2 == 0)){
                FallPlantingText.setText("Not Recommended");
            }
            else FallPlantingText.setText(String.format("%s to %s weeks after first frost", str1,str2));

            CompanionsLabel.setText("Companions: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_COMPANIONS));
            CompanionsText.setText(String.format("%s", str));

            NuisancesLabel.setText("Nuisances: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_NUISANCES));
            NuisancesText.setText(String.format("%s", str));

            HelpersLabel.setText("Helpers: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_HELPERS));
            HelpersText.setText(String.format("%s", str));

            PestsLabel.setText("Pests: ");
            str = cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_PESTS));
            PestsText.setText(String.format("%s", str));

            //str = cursor.getString(cursor
            //        .getColumnIndexOrThrow(PlantTable.PIC_NAME));
            str = "";
            PicText.setText(String.format("%s", str));

            // always close the cursor
            cursor.close();
        }
    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        ContentValues values = new ContentValues();
        if (on) {
            values.put(PlantTable.COLUMN_FAVORITE, "Y");
        } else {
            values.put(PlantTable.COLUMN_FAVORITE, "N");
        }

        getContentResolver().update(plantUri, values, null, null);
    }

   /* protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, plantUri);
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        saveState();
    }*/

  /*  private void saveState() {
       // String category = (String) mCategory.getSelectedItem();
        String summary = PlantTitleText.getText().toString();
        String description = PlantDepthText.getText().toString();

        // only save if either summary or description
        // is available

        if (description.length() == 0 && summary.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
       // values.put(PlantTable.COLUMN_SPACING, category);
        values.put(PlantTable.COLUMN_PLANT, summary);
        values.put(PlantTable.COLUMN_DEPTH, description);

        if (plantUri == null) {
            // New
            plantUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        } else {
            // Update
            getContentResolver().update(plantUri, values, null, null);
        }
    } */

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
}