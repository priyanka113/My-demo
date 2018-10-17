package com.piktale.views.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.piktale.R;
import com.piktale.RootApplication;
import com.piktale.network.APIService;
import com.piktale.utils.Util;
import com.piktale.views.activities.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import rx.subscriptions.CompositeSubscription;

public class BaseFragment extends Fragment {

    protected Context context;
    public BaseActivity baseActivity;
    private RootApplication rootApplication;

    public CompositeSubscription compositeSubscription;

    @Nullable
    @BindView(R.id.progress)
    public ProgressBar progress;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onPostCreateView(container);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        baseActivity = (BaseActivity) getActivity();
        if (progress != null) {
            progress.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    protected void onPostCreateView(View view) {
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
        rootApplication = (RootApplication) getActivity().getApplication();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeSubscription.clear();
    }

    public void setRecyclerViewLayoutManager(int orientation, RecyclerView recyclerView) {
        baseActivity.setRecyclerViewLayoutManager(orientation, recyclerView);
    }

    public RootApplication getApp() {
        if (rootApplication == null)
            rootApplication = (RootApplication) getActivity().getApplication();
        return rootApplication;
    }

    public APIService getAPIService() {
        return getApp().getAPIService();
    }

    public void toggleProgressBar(boolean show, View clickedView) {
        if (progress != null)
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
        clickedView.setSelected(show ? false : true);
        if (clickedView != null) {
            if (clickedView instanceof Button) {
                Button b = (Button) clickedView;
                if (!Util.textIsEmpty(b.getText().toString()))
                    b.setTag(b.getText().toString());
                b.setText(show ? "" : b.getTag().toString());
            }
        }
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }
}
