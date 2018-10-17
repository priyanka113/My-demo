package com.piktale.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.piktale.R;
import com.piktale.databinding.FragmentProfileWorkEduEditBinding;
import com.piktale.enums.EditPersonalInfoType;
import com.piktale.enums.EditProfileInfoItemType;
import com.piktale.enums.EditProfileInfoType;
import com.piktale.models.CommonProfileInfo;
import com.piktale.models.EditProfile;
import com.piktale.models.EditProfileItem;
import com.piktale.models.Profile;
import com.piktale.utils.Constants;
import com.piktale.views.adapter.EditProfileAdapter;
import com.piktale.views.adapter.decorations.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileEducationFragment extends BaseFragment {

    FragmentProfileWorkEduEditBinding binding;
    EditProfileAdapter adapter;
    Profile profile;

    public static EditProfileEducationFragment getInstance() {
        EditProfileEducationFragment fragment = new EditProfileEducationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_work_edu_edit, container, false);
        bindData();
        return binding.getRoot();
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

        profile = getApp().getProfile();

        adapter = new EditProfileAdapter(new ArrayList<>(), getActivity(), binding.items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        VerticalSpaceItemDecoration decoration = new VerticalSpaceItemDecoration(32, true);
        binding.items.addItemDecoration(decoration);
        binding.items.setLayoutManager(linearLayoutManager);

        List<EditProfile> editProfileItems = new ArrayList<>();

        EditProfile eduEditProfile = fakeEditProfile("Education", EditProfileInfoType.EDUCATION, new ArrayList<>());
        EditProfileItem e1 = fakeEditProfileItem("Add a high school", profile.getEducation().getHighSchool(), EditProfileInfoItemType.HIGH_SCHOOL);
        EditProfileItem e2 = fakeEditProfileItem("Add a higher secondary school", profile.getEducation().getHighSecSchool(), EditProfileInfoItemType.SECONDARY_HIGH_SCHOOL);
        EditProfileItem e3 = fakeEditProfileItem("Add a college/university", profile.getEducation().getUniversity(), EditProfileInfoItemType.UNIVERSITY);
        eduEditProfile.addItems(e1, e2, e3);
        editProfileItems.add(eduEditProfile);

        EditProfile workEditProfile = fakeEditProfile("Work", EditProfileInfoType.WORK, new ArrayList<>());
        EditProfileItem w1 = fakeEditProfileItem("Add a Company", profile.getWork().getCompanies(), EditProfileInfoItemType.COMPANY);
        workEditProfile.addItems(w1);
        editProfileItems.add(workEditProfile);
        adapter.addAll(editProfileItems);
        adapter.setOnDataChangedListener(new EditProfileAdapter.DataChangedListener() {
            @Override
            public void onDataChanged() {
                Log.d("Profile Date", new Gson().toJson(profile));
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.PROFILE_DATA, new Gson().toJson(profile));
            }
        });
        binding.items.setAdapter(adapter);
    }


    private EditProfileItem fakeEditProfileItem(String title, List<CommonProfileInfo> items, EditProfileInfoItemType type) {
        return new EditProfileItem(title, items, type);
    }

    private EditProfile fakeEditProfile(String title, EditProfileInfoType type, List<EditProfileItem> items) {
        return new EditProfile(title, type, items);
    }
}
