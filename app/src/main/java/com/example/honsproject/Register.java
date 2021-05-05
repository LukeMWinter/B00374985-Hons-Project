package com.example.honsproject;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //Initialize Variables
    TextView mLoginBtn;
    EditText mFullName,mPassword,mEmail;
    Button mRegister;
    private FirebaseAuth fireAuth;
    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Assign Variables
        mLoginBtn = findViewById(R.id.r_Login);
        mFullName = findViewById(R.id.r_FullName);
        mPassword = findViewById(R.id.r_Password);
        mEmail = findViewById(R.id.r_Email);
        mRegister = findViewById(R.id.r_Register);
        fireAuth = FirebaseAuth.getInstance();
        pBar = findViewById(R.id.r_PBar);


        //OnClickListener for Login button
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        //OnClickListener for Register button
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String variables for TextViews
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString().trim();
                Integer admin = 0;
                String Favourite = "";

                //Invalid parameter checking
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password Required");
                    return;
                }

                if(password.length() < 6) {
                    mPassword.setError("Password must contain 6 or more characters");
                    return;
                }

                //Progress bar visibility
                pBar.setVisibility(View.VISIBLE);


                //Firebase user creation
                fireAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                    Toast.makeText(Register.this, "User has been created " + authResult.toString(), Toast.LENGTH_SHORT).show();

                    //New user class created to hold user data
                    User user = new User(fullName,Favourite, admin);

                    //Firebase Database connected and user detailed stored in "Users" directory
                    FirebaseDatabase.getInstance("https://hons-project-55b17-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                    }
                });
            }
        });



    }


}