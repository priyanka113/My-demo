package com.piktale.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.piktale.BR;
import com.piktale.enums.EditPersonalInfoType;
import com.piktale.enums.EditProfileInfoType;

public class CommonProfileInfo extends BaseObservable {
    private String _id;
    private String updatedAt;
    private String createdAt;
    private String toYear;
    private String fromYear;
    private String designation;
    private String metaValue;
    private String metaName;
    private EditPersonalInfoType editPersonalInfoType;
    private EditProfileInfoType editProfileInfoType;
    private String type;
    @Bindable
    private boolean showForm;

    public CommonProfileInfo() {
    }

    public CommonProfileInfo(String metaValue, String designation, String toYear, String fromYear) {
        this.toYear = toYear;
        this.fromYear = fromYear;
        this.designation = designation;
        this.metaValue = metaValue;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getToYear() {
        return toYear;
    }

    public void setToYear(String toYear) {
        this.toYear = toYear;
    }

    public String getFromYear() {
        return fromYear;
    }

    public void setFromYear(String fromYear) {
        this.fromYear = fromYear;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public String getMetaName() {
        return metaName;
    }

    public void setMetaName(String metaName) {
        this.metaName = metaName;
    }

    public EditPersonalInfoType getEditPersonalInfoType() {
        return editPersonalInfoType;
    }

    public void setEditPersonalInfoType(EditPersonalInfoType editPersonalInfoType) {
        this.editPersonalInfoType = editPersonalInfoType;
    }

    public EditProfileInfoType getEditProfileInfoType() {
        return editProfileInfoType;
    }

    public void setEditProfileInfoType(EditProfileInfoType editProfileInfoType) {
        this.editProfileInfoType = editProfileInfoType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EditProfileInfoType getTypeEnum() {
        EditProfileInfoType t = null;
        if (type != null) {
            t = EditProfileInfoType.valueOf(type.toUpperCase());
        }
        return t;
    }

    public boolean isShowForm() {
        return showForm;
    }

    public void setShowForm(boolean showForm) {
        this.showForm = showForm;
        notifyPropertyChanged(BR.showForm);
    }
}
