package com.piktale.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.piktale.R;
import com.piktale.models.Photo;
import com.piktale.models.ProfileTag;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TaggedRecyclerViewAdapter extends RecyclerView
        .Adapter<TaggedRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "GalleryRecyclerViewAdapter";
    //    private final ImageLoader imageLoader;
    private List<ProfileTag> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView photo;

        public DataObjectHolder(View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
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

    public TaggedRecyclerViewAdapter(List<ProfileTag> myDataset, Context context) {
//        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staggered, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
//        holder.setIsRecyclable(true);
        ProfileTag item = mDataset.get(position);
        Log.d("getCoverPhoto", item.getPicture());
//        imageLoader.displayImage("file://" + item.getUri(), holder.pic);
        holder.itemView.setTag(item);

//        Picasso.get().load(item.getResId()).into(holder.photo);
        Picasso.get().load(item.getPicture()).into(holder.photo);
//        holder.photo.setImageResource(item.getResId());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}