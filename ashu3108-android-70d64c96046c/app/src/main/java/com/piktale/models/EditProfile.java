package com.piktale.models;

import com.piktale.enums.EditPersonalInfoType;
import com.piktale.enums.EditProfileInfoType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfile {
    private String title;
    private EditProfileInfoType type;
    private List<EditProfileItem> items;

    public EditProfile() {
    }

    public EditProfile(String title, EditProfileInfoType type, List<EditProfileItem> items) {
        this.title = title;
        this.type = type;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EditProfileInfoType getEditPersonalInfoType() {
        return type;
    }

    public void setEditProfileInfoType(EditProfileInfoType type) {
        this.type = type;
    }

    public List<EditProfileItem> getItems() {
        return items;
    }

    public void setItems(List<EditProfileItem> items) {
        this.items = items;
    }

    public List<EditProfileItem> addItems(EditProfileItem... item) {
        if (this.items == null)
            this.items = new ArrayList<>();
        this.items.addAll(Arrays.asList(item));
        return this.items;
    }
}
