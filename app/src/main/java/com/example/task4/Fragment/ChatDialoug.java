package com.example.task4.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task4.Activity.ConfirmRides;
import com.example.task4.Adapter.ChatAdapter;
import com.example.task4.Adapter.CreateChat;
import com.example.task4.DataModels.Chat.ChatBody;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatDialoug extends DialogFragment {
    RidesRespons.Ride ride;
    CreateChat createChat;
    FloatingActionButton fab;
    EditText chattext;
    RecyclerView rvchat;
    ChatAdapter adapter;
    List<ChatBody> chatBodyList=new ArrayList<>();
    int flag ;
    int i=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_dialoug,container,false);
        fab = view.findViewById(R.id.fab_send_chat);
        chattext = view.findViewById(R.id.chat_text);
        rvchat = view.findViewById(R.id.rv_chat);
createChat = new CreateChat(String.valueOf(ride.rideId));

createChat.mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

       /* for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
            // Retrieve each message
            ChatBody message = messageSnapshot.getValue(ChatBody.class);

            chatBodyList.add(message);
            // Do something with the message, for example, add it to a list
            // messageList.add(message);
        }
        Log.d("fb",chatBodyList.toString());
       */
        // Now, you can use the retrieved messages as needed

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        // Handle errors, if any
        Log.e("Firebase", "Error reading data", databaseError.toException());
    }
});





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chattext.getText().toString().isEmpty())
                {
                    return;
                }
                ChatBody chatBody;
                if (flag == 0) {
                     chatBody = new ChatBody(ride.user.getName(), chattext.getText().toString(), "11111");


                }
                else
                {
                    chatBody = new ChatBody(ride.driver.getName(), chattext.getText().toString(), "11111");

                }
                createChat.mDatabase.push().setValue(chatBody);
                chattext.setText("");

            }
        });
       // adapter.addchat( createChat.recivenewmessage());
        createChat.mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               ChatBody message = snapshot.getValue(ChatBody.class);
                chatBodyList.add(message);
                if (adapter==null)
                {
                    String sender;
                    if (flag==0)
                    {
                        sender = ride.user.getName();
                    }
                    else {
                        sender=ride.driver.getName();
                    }
                    adapter = new ChatAdapter(requireActivity(),chatBodyList,sender);
                    rvchat.setLayoutManager(new LinearLayoutManager(requireActivity()));
                    rvchat.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();

                    Log.d("ChatAdapter","chat add count"+i);
                    i++;
                    scrollToBottom();
                }

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
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(false);


        }
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    public void setRide(RidesRespons.Ride ride,int flag) {
        this.ride= ride;
        this.flag=flag;
    }
    private void scrollToBottom() {
        if (adapter.getItemCount() > 0) {
            rvchat.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }
}
