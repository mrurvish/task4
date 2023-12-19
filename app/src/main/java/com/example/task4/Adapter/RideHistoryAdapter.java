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
import com.example.task4.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.CustomViewHolder> {
    private List<RidesRespons.Ride> dataList;
    private Context context;
    private RideHistoryAdapter.OnItemClickListener clickListener;


    public RideHistoryAdapter(Context context, List<RidesRespons.Ride> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public List<RidesRespons.Ride> updatestatus(RidesRespons.Ride rideData) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).rideId == rideData.rideId) {
                dataList.set(i, rideData);
                notifyDataSetChanged();
            }
        }
        return dataList;
    }
    public List<RidesRespons.Ride> removeRide(RidesRespons.Ride rideData) {

        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).rideId == rideData.rideId) {
                dataList.remove(i);

                notifyDataSetChanged();
            }
        }
        return dataList;
    }

    public void setOnItemClickListener(RideHistoryAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void filterlist(List<RidesRespons.Ride> filterlist) {
        this.dataList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public RideHistoryAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RideHistoryAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RideHistoryAdapter.CustomViewHolder holder, int position) {

        holder.txt_name.setText(dataList.get(position).user.getName());
        holder.txt_requestid.setText(String.valueOf(dataList.get(position).rideId));
        holder.txt_servicetype.setText(dataList.get(position).serviceType.getVehicleType());
        holder.txt_pickup.setText(dataList.get(position).pickUp);
        holder.txt_dropoff.setText(dataList.get(position).dropOff);
        holder.txt_price.setText("₹" + dataList.get(position).totalFare);
        holder.txt_time.setText(dataList.get(position).rideTime);
        holder.txt_date.setText(dataList.get(position).rideDate);
        holder.btn_assign.setVisibility(View.GONE);
        holder.btn_cancel.setVisibility(View.GONE);
        if (dataList.get(position).message!=null) {
            Toast.makeText(context,dataList.get(position).message , Toast.LENGTH_SHORT).show();
        }
        if (dataList.get(position).driver != null)
        {   holder.btn_assign.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
            holder.txt_driver.setVisibility(View.VISIBLE);
            holder.txt_driver.setText(dataList.get(position).driver.getName());
        }
        if (dataList.get(position).status == -1) {
            holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
            ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.color_1));
            holder.btn.setBackgroundTintList(colorStateList);
        }
        if (dataList.get(position).status == 7) {


            holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
            ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.color7));
            holder.btn.setBackgroundTintList(colorStateList);
        }


        String path = "http://192.168.0.215:3000/" + dataList.get(position).user.getProfile();

        Picasso.get()
                .load(path)
                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onAssignClick(int position);
        void onCancelclick(int position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txt_name, txt_requestid, txt_servicetype, txt_pickup, txt_dropoff, txt_date, txt_time, txt_price,txt_driver;
        Button btn;
        private ImageView coverImage, btn_assign,btn_cancel;


        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txt_name = mView.findViewById(R.id.txt_username_rv);
            txt_price = mView.findViewById(R.id.txt_price_rv);
            txt_requestid = mView.findViewById(R.id.txt_requestid_rv);
            txt_servicetype = mView.findViewById(R.id.txt_servicetype_rv);
            txt_pickup = mView.findViewById(R.id.txt_pickup_rv);
            txt_dropoff = mView.findViewById(R.id.txt_dropoff_rv);
            coverImage = mView.findViewById(R.id.profile_pic_user);
            txt_date = mView.findViewById(R.id.txt_date_rv);
            txt_time = mView.findViewById(R.id.txt_time_rv);
            txt_driver=mView.findViewById(R.id.txt_driver_rv);
            btn_assign = mView.findViewById(R.id.img_assign_rv);
            btn = mView.findViewById(R.id.btn_rv);
            btn_cancel = mView.findViewById(R.id.img_reject_rv);
            btn_assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Call a method or perform an action based on the button click
                        clickListener.onAssignClick(position);
                    }
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Call a method or perform an action based on the button click
                        clickListener.onCancelclick(position);
                    }
                }
            });
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
