package com.piktale.views.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.piktale.R;
import com.piktale.databinding.ProfileAboutEditBinding;
import com.piktale.enums.EditPersonalInfoType;
import com.piktale.enums.FieldType;
import com.piktale.enums.Gender;
import com.piktale.models.BasicResponse;
import com.piktale.models.ItemEditPersonalInfo;
import com.piktale.models.Profile;
import com.piktale.models.User;
import com.piktale.network.CallbackWrapper;
import com.piktale.utils.Constants;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;
import com.piktale.views.adapter.EditPersonalInfoAdapter;
import com.piktale.views.adapter.decorations.VerticalSpaceItemDecoration;
import com.piktale.views.assist.Dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditProfilePersonalFragment extends BaseFragment {

    private List<ItemEditPersonalInfo> infoList = new ArrayList<>();
    View view;
    ProfileAboutEditBinding binding;
    EditPersonalInfoAdapter adapter;

    public static EditProfilePersonalFragment getInstance() {
        EditProfilePersonalFragment fragment = new EditProfilePersonalFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_about_edit, container, false);
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

        Profile profile = getApp().getPreferences().getProfile();
        User user = profile.getUser();

        infoList.add(new ItemEditPersonalInfo(FieldType.INPUT, EditPersonalInfoType.NAME,
                user.getName(), "", false, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.SELECTION, EditPersonalInfoType.USERNAME,
                user.getUsername(), "", false, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.DATE, EditPersonalInfoType.DOB,
                user.getDob(), "", false, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.INPUT, EditPersonalInfoType.MOBILE,
                user.getPhone(), "Account Info", false, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.INPUT, EditPersonalInfoType.EMAIL,
                "", "", false, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.INPUT, EditPersonalInfoType.LIVING,
                user.getHometown(), "", true, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.SELECTION, EditPersonalInfoType.GENDER,
                user.getGender() != null ? user.getGender().name() : "", "", true, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.SELECTION, EditPersonalInfoType.RELATIONSHIP_STATUS,
                "", "", true, false));
        infoList.add(new ItemEditPersonalInfo(FieldType.INPUT, EditPersonalInfoType.LANGUAGE,
                "", "", true, false));

        adapter = new EditPersonalInfoAdapter(infoList, baseActivity);

        LinearLayoutManager manager = new LinearLayoutManager(baseActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        VerticalSpaceItemDecoration decoration = new VerticalSpaceItemDecoration(16, true);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(decoration);
        adapter.setOnItemClickListener(new EditPersonalInfoAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                ItemEditPersonalInfo info = infoList.get(position);

                if (v.getId() == R.id.input) {
                    switch (info.getEditPersonalInfoType()) {
                        case DOB:
                            DatePickerFragment.newInstance(new DatePickerFragment.DatePickerFragmentListener() {
                                @Override
                                public void onDateSet(Calendar calendar) {
                                    String date = Util.getDisplayDate(calendar, Constants.DATE_FORMATS.DAY_MMM_D_YY);
                                    ((EditText) v).setText(date);
                                    ((TextInputLayout) ((EditText) v).getParentForAccessibility()).setTag(calendar);
                                }
                            }).show(((AppCompatActivity) context).getSupportFragmentManager(), "Select Birth Date");
                            break;
                        case GENDER:
                            CharSequence[] genders = getResources().getStringArray(R.array.gender);
                            Dialogs.showList(getActivity(), v, "Select Gender", genders, new Dialogs.DialogListener() {
                                @Override
                                public void onSelected() {

                                }
                            });
                            break;
                        case USERNAME:
                            Dialogs.showUsernameDialog(getActivity(), new Dialogs.UsernameDialogListener() {
                                @Override
                                public void okay(String username) {
                                    if (!Util.textIsEmpty(username)) {
                                        Profile profile = getApp().getProfile();
                                        User user = profile.getUser();
                                        user.setUsername(username);
                                        profile.setUser(user);
                                        getApp().getPreferences().setPreference(Constants.PreferenceKeys.PROFILE_DATA, new Gson().toJson(profile));
                                        ((EditText) v).setText(username);
                                    }
                                    getApp().getPreferences().setPreference(Constants.PreferenceKeys.USERNAME_DIALOG_SHOWED);
                                }

                                @Override
                                public void cancel() {
                                    getApp().getPreferences().setPreference(Constants.PreferenceKeys.USERNAME_DIALOG_SHOWED);
                                }
                            }, getApp());
                            break;
                    }
                } else if (v.getId() == R.id.privacy) {
                    Dialogs.showPopupMenu(context, R.menu.popup_privacy, v, new Dialogs.PopupMenuListener() {
                        @Override
                        public void onSelected(MenuItem item) {
                            LinearLayout ll = (LinearLayout) v;
                            for (int i = 0; i < ll.getChildCount(); i++) {
                                if (ll.getChildAt(i) instanceof TextView) {
                                    ((TextView) ll.getChildAt(i)).setText(item.getTitle());
                                    break;
                                }
                            }
                        }
                    });
                } else if (v.getId() == R.id.edit) {
                    Dialogs.showPopupMenu(context, R.menu.popup_edit, v, new Dialogs.PopupMenuListener() {
                        @Override
                        public void onSelected(MenuItem item) {
                            for (int i = 0; i < binding.recyclerView.getChildCount(); i++) {
                                ItemEditPersonalInfo info = infoList.get(i);
                                View view = binding.recyclerView.getChildAt(i);
                                EditText input = view.findViewById(R.id.input);
                                String value = input.getText().toString();
                                switch (info.getEditPersonalInfoType()) {
                                    case NAME:
                                        if (!Util.textIsEmpty(value)) {
                                            user.setName(value);
                                        }
                                        break;
                                    case RELATIONSHIP_STATUS:

                                        break;
                                    case GENDER:
                                        if (!Util.textIsEmpty(value)) {
                                            user.setGender(Gender.getGenderByDescription(value));
                                        }
                                        break;
                                    case DOB:
                                        if (!Util.textIsEmpty(value)) {
                                            Calendar calendar = (Calendar) ((TextInputLayout) (input).getParentForAccessibility()).getTag();
                                            user.setDob(Util.getServerDateFromCalendar(calendar));
                                        }
                                        break;
                                    case EMAIL:
                                        if (Util.isValidEmail(value)) {
                                            user.setEmail(value);
                                        }
                                        break;
                                    case LIVING:
                                        if (!Util.textIsEmpty(value)) {
                                            user.setHometown(value);
                                        }
                                        break;
                                    case MOBILE:
                                        if (!Util.textIsEmpty(value)) {
                                            user.setPhone(value);
                                        }
                                        break;
                                    case STATUS:
                                        break;
                                    case LANGUAGE:
                                        if (!Util.textIsEmpty(value)) {
                                            user.setLanguage(value);
                                        }
                                        break;
                                    case USERNAME:
                                        if (!Util.textIsEmpty(value)) {
                                            user.setUsername(value);
                                        }
                                        break;
                                }
                            }
                        }
                    });
                }
            }
        });

        binding.submitButton.mProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < binding.recyclerView.getChildCount(); i++) {
                    ItemEditPersonalInfo info = infoList.get(i);
                    View view = binding.recyclerView.getChildAt(i);
                    EditText input = view.findViewById(R.id.input);
                    String value = input.getText().toString();
                    switch (info.getEditPersonalInfoType()) {
                        case NAME:
                            if (!Util.textIsEmpty(value)) {
                                user.setName(value);
                            }
                            break;
                        case RELATIONSHIP_STATUS:

                            break;
                        case GENDER:
                            if (!Util.textIsEmpty(value)) {
                                user.setGender(Gender.getGenderByDescription(value));
                            }
                            break;
                        case DOB:
                            if (!Util.textIsEmpty(value)) {
                                Calendar calendar = (Calendar) ((TextInputLayout) (input).getParentForAccessibility()).getTag();
                                user.setDob(Util.getServerDateFromCalendar(calendar));
                            }
                            break;
                        case EMAIL:
                            if (Util.isValidEmail(value)) {
                                user.setEmail(value);
                            }
                            break;
                        case LIVING:
                            if (!Util.textIsEmpty(value)) {
                                user.setHometown(value);
                            }
                            break;
                        case MOBILE:
                            if (!Util.textIsEmpty(value)) {
                                user.setPhone(value);
                            }
                            break;
                        case STATUS:
                            break;
                        case LANGUAGE:
                            if (!Util.textIsEmpty(value)) {
                                user.setLanguage(value);
                            }
                            break;
                        case USERNAME:
                            if (!Util.textIsEmpty(value)) {
                                user.setUsername(value);
                            }
                            break;
                    }
                }
                binding.submitButton.showProgress();
                profile.setUser(user);
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.PROFILE_DATA, new Gson().toJson(profile));
                new CountDownTimer(2000, 1000) {
                    public void onFinish() {
                        // When timer is finished
                        // Execute your code here
                        binding.submitButton.hideProgress();
                    }

                    public void onTick(long millisUntilFinished) {
                        // millisUntilFinished    The amount of time until finished.
                    }
                }.start();
            }
        });

    }

    private void saveUserData(View v) {
        User user = getApp().getPreferences().getProfile().getUser();
        getCompositeSubscription().add(getAPIService().updateProfile(user).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CallbackWrapper<Response<BasicResponse<User>>>(this, v) {
            @Override
            protected void onSuccess(Response<BasicResponse<User>> response) {
                User u = response.body().getData();
                getApp().getPreferences().setPreference(Constants.PreferenceKeys.USER_DATA, new Gson().toJson(u));
                getApp().setUser();
            }

            @Override
            protected void onFailure(String message) {
                UiUtils.showToast(getActivity(), message);
            }
        }));
    }
}
