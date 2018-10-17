package com.piktale.views.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.models.ThumbnailItem;
import com.piktale.utils.Constants;
import com.piktale.utils.ThumbnailsManager;
import com.piktale.views.adapter.PhotoFilterRecyclerViewAdapter;
import com.piktale.views.adapter.decorations.EqualSpacingItemDecoration;
import com.appolica.flubber.Flubber;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class SelectedPhotoFilterActivity extends BaseActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    public static final String IMAGE_PATH = "image";
    public static final String FILTER_POSITION = "filter_position";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pic)
    ImageView pic;

    @BindView(R.id.filter_label)
    TextView filterLabel;

    @BindView(R.id.filters)
    RecyclerView filters;

    @BindView(R.id.buttons)
    LinearLayout buttons;

    @BindView(R.id.progress)
    ProgressBar progress;

    private String imagePath;

    PhotoFilterRecyclerViewAdapter adapter;
    private Bitmap origThumbImageBitmap;
    private int selectedFilterPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo_filter);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setBackNavigation(true);

        progress.setVisibility(View.GONE);

        toolbar.setTitle("");

        imagePath = getIntent().getStringExtra(IMAGE_PATH);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (imagePath.contains("content://")) {
            pic.setImageURI(Uri.parse(imagePath));
        } else {
            pic.setImageURI(Uri.parse("file://" + imagePath));
        }

        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(200)                              // Last for 1000 milliseconds(1 second)
                .createFor(pic)                             // Apply it to the view
                .start();

        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(400)                              // Last for 1000 milliseconds(1 second)
                .createFor(filterLabel)                             // Apply it to the view
                .start();

        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(600)                              // Last for 1000 milliseconds(1 second)
                .createFor(filters)                             // Apply it to the view
                .start();

        Flubber.with()
                .animation(Flubber.AnimationPreset.SLIDE_LEFT) // Slide up animation
                .repeatCount(0)                              // Repeat once
                .duration(800)                              // Last for 1000 milliseconds(1 second)
                .createFor(buttons)                             // Apply it to the view
                .start();

        adapter = new PhotoFilterRecyclerViewAdapter(new ArrayList<ThumbnailItem>(), this);
        adapter.setOnItemClickListener(new PhotoFilterRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                selectedFilterPosition = position;
                ThumbnailItem thumbnailItem = (ThumbnailItem) v.getTag();
                if (position != 0) {
                    progress.setVisibility(View.VISIBLE);
                    Filter filter = Constants.filtersMap[position];
                    Bitmap filteredBitmap = filter.processFilter(origThumbImageBitmap.copy(Bitmap.Config.ARGB_8888, true));
                    filteredBitmap = filter.processFilter(filteredBitmap);
                    pic.setImageBitmap(filteredBitmap);
                    progress.setVisibility(View.GONE);
                } else {
                    pic.setImageBitmap(null);
                    if (imagePath.contains("content://")) {
                        pic.setImageURI(Uri.parse(imagePath));
                    } else {
                        pic.setImageURI(Uri.parse("file://" + imagePath));
                    }
                }
                adapter.resetAlphs();
                thumbnailItem.setAlpha(0.5f);
                adapter.notifyItemChanged(position, thumbnailItem);
            }
        });
        setRecyclerViewLayoutManager(LinearLayoutManager.HORIZONTAL, filters);
        filters.setAdapter(adapter);
        filters.addItemDecoration(new EqualSpacingItemDecoration(16, 0));
        bindDataToAdapter();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                origThumbImageBitmap = ((BitmapDrawable) pic.getDrawable()).getBitmap();
                ThumbnailsManager.clearThumbs();
                String[] filterNames = new String[]{"Normal", "Star Lit", "Blue Mess", "Awe Struck", "Lime", "Night"};

                for (int i = 0; i < Constants.filtersMap.length; i++) {
                    ThumbnailItem thumb = new ThumbnailItem();
                    thumb.setImage(origThumbImageBitmap);
                    if (i != 0) {
                        thumb.setFilter(Constants.filtersMap[i]);
                    }
                    thumb.setName(filterNames[i]);
                    ThumbnailsManager.addThumb(thumb); // Original Image
                }

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);
                adapter.addItems(thumbs);
            }
        };
        handler.post(r);
    }

    @OnClick(R.id.cancle)
    void onCancle(View view) {
        onBackPressed();
    }

    @OnClick(R.id.done)
    void onDone(View view) {
        Intent intent = new Intent();
        intent.putExtra(FILTER_POSITION, selectedFilterPosition);
        intent.putExtra(IMAGE_PATH, imagePath);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

}

