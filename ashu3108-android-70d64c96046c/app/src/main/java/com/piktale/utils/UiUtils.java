package com.piktale.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.piktale.R;
import com.piktale.views.adapter.decorations.VerticalSpaceItemDecoration;
import com.piktale.views.widget.SubmitButton;

/**
 * Created by yassinegharsallah on 01/04/2017.
 */

public class UiUtils {


    public static void showSnackbar(View view, String message, int length) {
        Snackbar.make(view, message, length).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.primary_gradient_normal);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(color));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setAuthStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(R.color.authToolbar));
        }
    }

    public static void activeDeactive(View view, boolean active) {
        view.setEnabled(active);
        view.setClickable(active);
        if (view instanceof SubmitButton) {
            ((SubmitButton) view).mProceed.setEnabled(active);
            ((SubmitButton) view).mProceed.setClickable(active);
        }


//        view.setAlpha(active ? 1f : 0.2f);
        if (active) {
            view.animate().alpha(1f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        } else {
            view.animate().alpha(0.2f).setDuration(300).setInterpolator(new AccelerateInterpolator()).start();
        }
        if (view instanceof Button) {
            view.setSelected(active);
        }
    }

    public static GradientDrawable getGradientDrawable() {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        int color2 = generator.getRandomColor();
        int colors[] = {
                color1,
                color2};
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }

    public static GradientDrawable getGradientDrawable(String key1, String key2) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getColor(key1);
        int color2 = generator.getColor(key2);
        int colors[] = {
                color1,
                color2};
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColors(colors);
        return gradientDrawable;
    }

    public static LinearLayoutManager layoutManager(Context context, int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(orientation);
        return manager;
    }

    public static VerticalSpaceItemDecoration getItemDecoration(int h) {
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(h, true);
        return verticalSpaceItemDecoration;
    }

}
