package com.example.task4.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task4.DataModels.RidesRespons;
import com.example.task4.DataModels.Status;
import com.example.task4.DataModels.User;
import com.example.task4.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.CustomViewHolder> {
    private List<User> dataList;
    private Context context;
    private UserAdapter.OnItemClickListener clickListener;


    public UserAdapter(Context context, List<User> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public List<User> removeRide(User rideData) {

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId().equals(rideData.getId()))
            {
                dataList.remove(i);
            }
        }
        notifyDataSetChanged();
        return dataList;
    }
    public List<User> updateRide(User rideData)
    {
        for (int i = 0; i < dataList.size(); i++) {

        if (dataList.get(i).getId().equals(rideData.getId()))
        {
            dataList.set(i,rideData);
        }

        }
        notifyDataSetChanged();
        return dataList;
    }
    public List<User> addRide(User ridedata)
    {

        dataList.add(ridedata);
        notifyDataSetChanged();
        return dataList;

    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void filterlist(List<User> filterlist) {
        this.dataList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public UserAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_items, parent, false);
        return new UserAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.CustomViewHolder holder, int position) {

            holder.txt_name.setText(dataList.get(position).getName());
            holder.txt_num.setText(dataList.get(position).getPhoneCode()+" "+dataList.get(position).getPhone());

            String path = "http://192.168.0.215:3000/" + dataList.get(position).getProfile();

            Picasso.get()
                    .load(path)
                    .into(holder.coverImage);


    }

    @Override
    public int getItemCount() {
        int size = dataList.size();
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void oneditUserclick(int position);
        void onDeleteClick(int position);
        void onCardClick(int position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txt_name, txt_num;

        private ImageView coverImage, btn_edit_user,btn_card_user,btn_delete_user;


        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txt_name = mView.findViewById(R.id.name_user);
           coverImage = mView.findViewById(R.id.profile_pic_user);
            txt_num = mView.findViewById(R.id.phone_user);
            btn_edit_user=mView.findViewById(R.id.edit_user);
            btn_card_user=mView.findViewById(R.id.card_user);
            btn_delete_user = mView.findViewById(R.id.delete_user);

            btn_edit_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Call a method or perform an action based on the button click
                        clickListener.oneditUserclick(position);
                    }
                }
            });
            btn_delete_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    clickListener.onDeleteClick(position);
                }
            });

            btn_card_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    clickListener.onCardClick(position);


                }
            });
        }
    }
}
