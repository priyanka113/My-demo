package com.piktale.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.piktale.R;
import com.appolica.flubber.Flubber;

public class AnimationUtil {

    /**
     * Animates a view so that it slides in from the left of it's container.
     *
     * @param context
     * @param view
     */
    public static void slideInFromLeft(Context context, View view, boolean visible) {
        runSimpleAnimation(context, view, R.anim.slide_from_left, visible);
    }

    /**
     * Animates a view so that it slides from its current position, out of view to the left.
     *
     * @param context
     * @param view
     */
    public static void slideOutToLeft(Context context, View view, boolean visible) {
        runSimpleAnimation(context, view, R.anim.slide_to_left, visible);
    }

    /**
     * Animates a view so that it slides in the from the right of it's container.
     *
     * @param context
     * @param view
     */
    public static void slideInFromRight(Context context, View view, boolean visible) {
        runSimpleAnimation(context, view, R.anim.slide_from_right, visible);
    }

    /**
     * Animates a view so that it slides from its current position, out of view to the right.
     *
     * @param context
     * @param view
     */
    public static void slideOutToRight(Context context, View view, boolean visible) {
        runSimpleAnimation(context, view, R.anim.slide_to_right, visible);
    }

    public static void shake(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.shake, true);
    }

    /**
     * Runs a simple animation on a View with no extra parameters.
     *
     * @param context
     * @param view
     * @param animationId
     */
    private static void runSimpleAnimation(Context context, View view, int animationId, boolean visible) {
        Animation animation = AnimationUtils.loadAnimation(
                context, animationId
        );
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!visible)
                    view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void shake(View view, int repeatCount) {
        Flubber.with()
                .animation(Flubber.AnimationPreset.SHAKE)
                .repeatCount(repeatCount)
                .duration(500)
                .createFor(view)
                .start();
    }

}