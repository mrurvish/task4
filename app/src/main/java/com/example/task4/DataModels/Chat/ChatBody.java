package com.example.task4.DataModels.Chat;

public class ChatBody {
    public String sender;

    public String message;
    public String timestamp;

    public ChatBody(String sender, String text, String timestamp) {
        this.sender = sender;
        this.message = text;
        this.timestamp = timestamp;
    }
    public ChatBody(){

    }
}
