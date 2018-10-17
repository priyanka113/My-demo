package com.piktale.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piktale.R;
import com.piktale.databinding.ItemEditPersonalInfoBinding;
import com.piktale.enums.FieldType;
import com.piktale.models.ItemEditPersonalInfo;

import java.util.List;


public class EditPersonalInfoAdapter extends RecyclerView
        .Adapter<EditPersonalInfoAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "ChallengeRecyclerViewAdapter";
    //    private final ImageLoader imageLoader;
    private List<ItemEditPersonalInfo> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        ItemEditPersonalInfoBinding binding;

        public DataObjectHolder(ItemEditPersonalInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.privacy.setOnClickListener(this);
            this.binding.edit.setOnClickListener(this);
        }

        public void bind(ItemEditPersonalInfo item) {
            binding.setInfo(item);
            switch (item.getFieldType()) {
                case DATE:
                case SELECTION:
                    binding.input.setOnClickListener(this);
                    binding.input.setFocusable(false);
                    break;
            }
            binding.executePendingBindings();
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

    public EditPersonalInfoAdapter(List<ItemEditPersonalInfo> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEditPersonalInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_edit_personal_info, parent, false);
        context = parent.getContext();
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
//        holder.setIsRecyclable(true);
        ItemEditPersonalInfo item = mDataset.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}