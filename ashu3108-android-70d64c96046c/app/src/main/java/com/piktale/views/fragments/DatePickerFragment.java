package com.piktale.views.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerFragmentListener {
        public void onDateSet(Calendar calendar);
    }

    private DatePickerFragmentListener datePickerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);

        if (getArguments() != null && getArguments().containsKey("calendar")) {
            c = (Calendar) getArguments().getSerializable("calendar");
            String direction = getArguments().getString("direction");

            // if it is backward disable back dates to this calendar('c')
            if (direction.equalsIgnoreCase("backward")) {
                dpd.getDatePicker().setMinDate(c.getTimeInMillis());
            } else {
                dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
            }
        }

        // Create a new instance of DatePickerDialog and return it
        return dpd;
    }

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        return fragment;
    }

    public static DatePickerFragment newInstance(DatePickerFragmentListener listener, Calendar calendar, String direction) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setDatePickerListener(listener);
        Bundle bundle = new Bundle();
        bundle.putSerializable("calendar", calendar);
        bundle.putString("direction", direction);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date, d = null, m = null, y = null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));

        notifyDatePickerListener(calendar);
    }

    public DatePickerFragmentListener getDatePickerListener() {
        return this.datePickerListener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        this.datePickerListener = listener;
    }

    protected void notifyDatePickerListener(Calendar date) {
        if (this.datePickerListener != null) {
            this.datePickerListener.onDateSet(date);
        }
    }

}