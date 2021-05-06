package com.eliezergh.manacar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class VehicleFragment extends Fragment {

    OnVehicleInteractionListener mListener;
    List<Vehicle> vehicleList;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("vehicles");

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VehicleFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VehicleFragment newInstance(int columnCount) {
        VehicleFragment fragment = new VehicleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            vehicleList = new ArrayList<>();
            //vars
            final String[] vehicleManufacturer = new String[1];
            final String[] Motor = new String[1];
            final String[] vehicleRegistrationNumber = new String[1];
            final String[] vehicleMainImage = new String[1];
            final String[] vId = new String[1];
            //Read DB
            mConditionRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Clear the array before load again the data on DB
                    vehicleList.clear();
                    //Get vehicles on DB and
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Vehicle vehicle = snap.getValue(Vehicle.class);
                        vId[0] = (String) snap.getKey();
                        vehicleManufacturer[0] = (String) vehicle.vehicleManufacturer;
                        Motor[0] = (String) vehicle.Motor;
                        vehicleRegistrationNumber[0] = (String) vehicle.vehicleRegistrationNumber;
                        vehicleMainImage[0] = (String) vehicle.vehicleMainImage;
                        vehicleList.add(new Vehicle(vId[0], vehicleManufacturer[0], Motor[0], vehicleRegistrationNumber[0], vehicleMainImage[0]));

                    }
                    recyclerView.setAdapter(new MyVehicleRecyclerViewAdapter(getActivity(), vehicleList, mListener));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            recyclerView.setAdapter(new MyVehicleRecyclerViewAdapter(getActivity(), vehicleList, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnVehicleInteractionListener) {
            mListener = (OnVehicleInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()+" must implement OnVehicleInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}