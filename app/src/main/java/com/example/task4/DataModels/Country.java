package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable {

    @SerializedName("_id")
        public String _id;
    @SerializedName("name")
        public String name;
    @SerializedName("flag")
        public String flag;
    @SerializedName("currency")
        public String currency;
    @SerializedName("timezone")
        public String timezone;
    @SerializedName("code")
        public String code;
    @SerializedName("alphaCode")
        public String alphaCode;
    @SerializedName("latLong")
        public ArrayList<Double> latLong;
    @SerializedName("__v")
        public int __v;

    public Country(String _id, String name, String flag, String currency, String timezone, String code, String alphaCode, ArrayList<Double> latLong, int __v) {
        this._id = _id;
        this.name = name;
        this.flag = flag;
        this.currency = currency;
        this.timezone = timezone;
        this.code = code;
        this.alphaCode = alphaCode;
        this.latLong = latLong;
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlphaCode() {
        return alphaCode;
    }

    public void setAlphaCode(String alphaCode) {
        this.alphaCode = alphaCode;
    }

    public ArrayList<Double> getLatLong() {
        return latLong;
    }

    public void setLatLong(ArrayList<Double> latLong) {
        this.latLong = latLong;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
