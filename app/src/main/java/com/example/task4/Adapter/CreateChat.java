package com.example.task4.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.task4.DataModels.Chat.ChatBody;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateChat {
    public DatabaseReference mDatabase;
    ChatBody message;
   public CreateChat(String chatID){
       mDatabase = FirebaseDatabase.getInstance().getReference().child("chats").child(chatID).child("messages");
    }

    public List<ChatBody> receiveMessage(){
        List<ChatBody> cbody = null;
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve each message
                    ChatBody message = messageSnapshot.getValue(ChatBody.class);

                    cbody.add(message);
                    // Do something with the message, for example, add it to a list
                    // messageList.add(message);
                }
                Log.d("fb",cbody.toString());
                // Now, you can use the retrieved messages as needed

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
                Log.e("Firebase", "Error reading data", databaseError.toException());
            }
        });

        return cbody;
    }
    public ChatBody recivenewmessage() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Called when a new child (message) is added to the "messages" node
               message = dataSnapshot.getValue(ChatBody.class);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        return message;
    }
}