package com.piktale.enums;

public enum Gender {
    M("Male"), F("Female");

    private String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Gender getGenderByDescription(String input) {
        Gender gender = Gender.M;
        if (input.equalsIgnoreCase("female"))
            gender = Gender.F;
        return gender;
    }

}
