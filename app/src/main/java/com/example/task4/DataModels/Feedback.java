package com.example.task4.DataModels;

import com.google.gson.annotations.SerializedName;

public class Feedback {
    @SerializedName("id")
    private String id;
    @SerializedName("rating")
    private String rating;
    @SerializedName("feedback")
    private String feedback;

    public Feedback(String id, String rating, String feedback) {
        this.id = id;
        this.rating = rating;
        this.feedback = feedback;
    }
    public static class FeedbackResponce{
        @SerializedName("msg")
        private String message ;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
