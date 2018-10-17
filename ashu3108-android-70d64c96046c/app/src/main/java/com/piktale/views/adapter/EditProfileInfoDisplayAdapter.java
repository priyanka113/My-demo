package com.piktale.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.piktale.R;
import com.piktale.databinding.ItemEditProfileInfoDisplayBinding;

import com.piktale.databinding.ItemEditProfileItemBinding;
import com.piktale.models.CommonProfileInfo;
import com.piktale.models.EditProfileItem;
import com.piktale.utils.UiUtils;
import com.piktale.utils.Util;

import java.util.ArrayList;
import java.util.List;


public class EditProfileInfoDisplayAdapter extends RecyclerView
        .Adapter<EditProfileInfoDisplayAdapter
        .DataObjectHolder> {
    private final int parentId;
    private List<CommonProfileInfo> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        private ItemEditProfileInfoDisplayBinding binding;

        public DataObjectHolder(ItemEditProfileInfoDisplayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.privacy.setOnClickListener(this);
            binding.edit.setOnClickListener(this);
            binding.fromDate.setOnClickListener(this);
            binding.toDate.setOnClickListener(this);
            binding.save.setOnClickListener(this);
        }

        public void bind(CommonProfileInfo item) {
            binding.setInfo(item);
            binding.executePendingBindings();
        }

        public ItemEditProfileInfoDisplayBinding getBinding() {
            return binding;
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

    public EditProfileInfoDisplayAdapter(List<CommonProfileInfo> myDataset, Context context, int parentId) {
        this.context = context;
        mDataset = myDataset;
        this.parentId = parentId;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEditProfileInfoDisplayBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_edit_profile_info_display, parent, false);
        context = parent.getContext();
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        CommonProfileInfo item = mDataset.get(position);
        holder.binding.privacy.setTag(parentId);
        holder.binding.edit.setTag(parentId);
        holder.binding.fromDate.setTag(parentId);
        holder.binding.toDate.setTag(parentId);
        holder.binding.save.setTag(parentId);
        holder.binding.logo.setImageDrawable(UiUtils.getGradientDrawable(item.getMetaName(), item.getMetaValue()));
        holder.bind(item);

    }

    public void addAll(List<CommonProfileInfo> items) {
        if (this.mDataset == null)
            this.mDataset = new ArrayList<>();
        this.mDataset.addAll(items);
    }

    public void add(CommonProfileInfo item) {
        if (this.mDataset == null)
            this.mDataset = new ArrayList<>();
        this.mDataset.add(item);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}