package com.eliezergh.manacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
        TextView lostPassword = findViewById(R.id.lostPassword);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Register Button
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {createAccount();}
        });
        //Login Button
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {loginAccount();}
        });
        //Lost Password
        lostPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Contraseña olvidada");
                alertDialog.setMessage("La función de recuperación de contraseñas aún no está implementada. Puedes enviar un correo a eliezergh93@gmail.com para obtener ayuda.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


    }

    public void createAccount() {
        EditText email = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        int passwordLength = password.getText().length();

        //Check if EditTexts are empty
        if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_LONG);
        }
        if (passwordLength < 6){ //Check password length
            AlertDialog passwordRequeriments = new AlertDialog.Builder(LoginActivity.this).create();
            passwordRequeriments.setTitle("Requisitos mínimos");
            passwordRequeriments.setMessage("La longitud de la contraseña debe de ser mayor o igual a 6");
            passwordRequeriments.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            passwordRequeriments.show();
        } else { //Everything seems to be correct
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
        int passwordLength = password.getText().length();

        //Check if EditTexts are empty
        if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_LONG);
        }
        if (passwordLength < 6){ //Check password length
            AlertDialog passwordRequeriments = new AlertDialog.Builder(LoginActivity.this).create();
            passwordRequeriments.setTitle("Requisitos mínimos");
            passwordRequeriments.setMessage("La longitud de la contraseña debe de ser mayor o igual a 6");
            passwordRequeriments.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            passwordRequeriments.show();
        } else { //Everything seems to be correct
            //Let's authenticate
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        //Load MainActivity and send email value
                        showMain(email.getText().toString());
                    } else {
                        Log.w("CREATE USER: ", "FAILED " +task.getException());
                        Toast.makeText(LoginActivity.this, "La contraseña no es correcta", Toast.LENGTH_LONG).show();
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
