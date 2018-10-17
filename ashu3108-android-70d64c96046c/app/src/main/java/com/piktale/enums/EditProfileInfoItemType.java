package com.piktale.enums;

public enum EditProfileInfoItemType {
    HIGH_SCHOOL("high_school", "High School"), SECONDARY_HIGH_SCHOOL("high_sec_school", "Secondary High School"), UNIVERSITY("university", "University"), COMPANY("company", "Company");


    private String description;
    private String displayName;

    EditProfileInfoItemType(String description) {
        this.description = description;
    }

    EditProfileInfoItemType(String description, String displayName) {
        this.description = description;
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public EditProfileInfoItemType getByDescription(String input) {
        EditProfileInfoItemType type = EditProfileInfoItemType.HIGH_SCHOOL;
        for (EditProfileInfoItemType t : EditProfileInfoItemType.values()) {
            if (input.equalsIgnoreCase(t.getDescription())) {
                type = t;
                break;
            }
        }
        return type;
    }

    public static String getDisplayNameByDesc(String input) {
        String n = "";
        for (EditProfileInfoItemType t : EditProfileInfoItemType.values()) {
            if (input.equalsIgnoreCase(t.getDescription())) {
                n = t.getDisplayName();
                break;
            }
        }
        return n;
    }


}
