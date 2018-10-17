package com.piktale.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.piktale.R;
import com.piktale.utils.Util;

public class SubmitButton extends FrameLayout {

    private String mLabel;
    public Button mProceed;
    private ProgressBar mProgressBar;

    public SubmitButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttributes(attrs);
        setViews();
    }

    public SubmitButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttributes(attrs);
        setViews();
    }

    public SubmitButton(@NonNull Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.submit_button, null);
        mProceed = view.findViewById(R.id.proceed);
        mProgressBar = view.findViewById(R.id.progress);
        addView(view);
    }

    private void initAttributes(@Nullable AttributeSet attrs) {
        TypedArray styleAttrs = getContext().obtainStyledAttributes(
                attrs, R.styleable.SubmitButton);
        mLabel = styleAttrs.getString(R.styleable.SubmitButton_label);
    }

    private void setViews() {
        if (Util.textIsEmpty(mLabel)) {
            mLabel = "Submit";
        }
        mProceed.setText(mLabel);
        mProgressBar.setVisibility(View.GONE);
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProceed.setTag(mProceed.getText().toString());
        mProceed.setText("");
        mProceed.setEnabled(false);
        mProceed.setClickable(false);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        mProceed.setText(mProceed.getTag().toString());
        mProceed.setEnabled(true);
        mProceed.setClickable(true);
    }
}
