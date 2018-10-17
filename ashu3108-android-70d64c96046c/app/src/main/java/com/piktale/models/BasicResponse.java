package com.piktale.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Member;
import java.util.List;

public class BasicResponse<T> {

    private T data;

    public BasicResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
