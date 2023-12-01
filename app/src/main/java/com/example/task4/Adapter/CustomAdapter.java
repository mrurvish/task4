package com.example.task4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.task4.DataModels.RidesRespons;
import com.example.task4.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{
    private List<RidesRespons.Ride> dataList;
    private Context context;
    private OnItemClickListener clickListener;


    public CustomAdapter(Context context, List<RidesRespons.Ride> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public void filterlist(List<RidesRespons.Ride> filterlist)
    {
        this.dataList=filterlist;
        notifyDataSetChanged();
    }
    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txt_name,txt_requestid,txt_servicetype,txt_pickup,txt_dropoff,txt_date,txt_time,txt_price;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

           txt_name = mView.findViewById(R.id.txt_username_rv);
           txt_price = mView.findViewById(R.id.txt_price_rv);
           txt_requestid = mView.findViewById(R.id.txt_requestid_rv);
           txt_servicetype = mView.findViewById(R.id.txt_servicetype_rv);
           txt_pickup = mView.findViewById(R.id.txt_pickup_d);
           txt_dropoff = mView.findViewById(R.id.txt_dropoff_d);
           coverImage = mView.findViewById(R.id.profile_pic_rv);
           txt_date = mView.findViewById(R.id.txt_date_rv);
           txt_time = mView.findViewById(R.id.txt_time_rv);

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


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

    holder.txt_name.setText(dataList.get(position).user.getName());
    holder.txt_requestid.setText(String.valueOf(dataList.get(position).rideId));
    holder.txt_servicetype.setText(dataList.get(position).serviceType.getVehicleType());
    holder.txt_pickup.setText(dataList.get(position).pickUp);
    holder.txt_dropoff.setText(dataList.get(position).dropOff);
    holder.txt_price.setText("₹"+dataList.get(position).totalFare);
    holder.txt_time.setText(dataList.get(position).rideTime);
    holder.txt_date.setText(dataList.get(position).rideDate);
    String path = "http://192.168.0.215:3000/" + dataList.get(position).user.getProfile();

    Picasso.get()
            .load(path)
            .into(holder.coverImage);

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}