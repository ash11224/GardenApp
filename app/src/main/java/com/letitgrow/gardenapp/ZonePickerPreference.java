package com.letitgrow.gardenapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.preference.Preference;
import com.letitgrow.gardenapp.CustomDatePreference;

import java.util.Arrays;

/**
 * Created by Ashley on 6/8/2015.
 */
public class ZonePickerPreference extends DialogPreference {

    public static final int MAX_VALUE = 25;
    public static final int MIN_VALUE = 0;
    public static final String[] subZones = {   "1A", "1B", "2A", "2B",
                                                "3A", "3B", "4A", "4B",
                                                "5A", "5B", "6A", "6B",
                                                "7A", "7B", "8A", "8B",
                                                "9A", "9B", "10A", "10B",
                                                "11A", "11B", "12A", "12B",
                                                "13A", "13B"    };

    private NumberPicker zonePicker;
    private int value;

    public ZonePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZonePickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected View onCreateDialogView() {
        this.zonePicker = new NumberPicker(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        zonePicker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(zonePicker);

        return dialogView;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultVal) {
        if (restoreValue){
            setValue(getPersistedInt(MIN_VALUE));
        }
        else {
            setValue((Integer) defaultVal);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            setValue(zonePicker.getValue());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, MIN_VALUE);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        zonePicker.setMinValue(MIN_VALUE);
        zonePicker.setMaxValue(MAX_VALUE);
        zonePicker.setDisplayedValues(subZones);
        zonePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        zonePicker.setValue(getValue());
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
        setSummary(subZones[this.value]);
    }

    public void setValueFromZoneText(String txt){
       String uptxt = txt.toUpperCase();
       if (Arrays.asList(subZones).contains(uptxt)){
         int index = Arrays.asList(subZones).indexOf(uptxt);
         setValue(index);
        }

    }

    public int getValue() {
        return this.value;
    }

    public String getDisplayValue() {
        return subZones[getValue()];
    }
}
