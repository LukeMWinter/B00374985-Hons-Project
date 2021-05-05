package com.example.honsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    //Initialize Variables
    Button bLogout;
    public FirebaseAuth FireAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign Variables and Firebase Authentication
        bLogout = findViewById(R.id.m_Logout);
        FireAuth = FirebaseAuth.getInstance();

        //BottomNavigationView aspects assigned
        BottomNavigationView bottomNavigationView = findViewById(R.id.BotNav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Switch statement for each navigation icon
                switch(item.getItemId()){
                    case R.id.mainActivity:
                        return true;
                    case R.id.BMI:
                        Intent intent = new Intent(MainActivity.this, BMI.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                    case R.id.workout:
                        Intent intent_1 = new Intent(MainActivity.this, Workout.class);
                        startActivity(intent_1);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                }

                return false;
            }
        });



        //OnClickListener for Logout button
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase Authentication sign-out method
                FireAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
    });
}

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }

}