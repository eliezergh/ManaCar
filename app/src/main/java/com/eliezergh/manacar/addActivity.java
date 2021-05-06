package com.eliezergh.manacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class addActivity extends AppCompatActivity {

    //DB Connection
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        Random rnd = new Random();
        rnd.nextInt();
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
        Button saveButton = findViewById(R.id.addNewVehicleSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generate a random number to assign on vehicleID
                Random rnd = new Random();
                int vid = rnd.nextInt();
                DatabaseReference mConditionVId = mRootRef.child("vehicles").child("vId"+vid);
                DatabaseReference mConditionVName = mRootRef.child("vehicles").child("vId"+vid).child("vehicleManufacturer");
                DatabaseReference mConditionVMotor = mRootRef.child("vehicles").child("vId"+vid).child("Motor");
                DatabaseReference mConditionVRNumber = mRootRef.child("vehicles").child("vId"+vid).child("vehicleRegistrationNumber");
                DatabaseReference mConditionVMImage = mRootRef.child("vehicles").child("vId"+vid).child("vehicleMainImage");
                //First, we should get the values on the form
                TextInputLayout newVehicleManufacturer = findViewById(R.id.newVehicleManufacturer);
                String VehicleManufacturer = newVehicleManufacturer.getEditText().getText().toString();
                TextInputLayout newVehicleMotor = findViewById(R.id.newVehicleMotor);
                String VehicleMotor = newVehicleMotor.getEditText().getText().toString();
                TextInputLayout newVehicleRegistrationNumber = findViewById(R.id.newVehicleRegistrationNumber);
                String VehicleRegistrationNumber = newVehicleRegistrationNumber.getEditText().getText().toString();
                TextInputLayout newVehicleMainImage = findViewById(R.id.newVehicleMainImage);
                String VehicleMainImage = newVehicleMainImage.getEditText().getText().toString();

                if (!VehicleManufacturer.isEmpty() && !VehicleMotor.isEmpty() && !VehicleRegistrationNumber.isEmpty()) {
                    mConditionVName.setValue(VehicleManufacturer);
                    mConditionVMotor.setValue(VehicleMotor);
                    mConditionVRNumber.setValue(VehicleRegistrationNumber);
                    mConditionVMImage.setValue(VehicleMainImage);

                    //Go back to Main Activity
                    startActivity(new Intent(addActivity.this, MainActivity.class));
                } else {
                    Log.e("Vehicle ADD: ", "EMPTY FORM");
                    Snackbar.make(saveButton, "Hay campos vacios", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
