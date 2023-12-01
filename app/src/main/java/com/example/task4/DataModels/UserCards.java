package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCards {
    @SerializedName("cards")
    private List<Card> cards;
    @SerializedName("defaultCard")
    private String defaultCard;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(String defaultCard) {
        this.defaultCard = defaultCard;
    }
public static class Id{
        @SerializedName("id")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
    public static class Card {

        @SerializedName("id")
        private String id;

        @SerializedName("object")
        private String object;

        @SerializedName("address_city")
        private String addressCity;

        @SerializedName("address_country")
        private String addressCountry;

        @SerializedName("address_line1")
        private String addressLine1;

        @SerializedName("address_line1_check")
        private String addressLine1Check;

        @SerializedName("address_line2")
        private String addressLine2;

        @SerializedName("address_state")
        private String addressState;

        @SerializedName("address_zip")
        private String addressZip;

        @SerializedName("address_zip_check")
        private String addressZipCheck;

        @SerializedName("brand")
        private String brand;

        @SerializedName("country")
        private String country;

        @SerializedName("customer")
        private String customer;

        @SerializedName("cvc_check")
        private String cvcCheck;

        @SerializedName("dynamic_last4")
        private String dynamicLast4;

        @SerializedName("exp_month")
        private int expMonth;

        @SerializedName("exp_year")
        private int expYear;

        @SerializedName("fingerprint")
        private String fingerprint;

        @SerializedName("funding")
        private String funding;

        @SerializedName("last4")
        private String last4;

        @SerializedName("name")
        private String name;

        @SerializedName("tokenization_method")
        private String tokenizationMethod;

        @SerializedName("wallet")
        private String wallet;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getAddressCity() {
            return addressCity;
        }

        public void setAddressCity(String addressCity) {
            this.addressCity = addressCity;
        }

        public String getAddressCountry() {
            return addressCountry;
        }

        public void setAddressCountry(String addressCountry) {
            this.addressCountry = addressCountry;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine1Check() {
            return addressLine1Check;
        }

        public void setAddressLine1Check(String addressLine1Check) {
            this.addressLine1Check = addressLine1Check;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getAddressState() {
            return addressState;
        }

        public void setAddressState(String addressState) {
            this.addressState = addressState;
        }

        public String getAddressZip() {
            return addressZip;
        }

        public void setAddressZip(String addressZip) {
            this.addressZip = addressZip;
        }

        public String getAddressZipCheck() {
            return addressZipCheck;
        }

        public void setAddressZipCheck(String addressZipCheck) {
            this.addressZipCheck = addressZipCheck;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getCvcCheck() {
            return cvcCheck;
        }

        public void setCvcCheck(String cvcCheck) {
            this.cvcCheck = cvcCheck;
        }

        public String getDynamicLast4() {
            return dynamicLast4;
        }

        public void setDynamicLast4(String dynamicLast4) {
            this.dynamicLast4 = dynamicLast4;
        }

        public int getExpMonth() {
            return expMonth;
        }

        public void setExpMonth(int expMonth) {
            this.expMonth = expMonth;
        }

        public int getExpYear() {
            return expYear;
        }

        public void setExpYear(int expYear) {
            this.expYear = expYear;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        public String getFunding() {
            return funding;
        }

        public void setFunding(String funding) {
            this.funding = funding;
        }

        public String getLast4() {
            return last4;
        }

        public void setLast4(String last4) {
            this.last4 = last4;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTokenizationMethod() {
            return tokenizationMethod;
        }

        public void setTokenizationMethod(String tokenizationMethod) {
            this.tokenizationMethod = tokenizationMethod;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }
    }
}
