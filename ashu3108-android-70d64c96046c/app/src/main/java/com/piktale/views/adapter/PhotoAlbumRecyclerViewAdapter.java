package com.piktale.views.adapter;

import android.content.Context;
import android.net.Uri;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piktale.R;
import com.piktale.models.PhotoAlbum;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAlbumRecyclerViewAdapter extends RecyclerView
        .Adapter<PhotoAlbumRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    private List<PhotoAlbum> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name;
        ImageView pic;
        LinearLayout camera;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            camera = (LinearLayout) itemView.findViewById(R.id.camera);
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

    public PhotoAlbumRecyclerViewAdapter(List<PhotoAlbum> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_album, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setIsRecyclable(true);
        PhotoAlbum item = mDataset.get(position);

        if (item.getId() == -100) {
            holder.camera.setVisibility(View.VISIBLE);
        } else {
            holder.camera.setVisibility(View.GONE);
            holder.name.setText(item.getName());
            Log.d("getCoverPhoto", item.getCoverPhoto());
            Uri uri = Uri.parse(item.getCoverPhoto());

            if (uri.toString().contains("content://")) {
                Picasso.get().load(Uri.parse(item.getCoverPhoto())).resize(240, 240).centerCrop().onlyScaleDown().into(holder.pic);
            } else {
                Picasso.get().load(Uri.parse("file://" + item.getCoverPhoto())).resize(240, 240).centerCrop().onlyScaleDown().into(holder.pic);
            }


        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}