package com.piktale.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.piktale.enums.Gender;

public class User implements Parcelable {

    @SerializedName("_id")
    private String id;
    private String name;
    private String email;
    private String phone;
    private String username;
    @SerializedName("isBusiness")
    private boolean business;
    private int verified;
    private Gender gender;
    private String deviceType;
    private String password;
    private String isPicture;
    private String picture;
    private String userToken;
    private int isActive;
    private String updatedAT;
    private String createdAt;
    private String newPassword;
    private String dob;
    private String hometown;
    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isBusiness() {
        return business;
    }

    public void setBusiness(boolean business) {
        this.business = business;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsPicture() {
        return isPicture;
    }

    public void setIsPicture(String isPicture) {
        this.isPicture = isPicture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getUpdatedAT() {
        return updatedAT;
    }

    public void setUpdatedAT(String updatedAT) {
        this.updatedAT = updatedAT;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.username);
        dest.writeByte(this.business ? (byte) 1 : (byte) 0);
        dest.writeInt(this.verified);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeString(this.deviceType);
        dest.writeString(this.password);
        dest.writeString(this.isPicture);
        dest.writeString(this.picture);
        dest.writeString(this.userToken);
        dest.writeInt(this.isActive);
        dest.writeString(this.updatedAT);
        dest.writeString(this.createdAt);
        dest.writeString(this.newPassword);
        dest.writeString(this.dob);
        dest.writeString(this.hometown);
        dest.writeString(this.language);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.username = in.readString();
        this.business = in.readByte() != 0;
        this.verified = in.readInt();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.deviceType = in.readString();
        this.password = in.readString();
        this.isPicture = in.readString();
        this.picture = in.readString();
        this.userToken = in.readString();
        this.isActive = in.readInt();
        this.updatedAT = in.readString();
        this.createdAt = in.readString();
        this.newPassword = in.readString();
        this.dob = in.readString();
        this.hometown = in.readString();
        this.language = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
