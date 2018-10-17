package com.piktale.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piktale.R;
import com.piktale.databinding.ItemWorkInfoBinding;
import com.piktale.models.CommonProfileInfo;
import com.piktale.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;


public class WorkItemAdapter extends RecyclerView
        .Adapter<WorkItemAdapter
        .DataObjectHolder> {
    private List<CommonProfileInfo> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        private ItemWorkInfoBinding binding;

        public DataObjectHolder(ItemWorkInfoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CommonProfileInfo item) {
            binding.setInfo(item);
            binding.executePendingBindings();
        }

        public ItemWorkInfoBinding getBinding() {
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

    public WorkItemAdapter(List<CommonProfileInfo> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemWorkInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_work_info, parent, false);
        context = parent.getContext();
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        CommonProfileInfo item = mDataset.get(position);
        holder.binding.logo.setImageDrawable(UiUtils.getGradientDrawable(item.getMetaName(), item.getMetaValue()));
        holder.bind(item);
    }

    public void addAll(List<CommonProfileInfo> items) {
        if (this.mDataset == null)
            this.mDataset = new ArrayList<>();
        this.mDataset.addAll(items);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}