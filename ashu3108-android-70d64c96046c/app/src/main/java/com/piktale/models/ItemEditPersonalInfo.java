package com.piktale.models;

import com.piktale.enums.EditPersonalInfoType;
import com.piktale.enums.FieldType;

public class ItemEditPersonalInfo {

    private FieldType fieldType;
    private EditPersonalInfoType editPersonalInfoType;
    private String value;
    private String title;
    private boolean privacyRequired = false;
    private boolean editRequired = false;

    public ItemEditPersonalInfo(FieldType fieldType, EditPersonalInfoType editPersonalInfoType, String value, String title, boolean privacyRequired, boolean editRequired) {
        this.fieldType = fieldType;
        this.editPersonalInfoType = editPersonalInfoType;
        this.value = value;
        this.title = title;
        this.privacyRequired = privacyRequired;
        this.editRequired = editRequired;
    }

    public ItemEditPersonalInfo() {
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public EditPersonalInfoType getEditPersonalInfoType() {
        return editPersonalInfoType;
    }

    public void setEditPersonalInfoType(EditPersonalInfoType editPersonalInfoType) {
        this.editPersonalInfoType = editPersonalInfoType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPrivacyRequired() {
        return privacyRequired;
    }

    public void setPrivacyRequired(boolean privacyRequired) {
        this.privacyRequired = privacyRequired;
    }

    public boolean isEditRequired() {
        return editRequired;
    }

    public void setEditRequired(boolean editRequired) {
        this.editRequired = editRequired;
    }
}
