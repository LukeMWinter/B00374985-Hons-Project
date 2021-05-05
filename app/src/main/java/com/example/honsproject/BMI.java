package com.example.honsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;

public class BMI extends AppCompatActivity {

    //Variables Initialized
    Button bCalc, bLogout;
    TextView bResults, bMargin;
    EditText bHeight, bWeight;
    FirebaseAuth FireAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_m_i);

        //Variables Assigned
        bCalc = (Button) findViewById(R.id.bCalculate);
        bResults = (TextView) findViewById(R.id.bResult);
        bHeight = (EditText) findViewById(R.id.bHeight);
        bWeight = (EditText) findViewById(R.id.bWeight);
        bMargin = (TextView) findViewById(R.id.bMargin);


        //OnClickListener for Calculate button
        bCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Height = bHeight.getText().toString().trim();
                String Weight = bWeight.getText().toString().trim();
                if(TextUtils.isEmpty(Height)){
                    bHeight.setError("Please enter your height.");
                }else if(TextUtils.isEmpty(Weight)){
                    bWeight.setError("Please enter your Weight.");
                }else{
                    doCalculation();
                }

            }
        });

        bLogout = findViewById(R.id.m_Logout);
        FireAuth = FirebaseAuth.getInstance();

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.BotNav);
        bottomNavigationView.setSelectedItemId(R.id.BMI);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.mainActivity:
                        Intent intent = new Intent(BMI.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;
                    case R.id.BMI:
                        return true;
                    case R.id.workout:
                        Intent intent_1 = new Intent(BMI.this, Workout.class);
                        startActivity(intent_1);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void doCalculation(){
        if(bHeight != null && bWeight != null){
            Double height = Double.valueOf(bHeight.getText().toString()) / 100;
            Double weight = Double.valueOf(bWeight.getText().toString());
            Double result;

            result = (weight / (height * height));
            DecimalFormat df = new DecimalFormat("##.##");

            if(result > 100 || result < 10){
                bResults.setText("Please enter valid BMI parameters");
            }else{
                bResults.setText("Your BMI is: " + df.format(result));

                if(result <= 18){
                    bMargin.setText("You are underweight");
                } else if(result > 18 && result <= 24){
                    bMargin.setText("You are healthy");
                } else if(result > 24 && result <= 29){
                    bMargin.setText("You are overweight");
                } else if(result > 29){
                    bMargin.setText("You are obese");
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }

    }
