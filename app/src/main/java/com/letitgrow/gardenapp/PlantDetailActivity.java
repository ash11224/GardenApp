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
  // private Spinner mCategory;
    private TextView PlantTitleText;
    private TextView PlantDepthText;
    private TextView PlantSpacingText;
    private TextView FavoriteText;

    private Uri plantUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.plant_detail);

       // mCategory = (Spinner) findViewById(R.id.category);
        PlantTitleText = (TextView) findViewById(R.id.plant_text_name);
        PlantDepthText = (TextView) findViewById(R.id.plant_text_depth);
        PlantSpacingText = (TextView) findViewById(R.id.plant_text_spacing);
        FavoriteText = (TextView) findViewById(R.id.plant_text_favorite);
       // Button confirmButton = (Button) findViewById(R.id.plant_edit_button);

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

        String[] projection = { PlantTable.COLUMN_PLANT,
                PlantTable.COLUMN_SPACING, PlantTable.COLUMN_DEPTH,
                PlantTable.COLUMN_FAVORITE};
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
            PlantDepthText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_DEPTH)));
            PlantSpacingText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(PlantTable.COLUMN_SPACING)));
            FavoriteText.setText("Favorite: ");

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
}