package com.piktale.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.views.assist.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment {

    View view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    List<Fragment> tabsFragments = new ArrayList<>();

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
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

        toolbar.setNavigationIcon(R.drawable.ic_action_addfriend_dark);

        List<String> titles = new ArrayList<>();

        titles.add("Tales");
        titles.add("Feeds");
        titles.add("Chats");

        tabsFragments.add(TalesFragment.getInstance());
        tabsFragments.add(FeedsFragment.getInstance());
        tabsFragments.add(ChatsFragment.getInstance());

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

        toolbar.inflateMenu(R.menu.home);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });

    }

}

