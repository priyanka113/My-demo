package com.piktale.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.piktale.R;
import com.piktale.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoRecyclerViewAdapter extends RecyclerView
        .Adapter<PhotoRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
//    private final ImageLoader imageLoader;
    private List<Photo> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        @BindView(R.id.pic)
        ImageView pic;

        public DataObjectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            pic = (ImageView) itemView.findViewById(R.id.pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null)
                myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PhotoRecyclerViewAdapter(List<Photo> myDataset, Context context) {
//        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
//        holder.setIsRecyclable(true);
        Photo item = mDataset.get(position);
        Log.d("getCoverPhoto", item.getUri());
//        imageLoader.displayImage("file://" + item.getUri(), holder.pic);
        holder.itemView.setTag(item);
        Picasso.get().load("file://" + item.getUri()).resize(160, 160).centerCrop().onlyScaleDown().into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}