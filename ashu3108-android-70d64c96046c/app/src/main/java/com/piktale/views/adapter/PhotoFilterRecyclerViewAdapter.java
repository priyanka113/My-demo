package com.piktale.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.models.ThumbnailItem;

import java.util.List;

public class PhotoFilterRecyclerViewAdapter extends RecyclerView
        .Adapter<PhotoFilterRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    //    private final ImageLoader imageLoader;
    private List<ThumbnailItem> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView pic;
        TextView name;

        public DataObjectHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
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

    public PhotoFilterRecyclerViewAdapter(List<ThumbnailItem> myDataset, Context context) {
//        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        this.context = context;
        mDataset = myDataset;
    }

    public void addItems(List<ThumbnailItem> items) {
        mDataset = items;
        notifyDataSetChanged();
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_filter, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ThumbnailItem item = mDataset.get(position);
        holder.itemView.setTag(item);
        holder.name.setText(item.getName());
        holder.pic.setImageBitmap(item.getImage());
        holder.itemView.setTag(item);
        holder.itemView.setAlpha(item.getAlpha());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public void resetAlphs() {

        for (int i = 0; i < mDataset.size(); i++) {
            if (mDataset.get(i).getAlpha() != 1.0f) {
                mDataset.get(i).setAlpha(1.0f);
                notifyItemChanged(i, mDataset.get(i));
            } else {
                mDataset.get(i).setAlpha(1.0f);
            }

        }
    }

}