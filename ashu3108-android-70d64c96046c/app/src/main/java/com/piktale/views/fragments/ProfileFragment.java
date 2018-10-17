package com.piktale.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.models.User;
import com.piktale.utils.Constants;
import com.piktale.utils.DateUtil;
import com.piktale.utils.Util;
import com.piktale.views.activities.EditProfileActivity;
import com.piktale.views.assist.ControllableAppBarLayout;
import com.piktale.views.assist.FragmentAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    View view;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appbar)
    ControllableAppBarLayout appbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.nestedscrollview)
    NestedScrollView nestedscrollview;

    @BindView(R.id.username)
    TextView username;

    @BindView(R.id.fullname)
    TextView fullname;

    @BindView(R.id.location)
    TextView location;

    @BindView(R.id.dob)
    TextView dob;

    @BindView(R.id.pic)
    ImageView pic;

    List<Fragment> tabsFragments = new ArrayList<>();

    public static ProfileFragment getInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_profile);
//        ButterKnife.bind(this);
//        setBackNavigation(false);
//
//        Util.changeSystemBarColor(this, R.color.white_transparent);
//
//        setOptionMenu(R.menu.profile, new OptionMenuListener() {
//            @Override
//            public void onMenuClicked(MenuItem item) {
//
//            }
//        });
//
//
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
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

        User user = getApp().getProfile().getUser();

        username.setText(user.getUsername());
        fullname.setText(user.getName());
        location.setText(user.getHometown());
        dob.setText("N/A");
        if (!Util.textIsEmpty(user.getDob())) {
            dob.setText(DateUtil.getDisplayDate(user.getDob(), Constants.DATE_FORMATS.D_MMMM));
        }
        if (!Util.textIsEmpty(user.getPicture())) {
            Picasso.get().load(user.getPicture()).resize(400, 220).centerCrop().onlyScaleDown().into(pic);
        }

        List<String> titles = new ArrayList<>();

        titles.add("Gallery");
        titles.add("About");
        titles.add("tagged");

        tabsFragments.add(ProfileGalleryFragment.getInstance());
        tabsFragments.add(ProfileAboutFragment.getInstance());
        tabsFragments.add(ProfileTaggedFragment.getInstance());

        FragmentAdapter adapter =
                new FragmentAdapter(getChildFragmentManager(), tabsFragments);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (tabLayout.getTabAt(i).getCustomView() == null) {
                View tabView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_auth_tab, null);
                TextView t = (TextView) tabView.findViewById(R.id.title);
                t.setText(titles.get(i));
                tabLayout.getTabAt(i).setCustomView(tabView);
            }
        }

        toolbar.inflateMenu(R.menu.profile);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.text));

        appbar.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override
            public void onStateChange(ControllableAppBarLayout.State toolbarChange) {
                switch (toolbarChange) {
                    case EXPANDED:
                        collapsingToolbarLayout.setTitle("");
                        break;
                    case COLLAPSED:
                        collapsingToolbarLayout.setTitle("Profile");
                        break;
                    default:
                        break;
                }
            }
        });


    }

}

