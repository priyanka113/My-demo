package com.piktale.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piktale.R;

import butterknife.ButterKnife;

public class TalesFragment extends BaseFragment {

    View view;


    public static TalesFragment getInstance() {
        TalesFragment fragment = new TalesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tales, container, false);
        ButterKnife.bind(this, view);
        bindData();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreateView(View view) {
        super.onPostCreateView(view);
    }


    private void bindData() {

    }
}
