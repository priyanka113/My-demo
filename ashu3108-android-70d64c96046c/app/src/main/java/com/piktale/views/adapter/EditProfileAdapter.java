package com.piktale.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.piktale.R;
import com.piktale.databinding.ItemEditProfileBinding;
import com.piktale.databinding.ItemEditProfileItemBinding;
import com.piktale.enums.EditProfileInfoItemType;
import com.piktale.models.CommonProfileInfo;
import com.piktale.models.EditProfile;
import com.piktale.models.EditProfileItem;
import com.piktale.utils.Constants;
import com.piktale.utils.Util;
import com.piktale.views.adapter.decorations.VerticalSpaceItemDecoration;
import com.piktale.views.fragments.DatePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class EditProfileAdapter extends RecyclerView
        .Adapter<EditProfileAdapter
        .DataObjectHolder> {
    private List<EditProfile> mDataset;
    private RecyclerView recyclerView;
    private static MyClickListener myClickListener;
    private Context context;
    private List<DataObjectHolder> refHolder = new ArrayList<>();
    private EditProfileItem previousEditProfileItem;
    private DataChangedListener dataChangedListener;


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        ItemEditProfileBinding binding;

        public DataObjectHolder(ItemEditProfileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EditProfile item) {
            binding.setInfo(item);
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

    public void setOnDataChangedListener(DataChangedListener listener) {
        this.dataChangedListener = listener;
    }

    public EditProfileAdapter(List<EditProfile> myDataset, Context context, RecyclerView recyclerView) {
        this.context = context;
        mDataset = myDataset;
        this.recyclerView = recyclerView;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEditProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_edit_profile, parent, false);
        context = parent.getContext();
        return new DataObjectHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final EditProfile item = mDataset.get(position);

        final EditProfileItemAdapter adapter = new EditProfileItemAdapter(new ArrayList<>(), context, position, holder.binding.items);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        VerticalSpaceItemDecoration decoration = new VerticalSpaceItemDecoration(48, true);
        if (holder.binding.items.getItemDecorationCount() == 0)
            holder.binding.items.addItemDecoration(decoration);
        if (holder.binding.items.getLayoutManager() == null)
            holder.binding.items.setLayoutManager(linearLayoutManager);
        adapter.addAll(item.getItems());
        holder.binding.items.setAdapter(adapter);
        refHolder.add(position, holder);
        adapter.setOnItemClickListener(new EditProfileItemAdapter.MyClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                int id = (int) v.getTag();
                View pv = recyclerView.getChildAt(id);
                RecyclerView ircv = pv.findViewById(R.id.items);
                View vh = ircv.getChildAt(pos);
                final EditText input = vh.findViewById(R.id.input);
                final EditText designation = vh.findViewById(R.id.designation);
                final EditText toDate = vh.findViewById(R.id.toDate);
                final EditText fromDate = vh.findViewById(R.id.fromDate);

                EditProfileItem editProfileItem = mDataset.get(id).getItems().get(pos);

                TextView error = vh.findViewById(R.id.error);

                if (v.getId() == R.id.add) {
                    editProfileItem.setShowForm(editProfileItem.isShowForm() ? false : true);
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
                        CommonProfileInfo cpi = new CommonProfileInfo();

                        Calendar from = (Calendar) ((TextInputLayout) ((EditText) fromDate).getParentForAccessibility()).getTag();
                        Calendar to = (Calendar) ((TextInputLayout) ((EditText) toDate).getParentForAccessibility()).getTag();

                        cpi.setFromYear(Util.getDatesForDisplayAndServer(from)[1]);
                        cpi.setToYear(Util.getDatesForDisplayAndServer(to)[1]);
                        cpi.setMetaValue(input.getText().toString());
                        cpi.setMetaName(editProfileItem.getType().getDescription());
                        cpi.setType(mDataset.get(id).getEditPersonalInfoType().name());
                        if (((TextInputLayout) designation.getParentForAccessibility()).getVisibility() == View.VISIBLE)
                            cpi.setDesignation(designation.getText().toString());

                        Log.d("CommonProfileInfo", new Gson().toJson(cpi));

                        editProfileItem.getItems().add(cpi);
                        editProfileItem.setShowForm(editProfileItem.isShowForm() ? false : true);
                        notifyItemChanged(id, editProfileItem);
                        dataChangedListener.onDataChanged();
                    }
                } else if (v.getId() == R.id.fromDate) {
                    DatePickerFragment.newInstance(new DatePickerFragment.DatePickerFragmentListener() {
                        @Override
                        public void onDateSet(Calendar calendar) {
                            String date = Util.getDisplayDate(calendar, Constants.DATE_FORMATS.MMM_YYYY);
                            ((EditText) v).setText(date);
                            ((TextInputLayout) ((EditText) v).getParentForAccessibility()).setTag(calendar);
                            error.setText("");
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
                }
            }
        });

        holder.bind(item);
    }

    public void addAll(List<EditProfile> items) {
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

    public interface DataChangedListener {
        public void onDataChanged();
    }

}