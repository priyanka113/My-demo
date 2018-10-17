package com.piktale.models.requestBody;

import com.google.gson.annotations.SerializedName;
import com.piktale.enums.Gender;

import okhttp3.RequestBody;

public class Register1 {

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




}
