package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserPhone implements Serializable {
    @SerializedName("phoneCode")
    private String phoneCode;
    @SerializedName("phone")
    private String phone;

    public UserPhone(String phoneCode, String phone) {
        this.phoneCode = phoneCode;
        this.phone = phone;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
