package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginParameter implements Serializable {
@SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    //constructor
    public LoginParameter(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter Methods

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter Methods

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
