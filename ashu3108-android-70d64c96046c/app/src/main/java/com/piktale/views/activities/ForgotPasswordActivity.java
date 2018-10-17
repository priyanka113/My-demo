package com.piktale.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.models.BasicResponse;
import com.piktale.models.RestError;
import com.piktale.models.User;
import com.piktale.models.requestBody.VerifyOTP;
import com.piktale.network.CallbackWrapper;
import com.piktale.utils.AnimationUtil;
import com.piktale.utils.Constants;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.appolica.flubber.Flubber;
import com.chaos.view.PinView;
import com.piktale.views.widget.SubmitButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * A login screen that offers login via email/password.
 */
public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.enter_identity_cont)
    LinearLayout enterIdentityCont;

    @BindView(R.id.otp_sent_cont)
    LinearLayout otpSentCont;

    @BindView(R.id.enter_password)
    LinearLayout enterPassword;

    @BindView(R.id.identity)
    EditText identity;

    @BindView(R.id.otp_sent)
    TextView otpSent;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.confirm_password)
    EditText confirmPassword;

    @BindView(R.id.otp)
    PinView otp;

    @BindView(R.id.submit_button)
    SubmitButton submitButton;

    CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        compositeSubscription = new CompositeSubscription();
        setBackNavigation(true);
        UiUtils.setAuthStatusBarColor(this);
        toolbar.setTitle("Forgot Password");

        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(1000)                              // Last for 1000 milliseconds(1 second)
                .createFor(enterIdentityCont)                             // Apply it to the view
                .start();
        UiUtils.activeDeactive(submitButton, false);

        submitButton.mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProceed(view);
            }
        });

    }

    void onProceed(View view) {
        if (enterIdentityCont.getVisibility() == View.VISIBLE) {
            String input = identity.getText().toString();
            if (!Util.textIsEmpty(input)) {
                forgotPassword(view);
            } else {
                AnimationUtil.shake(this, identity);
            }
        } else if (otpSentCont.getVisibility() == View.VISIBLE) {
            String otpInput = otp.getText().toString();
            if (Util.textIsEmpty(otpInput)) {
                AnimationUtil.shake(this, otp);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "Please enter otp", Snackbar.LENGTH_LONG);
            } else if (otpInput.length() < 4) {
                AnimationUtil.shake(this, otp);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "OTP must be 4 digits long", Snackbar.LENGTH_LONG);
            } else {
                verifyOtp(view);
            }
        } else {
            String pass = password.getText().toString();
            String conPass = confirmPassword.getText().toString();
            if (Util.textIsEmpty(pass)) {
                AnimationUtil.shake(this, password);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "Please enter password", Snackbar.LENGTH_LONG);
            } else if (Util.textIsEmpty(conPass)) {
                AnimationUtil.shake(this, confirmPassword);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "Please confirm password", Snackbar.LENGTH_LONG);
            } else if (!pass.equalsIgnoreCase(conPass)) {
                AnimationUtil.shake(this, confirmPassword);
//                UiUtils.showSnackbar(findViewById(android.R.id.content), "Confirm password is incorrect", Snackbar.LENGTH_LONG);
            } else {
                updatePassword(view);
            }

        }
    }

    @OnTextChanged(R.id.otp)
    void onOTPChanged(CharSequence input) {
        String s = input.toString();
        if (s.length() == 4) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    @OnTextChanged(R.id.identity)
    void onTextChanged(CharSequence input) {
        String s = input.toString();
        if (!Util.textIsEmpty(s)) {
            UiUtils.activeDeactive(submitButton, true);
            if (TextUtils.isDigitsOnly(s)) {

            }
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    @OnTextChanged(R.id.password)
    void onPasswordChange(CharSequence input) {
        String s = input.toString();
        String cp = confirmPassword.getText().toString();
        if (s.equals(cp) && s.length() >= 5) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    @OnTextChanged(R.id.confirm_password)
    void onConfirmPasswordChange(CharSequence input) {
        String s = input.toString();
        String c = password.getText().toString();
        if (s.equals(c) && s.length() >= 5) {
            UiUtils.activeDeactive(submitButton, true);
        } else {
            UiUtils.activeDeactive(submitButton, false);
        }
    }

    private void forgotPassword(View clickedView) {
        User l = new User();
        l.setUsername(identity.getText().toString());
        getCompositeSubscription().add(getAPIService().forgot(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>(this, clickedView) {
            @Override
            protected void onSuccess(Response<BasicResponse<String>> response) {
                enterIdentityCont.setVisibility(View.GONE);
                otpSent.setText("OTP has been sent to your mobile number\n+91 " + identity.getText().toString());
                Flubber.with()
                        .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                        .repeatCount(0)                              // Repeat once
                        .duration(1000)                              // Last for 1000 milliseconds(1 second)
                        .createFor(otpSentCont)                             // Apply it to the view
                        .start();
                otpSentCont.setVisibility(View.VISIBLE);
                UiUtils.activeDeactive(submitButton, false);
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

    private void verifyOtp(View clickedView) {
        VerifyOTP l = new VerifyOTP();
        l.setUsername(identity.getText().toString());
        l.setOtp(1234);
        l.setApiType("forgot");
        getCompositeSubscription().add(getAPIService().verifyOTPForgotPassword(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>(this, clickedView) {
            @Override
            protected void onSuccess(Response<BasicResponse<String>> response) {
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.USER_TOKEN, response.body().getData());
                getApp().setApiService();
                otpSentCont.setVisibility(View.GONE);
                Flubber.with()
                        .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                        .repeatCount(0)                              // Repeat once
                        .duration(1000)                              // Last for 1000 milliseconds(1 second)
                        .createFor(enterPassword)                             // Apply it to the view
                        .start();
                enterPassword.setVisibility(View.VISIBLE);
                toolbar.setTitle("Reset Password");
                submitButton.mProceed.setText("Done");
                UiUtils.activeDeactive(submitButton, false);
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

    private void updatePassword(View clickedView) {
        User l = new User();
        l.setNewPassword(confirmPassword.getText().toString());
        getCompositeSubscription().add(getAPIService().updatePassword(l).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<String>>>(this, clickedView) {
            @Override
            protected void onSuccess(Response<BasicResponse<String>> response) {
                UiUtils.showToast(context, response.body().getData());
                finish();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
            }
        }));
    }

}

