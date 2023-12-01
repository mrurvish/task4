package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile")
    private String profile;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneCode")
    private String phoneCode;

    @SerializedName("phone")
    private String phone;

    @SerializedName("cards")
    private String[] cards; // Assuming "cards" is an array of strings

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("__v")
    private int version;

    @SerializedName("stripeID")
    private String stripeID;
    @SerializedName("msg")
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User(String id, String name, String profile, String email, String phoneCode, String phone, String[] cards, String createdAt, String updatedAt, int version, String stripeID) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.email = email;
        this.phoneCode = phoneCode;
        this.phone = phone;
        this.cards = cards;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.stripeID = stripeID;
    }

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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String[] getCards() {
        return cards;
    }

    public void setCards(String[] cards) {
        this.cards = cards;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStripeID() {
        return stripeID;
    }

    public void setStripeID(String stripeID) {
        this.stripeID = stripeID;
    }
}
