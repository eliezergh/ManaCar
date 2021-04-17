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
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class addActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Save Button
        Button saveButton = findViewById(R.id.addNewVehicleSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlThread.start();
                startActivity(new Intent(addActivity.this,MainActivity.class));
            }
        });
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
    //Write data to DB
    Thread sqlThread = new Thread(){
      public void run(){
          //First, we should get the values on the form
          TextInputLayout newVehicleManufacturer = findViewById(R.id.newVehicleManufacturer);
          String VehicleManufacturer = newVehicleManufacturer.getEditText().getText().toString();
          TextInputLayout newVehicleMotor = findViewById(R.id.newVehicleMotor);
          String VehicleMotor = newVehicleMotor.getEditText().getText().toString();
          TextInputLayout newVehicleRegistrationNumber = findViewById(R.id.newVehicleRegistrationNumber);
          String VehicleRegistrationNumber = newVehicleRegistrationNumber.getEditText().getText().toString();
          //Write the query
          String query ="INSERT INTO (Manufacturer, Motor, registrationNumber) VALUES ('"+VehicleManufacturer+"', '"+VehicleMotor+"', '"+VehicleRegistrationNumber+"')";
          //Let's save data
          dbConnect dbc = new dbConnect();
          Connection conn = dbc.connect();
          try {
              PreparedStatement prepStat = conn.prepareStatement(query);
              prepStat.executeUpdate();
              conn.close();
          } catch (SQLException e){
              Log.e("MySQL Connection: ", e.getMessage());
          }
      }
    };

}
