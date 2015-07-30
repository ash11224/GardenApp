package com.letitgrow.gardenapp;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
/**
 * Created by Ashley on 5/26/2015.
 */
public class CustomDatePreference extends DialogPreference implements
        DatePicker.OnDateChangedListener {

    private String dateString;
    private String changedValueCanBeNull;
    private DatePicker datePicker;

    public CustomDatePreference(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public CustomDatePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        }


    @Override
    protected View onCreateDialogView() {
        this.datePicker = new DatePicker(getContext());
        Calendar calendar = getDate();
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH), this);
        return datePicker;
        }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult && this.changedValueCanBeNull != null) {
            setDate(this.changedValueCanBeNull);
            this.changedValueCanBeNull = null;
        }
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultVal) {
        if (restoreValue) {
            this.dateString = getPersistedString(defaultValue());
            setDate(this.dateString);
        } else {
            boolean wasNull = this.dateString == null;
            setDate((String) defaultVal);
            if (!wasNull)
                persistDate(this.dateString);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        if (isPersistent())
            return super.onSaveInstanceState();
        else
            return new SavedState(super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            setDate(((SavedState) state).dateValue);
        } else {
            SavedState s = (SavedState) state;
            super.onRestoreInstanceState(s.getSuperState());
            setDate(s.dateValue);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        datePicker.clearFocus();
        onDateChanged(datePicker, datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        onDialogClosed(which == DialogInterface.BUTTON1);
    }

    public void setDate(String dateString) {
        this.dateString = dateString;
        persistDate(this.dateString);
        setSummary(summaryFormatter().format(getDate().getTime()));
    }

    public Calendar getDate() {
        try {
            Date date = formatter().parse(defaultValue());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (java.text.ParseException e) {
            return defaultCalendar();
        }
    }



    public static SimpleDateFormat formatter() {
        return new SimpleDateFormat("yyyy.MM.dd");
    }

    public static SimpleDateFormat summaryFormatter() {
        return new SimpleDateFormat("MMMM dd, yyyy");
    }

    public void onDateChanged(DatePicker view, int year, int month, int day) {
        Calendar selected = new GregorianCalendar(year, month, day);
        this.changedValueCanBeNull = formatter().format(selected.getTime());
    }

    private void persistDate(String s) {
        persistString(s);
        setSummary(summaryFormatter().format(getDate().getTime()));
    }

    public static Calendar defaultCalendar() {
        DateFormat printFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar today = Calendar.getInstance();
        String dte = printFormat.format(today.getTime());
        return today;
    }

    public static String defaultCalendarString() {
        return formatter().format(defaultCalendar().getTime());
    }

    public String defaultValue() {
        if (this.dateString == null)
        setDate(defaultCalendarString());
        return this.dateString;
    }

    public static Calendar getDateFor(SharedPreferences preferences, String field) {
        Date date = stringToDate(preferences.getString(field,
        defaultCalendarString()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private static Date stringToDate(String dateString) {
        try {
        return formatter().parse(dateString);
        } catch (ParseException e) {
        return defaultCalendar().getTime();
        }
    }

    private static class SavedState extends BaseSavedState {
        String dateValue;

        public SavedState(Parcel p) {
        super(p);
        dateValue = p.readString();
        }

        public SavedState(Parcelable p) {
        super(p);
    }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(dateValue);
        }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };
}
}