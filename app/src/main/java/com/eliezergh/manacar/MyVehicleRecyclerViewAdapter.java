package com.eliezergh.manacar;

import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Vehicle}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVehicleRecyclerViewAdapter extends RecyclerView.Adapter<MyVehicleRecyclerViewAdapter.ViewHolder> {

    private final List<Vehicle> mValues;
    private final OnVehicleInteractionListener mListener;
    private Context ctx;

    public MyVehicleRecyclerViewAdapter(Context context, List<Vehicle> items, OnVehicleInteractionListener listener) {
        ctx = context;
        mListener = listener;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.vehicleManufacturer.setText(holder.mItem.getVehicleManufacturer());
        holder.vehicleMotor.setText(holder.mItem.getMotor());
        holder.vehicleRegistrationNumber.setText(holder.mItem.getVehicleRegistrationNumber());
        Glide.with(ctx)
                .load(holder.mItem.getVehicleMainImage())
                .into(holder.vehicleMainImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.OnVehicleClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView vehicleManufacturer;
        public final TextView vehicleMotor;
        public final TextView vehicleRegistrationNumber;
        public final ImageView vehicleMainImage;
        public Vehicle mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            vehicleManufacturer = (TextView) view.findViewById(R.id.vehicleManufacturer);
            vehicleMotor = (TextView) view.findViewById(R.id.vehicleMotor);
            vehicleRegistrationNumber = (TextView) view.findViewById(R.id.vehicleRegistrationNumber);
            vehicleMainImage = (ImageView) view.findViewById(R.id.vehicleMainImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + vehicleManufacturer.getText() + "'";
        }
    }
}