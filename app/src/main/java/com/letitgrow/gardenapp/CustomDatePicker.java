package com.letitgrow.gardenapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;

/**
 * Created by Ashley on 5/27/2015.
 */
public class CustomDatePicker extends DatePickerDialog {

    public CustomDatePicker(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        hideYear();
    }

    public CustomDatePicker(Context context, int theme, OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, listener, year, monthOfYear, dayOfMonth);
        hideYear();
    }

    @Override
    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        view = findViewById(R.id.CustomDatePicker);
        super.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    private void hideYear() {
        try {
            Field f[] = this.getClass().getDeclaredFields();
            for (Field field : f) {
                if (field.getName().equals("mYearPicker")) {
                    field.setAccessible(true);
                    ((View) field.get(this)).setVisibility(View.GONE);
                }
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }
    }
}