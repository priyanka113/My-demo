package com.piktale.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piktale.R;
import com.piktale.models.ProfileGallery;
import com.piktale.models.ProfileTag;
import com.piktale.models.User;
import com.piktale.views.adapter.GalleryRecyclerViewAdapter;
import com.piktale.views.adapter.TaggedRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileTaggedFragment extends BaseFragment {

    View view;

    TaggedRecyclerViewAdapter adapter;
    ArrayList<ProfileTag> items = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    int[] imgList = {R.drawable.two, R.drawable.one, R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.two, R.drawable.one, R.drawable.three, R.drawable.four,
            R.drawable.five};

    public static ProfileTaggedFragment getInstance() {
        ProfileTaggedFragment fragment = new ProfileTaggedFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_tagged, container, false);
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
        for (int i = 0; i < 15; i++) {
            items.add(new ProfileTag(String.valueOf(i), "https://images.ctfassets.net/o59xlnp87tr5/6NVEIec3ZumgAagm4ye8C8/14471b28ae19f1f66423591cb5ca3cc3/shutterstock_380243965.jpg?w=360&h=240&fit=fill", imgList[i]));
        }
        adapter = new TaggedRecyclerViewAdapter(items, getActivity());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
