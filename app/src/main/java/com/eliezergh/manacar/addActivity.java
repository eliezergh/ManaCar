package com.eliezergh.manacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;


public class addActivity extends AppCompatActivity {

    //DB Connection
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //ImageView
    Button pickImageButton;
    ImageView vehicleImageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    String gsPath;
    //Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    //Generate a random number to assign on vehicleID
    Random rnd = new Random();
    int vid = rnd.nextInt();
    String fpath = "vId"+vid;
    String userUid = "";

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
        //Get userUid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            userUid = user.getUid();
        } else {
            startActivity(new Intent(addActivity.this, LoginActivity.class));
        }
        //Image
        pickImageButton = findViewById(R.id.pickImageButton);
        vehicleImageView = findViewById(R.id.vehicleImageView);
        pickImageButton.setOnClickListener(new View.OnClickListener(){
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
        startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"), PICK_IMAGE_REQUEST);
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
                vehicleImageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
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
                                    + fpath);

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
                                            .makeText(addActivity.this,
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
                                    .makeText(addActivity.this,
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
                DatabaseReference mConditionVId = mRootRef.child(userUid).child("vehicles").child("vId"+vid);
                DatabaseReference mConditionVName = mRootRef.child(userUid).child("vehicles").child("vId"+vid).child("vehicleManufacturer");
                DatabaseReference mConditionVMotor = mRootRef.child(userUid).child("vehicles").child("vId"+vid).child("Motor");
                DatabaseReference mConditionVRNumber = mRootRef.child(userUid).child("vehicles").child("vId"+vid).child("vehicleRegistrationNumber");
                DatabaseReference mConditionVMImage = mRootRef.child(userUid).child("vehicles").child("vId"+vid).child("vehicleMainImage");
                //First, we should get the values on the form
                TextInputLayout newVehicleManufacturer = findViewById(R.id.newVehicleManufacturer);
                String VehicleManufacturer = newVehicleManufacturer.getEditText().getText().toString();
                TextInputLayout newVehicleMotor = findViewById(R.id.newVehicleMotor);
                String VehicleMotor = newVehicleMotor.getEditText().getText().toString();
                TextInputLayout newVehicleRegistrationNumber = findViewById(R.id.newVehicleRegistrationNumber);
                String VehicleRegistrationNumber = newVehicleRegistrationNumber.getEditText().getText().toString();

                //TextInputLayout newVehicleMainImage = findViewById(R.id.newVehicleMainImage);
                //String VehicleMainImage = newVehicleMainImage.getEditText().getText().toString();
                StorageReference stg = storage.getReference("images/"+userUid+"/"+fpath);
                gsPath = "gs://"+stg.getBucket()+stg.getPath();
                if (!VehicleManufacturer.isEmpty() && !VehicleMotor.isEmpty() && !VehicleRegistrationNumber.isEmpty()) {
                    mConditionVName.setValue(VehicleManufacturer);
                    mConditionVMotor.setValue(VehicleMotor);
                    mConditionVRNumber.setValue(VehicleRegistrationNumber);
                    uploadImage();
                    mConditionVMImage.setValue(gsPath);

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
