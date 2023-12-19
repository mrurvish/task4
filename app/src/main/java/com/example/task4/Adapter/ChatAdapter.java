package com.example.task4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.task4.DataModels.Chat.ChatBody;
import com.example.task4.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder>{
    private List<ChatBody> dataList;
    private Context context;
    private ChatAdapter.OnItemClickListener clickListener;
    int flag ;
    String sender;


    public ChatAdapter(Context context, List<ChatBody> dataList,String sender) {
        this.context = context;
        this.dataList = dataList;
        this.sender = sender;
    }

    public List<ChatBody> updatestatus(ChatBody rideData) {
        for (int i = 0; i < dataList.size(); i++) {

            }

        return dataList;
    }
    public List<ChatBody> removeRide(ChatBody rideData) {

        for (int i = 0; i < dataList.size(); i++) {

        }
        return dataList;
    }

    public void setOnItemClickListener(ChatAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void addchat(ChatBody filterlist) {
        if (filterlist!=null) {
            dataList.add(filterlist);

            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {

        return dataList.get(position).sender.equals(sender) ? 0 : 1;
    }

    @Override
    public ChatAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType==0) {
            view= layoutInflater.inflate(R.layout.chat_sender_item, parent, false);
        }else {
            view = layoutInflater.inflate(R.layout.chat_reciver__item, parent, false);
        }
        return new ChatAdapter.CustomViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.CustomViewHolder holder, int position) {
if (dataList.get(position) == null) {
    return;
}
       holder.txt_sender.setText(dataList.get(position).sender);
        holder.txt_chat.setText(dataList.get(position).message);
        holder.txt_time.setText(dataList.get(position).timestamp);
  
       
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);


    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txt_chat, txt_sender, txt_time;



        CustomViewHolder(View itemView, int viewType) {
            super(itemView);
            mView = itemView;
            if (viewType==0) {
                txt_chat = mView.findViewById(R.id.message_chat_sender);
                txt_sender = mView.findViewById(R.id.user_chat_sender);
                txt_time = mView.findViewById(R.id.time_chat_sender);
            }else {
                txt_chat = mView.findViewById(R.id.message_chat_receiver);
                txt_sender = mView.findViewById(R.id.user_chat_receiver);
                txt_time = mView.findViewById(R.id.time_chat_receiver);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            clickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
