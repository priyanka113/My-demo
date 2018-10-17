package com.piktale.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.piktale.R;
import com.piktale.utils.Constants;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.piktale.views.fragments.ProfileFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    private static final int TIMER_VALUE = 2;

    @BindView(R.id.logo)
    View logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getCompositeSubscription().add(waitForSplash());
        UiUtils.setStatusBarColor(this, R.color.bg);
        
//        String input1 = "2018-07-08T15:58:04.039";
//        String input2 = "2011-03-02T00:00:00.000Z";
//
//        String output = Util.getDisplayDate(input2, Constants.DATE_FORMATS.MMM_YYYY);
//        Log.d("output", output);


//        Flubber.with()
//                .animation(Flubber.AnimationPreset.ROTATION)
//                .repeatCount(5)
//                .duration(500)
//                .createFor(logo)
//                .start();

    }

    private Subscription waitForSplash() {
        return Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Long>() {
                    public void call(Long x) {
                        Log.d("Interval", String.valueOf(x));
                    }
                })
                .takeUntil(aLong -> aLong == TIMER_VALUE)
                .doOnCompleted(() -> {
                    navigate();
                }).subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void navigate() {
        if (getApp().getPreferences().isUserLoggedIn()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        }
        finish();
    }
}
