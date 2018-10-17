package com.piktale.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.piktale.R;
import com.piktale.models.RestError;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;

public class Util {

    public static boolean textIsEmpty(String value) {
        boolean empty = false;

        if (value == null)
            return true;

        String message = value.trim();

        if (TextUtils.isEmpty(message)) {
            empty = true;
        }

        boolean isWhitespace = message.matches("^\\s*$");

        if (isWhitespace) {
            empty = true;
        }

        return empty;
    }

    public static int getPixelsFromDPs(Activity activity, int dps) {
        /*
            public abstract Resources getResources ()

                Return a Resources instance for your application's package.
        */
        Resources r = activity.getResources();
        float dimen = r.getDimension(dps);

        /*
            TypedValue

                Container for a dynamically typed data value. Primarily
                used with Resources for holding resource values.
        */

        /*
            applyDimension(int unit, float value, DisplayMetrics metrics)

                Converts an unpacked complex data value holding
                a dimension to its final floating point value.
        */

        /*
            Density-independent pixel (dp)

                A virtual pixel unit that you should use when defining UI layout,
                to express layout dimensions or position in a density-independent way.

                The density-independent pixel is equivalent to one physical pixel on
                a 160 dpi screen, which is the baseline density assumed by the system
                for a "medium" density screen. At runtime, the system transparently handles
                any scaling of the dp units, as necessary, based on the actual density
                of the screen in use. The conversion of dp units to screen pixels
                is simple: px = dp * (dpi / 160). For example, on a 240 dpi screen,
                1 dp equals 1.5 physical pixels. You should always use dp
                units when defining your application's UI, to ensure proper
                display of your UI on screens with different densities.
        */

        /*
            public static final int COMPLEX_UNIT_DIP

                TYPE_DIMENSION complex unit: Value is Device Independent Pixels.
                Constant Value: 1 (0x00000001)
        */
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dimen, r.getDisplayMetrics()));
        return px;
    }

    public static RestError handleError(ResponseBody response) {
        RestError restError = new RestError();
        restError.setErrorMsg("Something Went Wrong!");
        if (response instanceof ResponseBody) {
            ResponseBody responseBody = response;
            try {
                restError = new Gson().fromJson(new String(responseBody.bytes()), RestError.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return restError;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void changeSystemBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(activity.getResources().getColor(color));
    }

    public static void makeStatusbarTint(Context context, boolean tint) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = appCompatActivity.getWindow().getDecorView();
            if (tint) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);
            }
        }
    }

    public static String[] getDatesForDisplayAndServer(Calendar calendar) {
        SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat displayFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
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

    public static TextDrawable getTextDrawable(String input) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(input);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(input.charAt(0)), color);
        return drawable;
    }


}
