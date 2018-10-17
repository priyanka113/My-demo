package com.piktale.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.piktale.BR;
import com.piktale.enums.EditProfileInfoItemType;

import java.util.ArrayList;
import java.util.List;

public class EditProfileItem extends BaseObservable {
    private String title;
    private List<CommonProfileInfo> items = new ArrayList<>();
    @Bindable
    private boolean showForm;
    private EditProfileInfoItemType type;


    public EditProfileItem(String title, List<CommonProfileInfo> items) {
        this.title = title;
        this.items = items;
    }

    public EditProfileItem(String title, List<CommonProfileInfo> items, EditProfileInfoItemType type) {
        this.title = title;
        this.items = items;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommonProfileInfo> getItems() {
        return items;
    }

    public void setItems(List<CommonProfileInfo> items) {
        this.items = items;
    }

    public boolean isShowForm() {
        return showForm;
    }

    public void setShowForm(boolean showForm) {
        this.showForm = showForm;
        notifyPropertyChanged(BR.showForm);
    }

    public EditProfileInfoItemType getType() {
        return type;
    }

    public void setType(EditProfileInfoItemType type) {
        this.type = type;
    }
}
