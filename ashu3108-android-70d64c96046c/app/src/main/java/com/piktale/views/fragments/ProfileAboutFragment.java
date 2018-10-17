package com.piktale.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.models.CommonProfileInfo;
import com.piktale.models.Profile;
import com.piktale.models.User;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.piktale.views.adapter.EducationItemAdapter;
import com.piktale.views.adapter.WorkItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileAboutFragment extends BaseFragment {

    @BindView(R.id.relation)
    TextView relation;
    @BindView(R.id.relation_logo)
    ImageView relationLogo;

    @BindView(R.id.gender)
    TextView gender;
    @BindView(R.id.gender_logo)
    ImageView genderLogo;

    @BindView(R.id.language)
    TextView language;
    @BindView(R.id.language_logo)
    ImageView languageLogo;

    @BindView(R.id.work)
    RecyclerView work;

    @BindView(R.id.education)
    RecyclerView education;

    View view;
    Profile profile;

    public static ProfileAboutFragment getInstance() {
        ProfileAboutFragment fragment = new ProfileAboutFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_about, container, false);
        ButterKnife.bind(this, view);
        profile = getApp().getProfile();
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
        relationLogo.setImageDrawable(UiUtils.getGradientDrawable());
        genderLogo.setImageDrawable(UiUtils.getGradientDrawable());
        languageLogo.setImageDrawable(UiUtils.getGradientDrawable());
        User user = profile.getUser();
        if (user.getGender() != null) {
            gender.setText(user.getGender().getDescription());
        }

        if (!Util.textIsEmpty(user.getLanguage())) {
            language.setText(user.getLanguage());
        }

        WorkItemAdapter workItemAdapter = new WorkItemAdapter(profile.getWork().getCompanies(), getActivity());

        List<CommonProfileInfo> educationItems = new ArrayList<>();
        educationItems.addAll(profile.getEducation().getHighSchool());
        educationItems.addAll(profile.getEducation().getHighSecSchool());
        educationItems.addAll(profile.getEducation().getUniversity());

        EducationItemAdapter educationItemAdapter = new EducationItemAdapter(educationItems, getActivity());
        work.setLayoutManager(UiUtils.layoutManager(getActivity(), LinearLayoutManager.VERTICAL));
        work.addItemDecoration(UiUtils.getItemDecoration(32));
        work.setAdapter(workItemAdapter);

        education.setLayoutManager(UiUtils.layoutManager(getActivity(), LinearLayoutManager.VERTICAL));
        education.addItemDecoration(UiUtils.getItemDecoration(32));
        education.setAdapter(educationItemAdapter);
    }
}
