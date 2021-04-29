package com.eliezergh.manacar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


//

public class MainActivity extends AppCompatActivity {
    //
    Context context;
    Button edit;
    Button delete;
    LayoutParams layoutParams;
    TextView textView;
    LinearLayout linearLayout;
    ImageView imageView;
    String vehicleName;
    String vehicleMotor;
    String vehicleRegistrationNumber;
    long childItems;

    //DB Connection
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        linearLayout = (LinearLayout) findViewById(R.id.vehicleCardLayout);

        //Floating button functionality
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mconditionRef = mRootRef.child("vehicles");
        mconditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childItems = snapshot.getChildrenCount();
                for (DataSnapshot snap : snapshot.getChildren()){
                    Vehicle vehicle = snap.getValue(Vehicle.class);
                    vehicleName = (String) vehicle.vehicleName;
                    addVehicleCardView();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addVehicleCardView(){

        CardView materialCardView = new CardView(context);

        layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        //Card
        materialCardView.setLayoutParams(layoutParams);
        materialCardView.setCardBackgroundColor(Color.GRAY);
        materialCardView.setMaxCardElevation(6);
        materialCardView.setRadius(10);
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) materialCardView.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 30);
        materialCardView.requestLayout();
        //CardView Title
        textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(vehicleName);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(25,25,25,25);
        textView.setGravity(Gravity.CENTER);
        /*//Card Image
        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.default_vehicle_logo);
        imageView.setCropToPadding(true);
        materialCardView.addView(imageView);*/
        materialCardView.addView(textView);
        linearLayout.addView(materialCardView);

    }

}