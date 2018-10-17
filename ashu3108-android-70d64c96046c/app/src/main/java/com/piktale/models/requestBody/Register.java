package com.piktale.models.requestBody;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.piktale.enums.Gender;

public class Register {

    private String name;
    private String email;
    private String phone;
    private String username;
    @SerializedName("isBusiness")
    private String business;
    private Gender gender;
    private String regType;
    private String password;
    private int isPicture;
    private String photo;

    public Register() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String isBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsPicture() {
        return isPicture;
    }

    public void setIsPicture(int isPicture) {
        this.isPicture = isPicture;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
