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

public class RunningRequestAdapter extends RecyclerView.Adapter<RunningRequestAdapter.CustomViewHolder> {
    private List<RidesRespons.Ride> dataList;
    private Context context;
    private RunningRequestAdapter.OnItemClickListener clickListener;


    public RunningRequestAdapter(Context context, List<RidesRespons.Ride> dataList) {
        this.context = context;
        this.dataList = dataList;
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
    public List<RidesRespons.Ride> updateRide(RidesRespons.Ride rideData)
    {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).rideId == rideData.rideId) {
                if (rideData.status == 7)
                {
                    dataList.remove(i);
                }else {
                    dataList.set(i, rideData);
                }

                notifyDataSetChanged();
            }
        }
        return dataList;
    }
    public List<RidesRespons.Ride> addRide(RidesRespons.Ride ridedata)
    {

        dataList.add(ridedata);
        notifyDataSetChanged();
        return dataList;

    }

    public void setOnItemClickListener(RunningRequestAdapter.OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void filterlist(List<RidesRespons.Ride> filterlist) {
        this.dataList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.running_request_adapter, parent, false);
        return new RunningRequestAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        if (dataList.get(position).status >= 2 && dataList.get(position).status <= 6) {
            holder.txt_name.setText(dataList.get(position).user.getName());
            holder.txt_requestid.setText(String.valueOf(dataList.get(position).rideId));
            holder.txt_servicetype.setText(dataList.get(position).serviceType.getVehicleType());
            holder.txt_pickup.setText(dataList.get(position).pickUp);
            holder.txt_dropoff.setText(dataList.get(position).dropOff);
            holder.txt_price.setText("₹" + dataList.get(position).totalFare);
            holder.txt_time.setText(dataList.get(position).rideTime);
            holder.txt_date.setText(dataList.get(position).rideDate);
            holder.txt_driver.setText(dataList.get(position).driver.getName());
            if (dataList.get(position).message!=null) {
                Toast.makeText(context,dataList.get(position).message , Toast.LENGTH_SHORT).show();
            }
            if (dataList.get(position).status == 2) {
                holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color2));
                holder.btn.setBackgroundTintList(colorStateList);
                holder.btn_next.setVisibility(View.GONE);
                holder.btn_assign.setVisibility(View.VISIBLE);
                holder.btn_reject.setVisibility(View.VISIBLE);
            }
            if (dataList.get(position).status == 3) {
                holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
                holder.btn_next.setText(Status.reversedServices.get(dataList.get(position).status+1));

                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color3));
                holder.btn.setBackgroundTintList(colorStateList);
                holder.btn_assign.setVisibility(View.GONE);
                holder.btn_reject.setVisibility(View.GONE);
                holder.btn_next.setVisibility(View.VISIBLE);



            }
            if (dataList.get(position).status == 4) {
                holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
                holder.btn_next.setText(Status.reversedServices.get(dataList.get(position).status+1));
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color4));
                ColorStateList colorStateList1 = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color5));
                holder.btn.setBackgroundTintList(colorStateList);
                holder.btn_next.setBackgroundTintList(colorStateList1);

            }
            if (dataList.get(position).status == 5) {
                holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
                holder.btn_next.setText(Status.reversedServices.get(dataList.get(position).status+1));
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color5));
                ColorStateList colorStateList1 = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color6));
                holder.btn.setBackgroundTintList(colorStateList);
                holder.btn_next.setBackgroundTintList(colorStateList1);
            }
            if (dataList.get(position).status == 6) {
                holder.btn.setText(Status.reversedServices.get(dataList.get(position).status));
                holder.btn_next.setText(Status.reversedServices.get(dataList.get(position).status+1));
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color6));
                ColorStateList colorStateList1 = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.color7));
                holder.btn.setBackgroundTintList(colorStateList);
                holder.btn_next.setBackgroundTintList(colorStateList1);
            }
            String path = "http://192.168.0.215:3000/" + dataList.get(position).user.getProfile();

            Picasso.get()
                    .load(path)
                    .into(holder.coverImage);
        }

    }

    @Override
    public int getItemCount() {
        int size = dataList.size();
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onAssignClick(int position);
        void onNextClick(int position);
        void oncancelClick(int position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txt_name, txt_requestid, txt_servicetype, txt_pickup, txt_dropoff, txt_date, txt_time, txt_price, txt_driver;
        Button btn,btn_next;
        private ImageView coverImage, btn_assign,btn_reject;


        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txt_name = mView.findViewById(R.id.txt_username_rr);
            txt_price = mView.findViewById(R.id.txt_price_rr);
            txt_requestid = mView.findViewById(R.id.txt_requestid_rr);
            txt_servicetype = mView.findViewById(R.id.txt_servicetype_rr);
            txt_pickup = mView.findViewById(R.id.txt_pickup_rv);
            txt_dropoff = mView.findViewById(R.id.txt_dropoff_rv);
            coverImage = mView.findViewById(R.id.profile_pic_rr);
            txt_date = mView.findViewById(R.id.txt_date_rr);
            txt_time = mView.findViewById(R.id.txt_time_rr);
            btn_assign = mView.findViewById(R.id.img_assign_rv);
            btn_reject= mView.findViewById(R.id.img_reject_rv);
            btn = mView.findViewById(R.id.btn_rv);
            txt_driver = mView.findViewById(R.id.txt_driver_rr);
            btn_next = mView.findViewById(R.id.btn_next_rr);
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
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    clickListener.onNextClick(position);
                }
            });

            btn_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        int position = getAdapterPosition();

                            clickListener.oncancelClick(position);


                }
            });
        }
    }
}

