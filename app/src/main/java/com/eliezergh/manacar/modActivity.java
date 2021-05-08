package com.eliezergh.manacar;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;


public class modActivity extends AppCompatActivity {

    //DB Connection
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("vehicles");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextInputLayout modVehicleManufacturer = findViewById(R.id.modVehicleManufacturer);
        TextInputLayout modVehicleMotor = findViewById(R.id.modVehicleMotor);
        TextInputLayout modVehicleRegistrationNumber = findViewById(R.id.modVehicleRegistrationNumber);
        TextInputLayout modVehicleMainImage = findViewById(R.id.modVehicleMainImage);
        //Get vehicle id to modify
        String vIdToModify = getIntent().getStringExtra("EXTRA_VEHICLE_ID");
        Log.e("vID TO MODIFIY: ", vIdToModify);
        //Bring back data from DB
        final String[] mVehicleManufacturer = new String[1];
        final String[] mMotor = new String[1];
        final String[] mVehicleRegistrationNumber = new String[1];
        final String[] mVehicleMainImage = new String[1];
        mConditionRef.child(vIdToModify).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Vehicle vehicle = snapshot.getValue(Vehicle.class);
                mVehicleManufacturer[0] = vehicle.vehicleManufacturer;
                mMotor[0] = vehicle.Motor;
                mVehicleRegistrationNumber[0] = vehicle.vehicleRegistrationNumber;
                mVehicleMainImage[0] = vehicle.vehicleMainImage;
                //Set text on TextViews
                modVehicleManufacturer.getEditText().setText(mVehicleManufacturer[0]);
                modVehicleMotor.getEditText().setText(mMotor[0]);
                modVehicleRegistrationNumber.getEditText().setText(mVehicleRegistrationNumber[0]);
                modVehicleMainImage.getEditText().setText(mVehicleMainImage[0]);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Save changes
        Button modifyButton = findViewById(R.id.modVehicleSaveButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DatabaseReference mConditionVId = mRootRef.child("vehicles").child(vIdToModify);
                DatabaseReference mConditionVName = mRootRef.child("vehicles").child(vIdToModify).child("vehicleManufacturer");
                DatabaseReference mConditionVMotor = mRootRef.child("vehicles").child(vIdToModify).child("Motor");
                DatabaseReference mConditionVRNumber = mRootRef.child("vehicles").child(vIdToModify).child("vehicleRegistrationNumber");
                DatabaseReference mConditionVMImage = mRootRef.child("vehicles").child(vIdToModify).child("vehicleMainImage");
                //First, we should get the values on the form
                String VehicleManufacturer = modVehicleManufacturer.getEditText().getText().toString();
                String VehicleMotor = modVehicleMotor.getEditText().getText().toString();
                String VehicleRegistrationNumber = modVehicleRegistrationNumber.getEditText().getText().toString();
                String VehicleMainImage = modVehicleMainImage.getEditText().getText().toString();

                if (!VehicleManufacturer.isEmpty() && !VehicleMotor.isEmpty() && !VehicleRegistrationNumber.isEmpty()) {
                    mConditionVName.setValue(VehicleManufacturer);
                    mConditionVMotor.setValue(VehicleMotor);
                    mConditionVRNumber.setValue(VehicleRegistrationNumber);
                    mConditionVMImage.setValue(VehicleMainImage);

                    //Go back to Main Activity
                    startActivity(new Intent(modActivity.this, MainActivity.class));
                } else {
                    Log.e("Vehicle MODIFY: ", "EMPTY FORM");
                    Snackbar.make(modifyButton, "Hay campos vacios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
