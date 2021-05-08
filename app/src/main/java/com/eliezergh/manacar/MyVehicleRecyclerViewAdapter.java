package com.eliezergh.manacar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Vehicle}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVehicleRecyclerViewAdapter extends RecyclerView.Adapter<MyVehicleRecyclerViewAdapter.ViewHolder> {

    private final List<Vehicle> mValues;
    private final OnVehicleInteractionListener mListener;
    private Context ctx;
    //Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //StorageReference storageRef = storage.getReference();
    //Default vehicle logo
    StorageReference defaultVehicleImage = storage.getReferenceFromUrl("gs://manacar-46ccf.appspot.com/images/defaultVehicle.jpg");

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
        holder.vId.setText(holder.mItem.getvId());

        //if it has no value, then load manacar logo
        if (holder.mItem.getVehicleMainImage().equals("")) {
            //Load default vehicle image
            Glide.with(ctx)
                    .load(defaultVehicleImage)
                    .into(holder.vehicleMainImage);
        } else {
            //Load image provided by the user
            Glide.with(ctx)
                    .load(holder.mItem.getVehicleMainImage())
                    .into(holder.vehicleMainImage);
        }
        //Delete BUTTON
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get position
                String vIdToDelete = holder.mItem.getvId();
                //DB Connection
                DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mConditionRef = mRootRef.child("vehicles");
                //Delete from DB using vId
                mConditionRef.child(vIdToDelete).removeValue();
                //Delete from list
                mValues.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        //Modify BUTTON
        holder.modifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Get the vID to modify
                String vIdToModify = holder.mItem.getvId();
                Intent intent = new Intent(ctx, modActivity.class);
                //Pass the vID to the next modify activity
                intent.putExtra("EXTRA_VEHICLE_ID", vIdToModify);
                ctx.startActivity(intent);

            }
        });
        //
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
        public Button deleteButton;
        public Button modifyButton;
        public TextView vId;
        public Vehicle mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            vId = (TextView) view.findViewById(R.id.vId);
            vehicleManufacturer = (TextView) view.findViewById(R.id.vehicleManufacturer);
            vehicleMotor = (TextView) view.findViewById(R.id.vehicleMotor);
            vehicleRegistrationNumber = (TextView) view.findViewById(R.id.vehicleRegistrationNumber);
            vehicleMainImage = (ImageView) view.findViewById(R.id.vehicleMainImage);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);
            modifyButton = (Button) view.findViewById(R.id.modifyButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + vehicleManufacturer.getText() + "'";
        }
    }

}