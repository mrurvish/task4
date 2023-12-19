package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

public class AllUsers {
    @SerializedName("userCount")
    private int userCount;

    @SerializedName("pageCount")
    private int pageCount;

    @SerializedName("users")
    private List<User> users;

    public AllUsers(int userCount, int pageCount, List<User> users) {
        this.userCount = userCount;
        this.pageCount = pageCount;
        this.users = users;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public static class Useredit{
        @SerializedName("name")
        private String name;

       @SerializedName("id")
       private String id;
        @SerializedName("email")
        private String email;
        @SerializedName("phoneCode")
        private String phoneCode;

        @SerializedName("phone")
        private String phone;
        @SerializedName("profile")
        private MultipartBody.Part part;

        public Useredit(String name, String email, String phoneCode, String phone,String id,MultipartBody.Part part) {
            this.name = name;
            this.id=id;
            this.email = email;
            this.phoneCode = phoneCode;
            this.phone = phone;
            this.part = part;

        }
        public Useredit(String name, String email, String phoneCode, String phone) {
            this.name = name;

            this.email = email;
            this.phoneCode = phoneCode;
            this.phone = phone;


        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    }
}
