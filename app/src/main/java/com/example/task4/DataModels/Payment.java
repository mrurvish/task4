package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("status")
    private String paymentstatus;
    @SerializedName("amount")
    private int amount;

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public static class PaymentBody{
        @SerializedName("id")
        private String id;

        public PaymentBody(String id) {
            this.id = id;
        }
    }
}
