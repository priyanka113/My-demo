package com.piktale.views.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.views.assist.ControllableAppBarLayout;
import com.piktale.views.assist.FragmentAdapter;
import com.piktale.views.fragments.EditProfileEducationFragment;
import com.piktale.views.fragments.EditProfileInterestFragment;
import com.piktale.views.fragments.EditProfilePersonalFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.appbar)
    ControllableAppBarLayout appbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.pic)
    ImageView pic;

    List<Fragment> tabsFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setBackNavigation(true);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.text));
        appbar.setOnStateChangeListener(new ControllableAppBarLayout.OnStateChangeListener() {
            @Override
            public void onStateChange(ControllableAppBarLayout.State toolbarChange) {
                switch (toolbarChange) {
                    case EXPANDED:
                        collapsingToolbarLayout.setTitle("");
                        break;
                    case COLLAPSED:
                        collapsingToolbarLayout.setTitle("Edit Profile");
                        break;
                    default:
                        break;
                }
            }
        });
        bindData();
    }

    private void bindData() {
        List<String> titles = new ArrayList<>();

        titles.add("Personal");
        titles.add("Education");
        titles.add("Interest");

        Picasso.get().load(getApp().getProfile().getUser().getPicture()).resize(400, 220).centerCrop().onlyScaleDown().into(pic);

        tabsFragments.add(EditProfilePersonalFragment.getInstance());
        tabsFragments.add(EditProfileEducationFragment.getInstance());
        tabsFragments.add(EditProfileInterestFragment.getInstance());

        FragmentAdapter adapter =
                new FragmentAdapter(getSupportFragmentManager(), tabsFragments);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (tabLayout.getTabAt(i).getCustomView() == null) {
                View tabView = LayoutInflater.from(this).inflate(R.layout.layout_auth_tab, null);
                TextView t = (TextView) tabView.findViewById(R.id.title);
                t.setText(titles.get(i));
                tabLayout.getTabAt(i).setCustomView(tabView);
            }
        }
    }
}

