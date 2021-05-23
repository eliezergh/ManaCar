package com.eliezergh.manacar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class modActivity extends AppCompatActivity {

    //DB Connection
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //ImageView
    Button modPickImageButton;
    ImageView modVehicleImageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    String vIdToModify;
    //Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    //
    String userUid = "";
    String defaultVehicleImage = "gs://manacar-46ccf.appspot.com/images/defaultVehicle.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Get userUid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            userUid = user.getUid();
        } else {
            startActivity(new Intent(modActivity.this, LoginActivity.class));
        }
        //Image
        modPickImageButton = findViewById(R.id.modPickImageButton);
        modVehicleImageView = findViewById(R.id.modVehicleImageView);
        modPickImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

    }

    void SelectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Cambiar imagen"), PICK_IMAGE_REQUEST);
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Guardando...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"+userUid+"/"
                                    + vIdToModify);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(modActivity.this,
                                                    "Imagen guardada satisfactoriamente",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(modActivity.this,
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Guardando:  "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                modVehicleImageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
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
        //Get vehicle id to modify
        vIdToModify = getIntent().getStringExtra("EXTRA_VEHICLE_ID");
        //Get vehicle image path
        StorageReference stg = storage.getReference("images/"+userUid+"/"+vIdToModify);
        String gsPath = "gs://"+stg.getBucket()+stg.getPath();
        //StorageReference userVehicleImage = storage.getReferenceFromUrl(""+gsPath+"");
        TextInputLayout modVehicleManufacturer = findViewById(R.id.modVehicleManufacturer);
        TextInputLayout modVehicleMotor = findViewById(R.id.modVehicleMotor);
        TextInputLayout modVehicleRegistrationNumber = findViewById(R.id.modVehicleRegistrationNumber);
        //Bring back data from DB
        final String[] mVehicleManufacturer = new String[1];
        final String[] mMotor = new String[1];
        final String[] mVehicleRegistrationNumber = new String[1];
        DatabaseReference mConditionRef = mRootRef.child(userUid).child("vehicles");
        mConditionRef.child(vIdToModify).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    mVehicleManufacturer[0] = vehicle.vehicleManufacturer;
                    mMotor[0] = vehicle.Motor;
                    mVehicleRegistrationNumber[0] = vehicle.vehicleRegistrationNumber;
                    //Set text on TextViews
                    modVehicleManufacturer.getEditText().setText(mVehicleManufacturer[0]);
                    modVehicleMotor.getEditText().setText(mMotor[0]);
                    modVehicleRegistrationNumber.getEditText().setText(mVehicleRegistrationNumber[0]);
                } else {
                    //do nothing
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Read & Save changes
        Button modifyButton = findViewById(R.id.modVehicleSaveButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mConditionVName = mRootRef.child(userUid).child("vehicles").child(vIdToModify).child("vehicleManufacturer");
                DatabaseReference mConditionVMotor = mRootRef.child(userUid).child("vehicles").child(vIdToModify).child("Motor");
                DatabaseReference mConditionVRNumber = mRootRef.child(userUid).child("vehicles").child(vIdToModify).child("vehicleRegistrationNumber");
                DatabaseReference mConditionVMImage = mRootRef.child(userUid).child("vehicles").child(vIdToModify).child("vehicleMainImage");
                //First, we should get the values on the form
                String VehicleManufacturer = modVehicleManufacturer.getEditText().getText().toString();
                String VehicleMotor = modVehicleMotor.getEditText().getText().toString();
                String VehicleRegistrationNumber = modVehicleRegistrationNumber.getEditText().getText().toString();

                //Check if text inputs are empty
                if (!VehicleManufacturer.isEmpty() && !VehicleMotor.isEmpty() && !VehicleRegistrationNumber.isEmpty()) {
                   //Save all data

                    mConditionVName.setValue(VehicleManufacturer);
                    mConditionVMotor.setValue(VehicleMotor);
                    mConditionVRNumber.setValue(VehicleRegistrationNumber);
                    uploadImage();
                    mConditionVMImage.setValue(gsPath);

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
