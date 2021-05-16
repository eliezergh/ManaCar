package com.eliezergh.manacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {


    //Authentication
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        Button register = findViewById(R.id.register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Register Button
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
        //Login Button
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginAccount();
            }
        });

    }

    public void createAccount() {
        EditText email = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_LONG);
        } else {
            //Let's authenticate
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Load MainActivity and send email value
                        showMain(email.getText().toString());
                    } else {
                        Log.w("CREATE USER: ", "FAILED " +task.getException());
                        Toast.makeText(LoginActivity.this, "Oops, algo ha fallado", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    public void loginAccount(){
        EditText email = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_LONG);
        } else {
            //Let's authenticate
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Load MainActivity and send email value
                        showMain(email.getText().toString());
                    } else {
                        Log.w("CREATE USER: ", "FAILED " +task.getException());
                        Toast.makeText(LoginActivity.this, "Oops, algo ha fallado", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void showMain(String email){
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.putExtra("USER_EMAIL", email);
        startActivity(mainIntent);
    }
    @Override
    public void onBackPressed() {
        //do nothing
    }
}
