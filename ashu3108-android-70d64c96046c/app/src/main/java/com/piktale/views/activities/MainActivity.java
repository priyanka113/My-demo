package com.piktale.views.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.piktale.R;
import com.piktale.models.Profile;
import com.piktale.models.User;
import com.piktale.utils.Constants;
import com.piktale.utils.Util;
import com.piktale.views.assist.Dialogs;
import com.piktale.views.assist.FragmentAdapter;
import com.piktale.views.fragments.ExploreFragment;
import com.piktale.views.fragments.HomeFragment;
import com.piktale.views.fragments.NotificationsFragment;
import com.piktale.views.fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.home)
    Button home;

    @BindView(R.id.explore)
    Button explore;

    @BindView(R.id.notification)
    Button notification;

    @BindView(R.id.profile)
    Button profile;

    Button[] bottomNavs;

    List<Fragment> tabsFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomNavs = new Button[]{home, explore, notification, profile};

//        Util.changeSystemBarColor(this, R.color.white_transparent);

        if (!getApp().getPreferences().isUserNameDialogShowed()) {
            Dialogs.showUsernameDialog(this, new Dialogs.UsernameDialogListener() {
                @Override
                public void okay(String username) {
                    if (!Util.textIsEmpty(username)) {
                        Profile profile = getApp().getProfile();
                        User user = profile.getUser();
                        user.setUsername(username);
                        profile.setUser(user);
                        getApp().getPreferences().setPreference(Constants.PreferenceKeys.PROFILE_DATA, new Gson().toJson(profile));
                    }
                    getApp().getPreferences().setPreference(Constants.PreferenceKeys.USERNAME_DIALOG_SHOWED);
                }

                @Override
                public void cancel() {
                    getApp().getPreferences().setPreference(Constants.PreferenceKeys.USERNAME_DIALOG_SHOWED);
                }
            }, getApp());
        }


        tabsFragments.add(getHomeFragment());
        tabsFragments.add(getExploreFragment());
        tabsFragments.add(getNotificationsFragment());
        tabsFragments.add(getProfileFragment());

        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), tabsFragments);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);


//        addFragment(getHomeFragment(), Constants.FRAGMENT_TAG.HOME);
        activateDeactivate(home, true);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                activateDeactivate(bottomNavs[position], false);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private HomeFragment getHomeFragment() {
        return HomeFragment.getInstance();
    }

    private ExploreFragment getExploreFragment() {
        return ExploreFragment.getInstance();
    }

    private NotificationsFragment getNotificationsFragment() {
        return NotificationsFragment.getInstance();
    }

    private ProfileFragment getProfileFragment() {
        return ProfileFragment.getInstance();
    }


//    public void addFragment(Fragment fragment, String title) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
//        fragmentTransaction.replace(R.id.container, fragment, title);
//        if (fragmentManager.getFragments().size() > 0)
//            fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commitAllowingStateLoss();
//    }
//
//    public void popBackStackFragment() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if (fragmentManager.getFragments().size() > 1)
//            fragmentManager.popBackStack();
//        else
//            super.onBackPressed();
//    }

    @OnClick({R.id.home, R.id.explore, R.id.notification, R.id.profile})
    void onBottomNavClick(View v) {
        if (v.isSelected())
            return;
        activateDeactivate(v, true);
    }

    private void activateDeactivate(View v, boolean scrolViewpager) {
        for (int i = 0; i < bottomNavs.length; i++) {
            if (v.getId() == bottomNavs[i].getId()) {
                bottomNavs[i].setSelected(true);
                if (scrolViewpager)
                    viewpager.setCurrentItem(i, true);
            } else {
                bottomNavs[i].setSelected(false);
            }
        }
    }

}
