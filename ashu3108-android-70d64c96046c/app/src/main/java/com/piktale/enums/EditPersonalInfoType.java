package com.piktale.enums;

public enum EditPersonalInfoType {
    NAME("Name"), USERNAME("Username"), DOB("Date Of Birth"), STATUS("Status"), MOBILE("Mobile"), EMAIL("E-mail Address"),
    LIVING("Living"), GENDER("Gender"), RELATIONSHIP_STATUS("Relationship Status"), LANGUAGE("Language");

    private String title;

    EditPersonalInfoType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
