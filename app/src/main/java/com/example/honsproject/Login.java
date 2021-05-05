package com.example.honsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    //Initialize Variables
    EditText lEmail, lPassword;
    Button lLogin;
    TextView lRegister;
    ProgressBar pBar;
    private FirebaseAuth fireAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assign Variables
        lEmail = findViewById(R.id.l_Email);
        lPassword = findViewById(R.id.l_Password);
        lLogin = findViewById(R.id.l_Login);
        lRegister = findViewById(R.id.l_Register);
        pBar = findViewById(R.id.l_PBar);
        fireAuth = FirebaseAuth.getInstance();


        //OnClickListener for Register TextView
        lRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        //OnClickListener for Login button
        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String variables for TextViews
                String email = lEmail.getText().toString().trim();
                String password = lPassword.getText().toString().trim();

                //Invalid parameter checking
                if(TextUtils.isEmpty(email)){
                    lEmail.setError("Email Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    lPassword.setError("Password Required");
                    return;
                }

                //Progress bar visibility
                pBar.setVisibility(View.VISIBLE);

                //Firebase user sign-in
                fireAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //String created to hold current user Uid
                        String UID = fireAuth.getCurrentUser().getUid();
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error > " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}