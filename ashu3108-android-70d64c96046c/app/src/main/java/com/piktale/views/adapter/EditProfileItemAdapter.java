package com.piktale.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.piktale.R;
import com.piktale.databinding.ItemEditPersonalInfoBinding;
import com.piktale.databinding.ItemEditProfileBinding;
import com.piktale.databinding.ItemEditProfileItemBinding;
import com.piktale.enums.EditProfileInfoItemType;
import com.piktale.models.CommonProfileInfo;
import com.piktale.models.EditProfile;
import com.piktale.models.EditProfileItem;
import com.piktale.utils.Constants;
import com.piktale.utils.Util;
import com.piktale.views.adapter.decorations.VerticalSpaceItemDecoration;
import com.piktale.views.assist.Dialogs;
import com.piktale.views.fragments.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditProfileItemAdapter extends RecyclerView
        .Adapter<EditProfileItemAdapter
        .DataObjectHolder> {
    private final int parentId;
    private RecyclerView recyclerView;
    private List<EditProfileItem> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        private ItemEditProfileItemBinding binding;

        public DataObjectHolder(ItemEditProfileItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.add.setOnClickListener(this);
            binding.save.setOnClickListener(this);
            binding.fromDate.setOnClickListener(this);
            binding.toDate.setOnClickListener(this);
        }

        public void bind(EditProfileItem item) {
            binding.setInfo(item);
            binding.executePendingBindings();
        }

        public ItemEditProfileItemBinding getBinding() {
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

    public EditProfileItemAdapter(List<EditProfileItem> myDataset, Context context, int parentId, RecyclerView recyclerView) {
        this.context = context;
        mDataset = myDataset;
        this.parentId = parentId;
        this.recyclerView = recyclerView;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEditProfileItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_edit_profile_item, parent, false);
        context = parent.getContext();
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        EditProfileItem item = mDataset.get(position);
        holder.binding.add.setTag(parentId);
        holder.binding.toDate.setTag(parentId);
        holder.binding.fromDate.setTag(parentId);
        holder.binding.save.setTag(parentId);
        EditProfileInfoDisplayAdapter adapter = new EditProfileInfoDisplayAdapter(item.getItems(), context, position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        VerticalSpaceItemDecoration decoration = new VerticalSpaceItemDecoration(24, true);
        if (holder.binding.items.getItemDecorationCount() == 0)
            holder.binding.items.addItemDecoration(decoration);
        if (holder.binding.items.getLayoutManager() == null)
            holder.binding.items.setLayoutManager(linearLayoutManager);
        holder.binding.items.setAdapter(adapter);

        adapter.setOnItemClickListener(new EditProfileInfoDisplayAdapter.MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                final int id = (int) v.getTag();

                View pv = recyclerView.getChildAt(id);
                RecyclerView ircv = pv.findViewById(R.id.items);
                View vh = ircv.getChildAt(position);

                final EditText input = vh.findViewById(R.id.input);
                final EditText designation = vh.findViewById(R.id.designation);
                final EditText toDate = vh.findViewById(R.id.toDate);
                final EditText fromDate = vh.findViewById(R.id.fromDate);
                final TextView error = vh.findViewById(R.id.error);

                EditProfileItem editProfileItem = mDataset.get(id);
                CommonProfileInfo cpi = editProfileItem.getItems().get(position);

                if (v.getId() == R.id.privacy) {
                    Dialogs.showPopupMenu(context, R.menu.popup_privacy, v, new Dialogs.PopupMenuListener() {
                        @Override
                        public void onSelected(MenuItem item) {

                        }
                    });
                } else if (v.getId() == R.id.edit) {
                    Dialogs.showPopupMenu(context, R.menu.popup_edit, v, new Dialogs.PopupMenuListener() {
                        @Override
                        public void onSelected(MenuItem item) {
                            if (item.getItemId() == R.id.action_edit) {
                                cpi.setShowForm(cpi.isShowForm() ? false : true);
                            } else if (item.getItemId() == R.id.action_delete) {
                                mDataset.get(id).getItems().remove(position);
                                notifyItemChanged(position, mDataset.get(id));
                            }
                        }
                    });
                } else if (v.getId() == R.id.fromDate) {
                    DatePickerFragment.newInstance(new DatePickerFragment.DatePickerFragmentListener() {
                        @Override
                        public void onDateSet(Calendar calendar) {
                            String date = Util.getDisplayDate(calendar, Constants.DATE_FORMATS.MMM_YYYY);
                            ((EditText) v).setText(date);
                            ((TextInputLayout) ((EditText) v).getParentForAccessibility()).setTag(calendar);
                        }
                    }).show(((AppCompatActivity) context).getSupportFragmentManager(), "Select Date");
                } else if (v.getId() == R.id.toDate) {
                    if (Util.textIsEmpty(fromDate.getText().toString())) {
                        error.setText("Please select Joining Date");
                        return;
                    }
                    Calendar calendar = (Calendar) ((TextInputLayout) ((EditText) fromDate).getParentForAccessibility()).getTag();
                    DatePickerFragment.newInstance(new DatePickerFragment.DatePickerFragmentListener() {
                        @Override
                        public void onDateSet(Calendar calendar) {
                            String date = Util.getDisplayDate(calendar, Constants.DATE_FORMATS.MMM_YYYY);
                            ((EditText) v).setText(date);
                            ((TextInputLayout) ((EditText) v).getParentForAccessibility()).setTag(calendar);
                            error.setText("");
                        }
                    }, calendar, "backward").show(((AppCompatActivity) context).getSupportFragmentManager(), "Select Date");
                } else if (v.getId() == R.id.save) {
                    error.setText("");

                    if (Util.textIsEmpty(input.getText().toString())) {
                        error.setText(editProfileItem.getType() != EditProfileInfoItemType.COMPANY ? "Please enter School/Uinversity name" : "Please enter company name");
                    } else if (((TextInputLayout) designation.getParentForAccessibility()).getVisibility() == View.VISIBLE && Util.textIsEmpty(designation.getText().toString())) {
                        error.setText("Please enter designation");
                        designation.setError("--");
                    } else if (Util.textIsEmpty(toDate.getText().toString())) {
                        error.setText("Please select Joining Date");
                    } else if (Util.textIsEmpty(fromDate.getText().toString())) {
                        error.setText("Please select End Date");
                    } else {
                        Calendar from = (Calendar) ((TextInputLayout) ((EditText) fromDate).getParentForAccessibility()).getTag();
                        Calendar to = (Calendar) ((TextInputLayout) ((EditText) toDate).getParentForAccessibility()).getTag();

                        cpi.setFromYear(Util.getDatesForDisplayAndServer(from)[1]);
                        cpi.setToYear(Util.getDatesForDisplayAndServer(to)[1]);
                        cpi.setMetaValue(input.getText().toString());
                        cpi.setMetaName(editProfileItem.getType().getDescription());
                        if (((TextInputLayout) designation.getParentForAccessibility()).getVisibility() == View.VISIBLE)
                            cpi.setDesignation(designation.getText().toString());

                        cpi.setShowForm(cpi.isShowForm() ? false : true);
                        cpi.setType(editProfileItem.getType().getDescription());
                        editProfileItem.getItems().set(position, cpi);
                        notifyItemChanged(id, editProfileItem);
                    }
                }
            }
        });

        holder.bind(item);
    }

    public void addAll(List<EditProfileItem> items) {
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