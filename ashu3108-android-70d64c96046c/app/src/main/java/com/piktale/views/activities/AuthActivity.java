package com.piktale.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.piktale.BuildConfig;
import com.piktale.R;
import com.piktale.models.BasicResponse;
import com.piktale.models.Profile;
import com.piktale.models.User;
import com.piktale.network.CallbackWrapper;
import com.piktale.network.RestApiWrapper;
import com.piktale.utils.Constants;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.appolica.flubber.Flubber;
import com.piktale.views.widget.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A login screen that offers login via email/password.
 */
public class AuthActivity extends BaseActivity {

    @BindView(R.id.submit_button)
    SubmitButton submitButton;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.helper)
    TextView helper;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.form1)
    LinearLayout form1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        UiUtils.setStatusBarColor(this, R.color.bg);
        UiUtils.activeDeactive(submitButton, false);
        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(1000)                              // Last for 1000 milliseconds(1 second)
                .createFor(form1)                             // Apply it to the view
                .start();

        helper.setText(Html.fromHtml(getString(R.string.forgot_password)));

        if (!BuildConfig.BUILD_TYPE.contains("release")) {
//            name.setText("9619560549");
//            password.setText("123456789");
        }

        submitButton.mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);
            }
        });
    }

    @OnClick(R.id.helper)
    void onHelper() {
        startActivity(new Intent(AuthActivity.this, ForgotPasswordActivity.class));
    }

    @OnClick(R.id.signup)
    void onSignup() {
        startActivity(new Intent(AuthActivity.this, SignUpActivity1.class));
        finish();
    }

    @OnTextChanged(R.id.name)
    void onName(CharSequence input) {
        String s = input.toString();
        String p = password.getText().toString();

        if (!Util.textIsEmpty(s) && !Util.textIsEmpty(p)) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    @OnTextChanged(R.id.password)
    void onPassword(CharSequence input) {
        String s = input.toString();
        String p = name.getText().toString();

        if (!Util.textIsEmpty(s) && !Util.textIsEmpty(p)) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    void onSubmit(View view) {
        User l = new User();
        l.setUsername(name.getText().toString());
        l.setPassword(password.getText().toString());
        getCompositeSubscription().add(getAPIService().login(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<User>>>(this, submitButton) {
            @Override
            protected void onSuccess(Response<BasicResponse<User>> response) {
                User user = response.body().getData();
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.USER_TOKEN, user.getUserToken());
                getApp().setApiService();
                Profile profile = new Profile();
                profile.setUser(user);
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.PROFILE_DATA, new Gson().toJson(profile));
                getApp().setUser();
                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                finish();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));

    }
}

