package com.piktale.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.piktale.models.RestError;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;

public class DateUtil {

    public static String[] getDatesForDisplayAndServer(Calendar calendar) {
        SimpleDateFormat serverFormat = new SimpleDateFormat(Constants.DATE_FORMATS.YYYY_MM_DDTHHMMSS);
        SimpleDateFormat displayFormat = new SimpleDateFormat(Constants.DATE_FORMATS.DAY_MMM_D_YY);
        String display = displayFormat.format(calendar.getTime());
        String server = serverFormat.format(calendar.getTime());
        return new String[]{display, server};
    }

    public static String getServerDateFromCalendar(Calendar calendar) {
        SimpleDateFormat serverFormat = new SimpleDateFormat(Constants.DATE_FORMATS.YYYY_MM_DDTHHMMSS);
        String server = serverFormat.format(calendar.getTime());
        return server;
    }

    public static String getDisplayDate(Calendar calendar, String format) {
        SimpleDateFormat displayFormat = new SimpleDateFormat(format);
        String display = displayFormat.format(calendar.getTime());
        return display;
    }

    public static String getDisplayDate(String input, String format) {
        Date date = parseDate(input);
        String formatedDate = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            formatedDate = sdf.format(date);
        }
        return formatedDate;
    }

    public static Calendar getCalendar(String input) {
        Date date = parseDate(input);
        Calendar calendar = Calendar.getInstance();
        if(date != null)
            calendar.setTime(date);
        return calendar;
    }

    private static Date parseDate(String input) {
        Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMATS.YYYY_MM_DDTHHMMSS);
        try {
            if (input != null)
                d = sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
