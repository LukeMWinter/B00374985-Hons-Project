package com.example.honsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class Workout extends AppCompatActivity {

    //Initialize Variables
    Button bLogout, bGenerate;
    FirebaseAuth FireAuth;
    ListView wList;
    public static SparseBooleanArray clickedItemPositions;
    ArrayAdapter<String> wArray;
    private Integer count = 0;

    public static String[] wMuscle = {
            "Chest",
            "Calves",
            "Hamstrings",
            "Quads",
            "Glutes",
            "Delts",
            "Traps",
            "Triceps",
            "Biceps",
            "Forearms",
            "Upper Back",
            "Lower Back",
            "Obliques",
            "Lats",
            "Abs"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //Assign Variables
        bLogout = findViewById(R.id.m_Logout);
        FireAuth = FirebaseAuth.getInstance();
        wList = findViewById(R.id.w_List);
        bGenerate = findViewById(R.id.wGenerate);
        wArray = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,wMuscle);
        clickedItemPositions = new SparseBooleanArray(wMuscle.length);


        //Populate ListView with wArray
        wList.setAdapter(wArray);
        //OnItemClickListener for ListView
        wList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedItemPositions = wList.getCheckedItemPositions();
            }
        });


        //OnClickListener for Generate Workout Button
        bGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If statement to ensure between 2 and 5 options are selected
                if(wList.getCheckedItemCount() < 2 || wList.getCheckedItemCount() > 5){
                    Toast.makeText(Workout.this, "Please select between 2 and 5 muscles", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(getApplicationContext(),WorkoutGenerated.class));
                }
            }
        });

        //OnClickListener for Logout button
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        //BottomNavigationView aspects assigned
        BottomNavigationView bottomNavigationView = findViewById(R.id.BotNav);
        bottomNavigationView.setSelectedItemId(R.id.workout);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Switch statement for each navigation icon
                switch(item.getItemId()){
                    case R.id.mainActivity:
                        Intent intent = new Intent(Workout.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;
                    case R.id.BMI:
                        Intent intent_1 = new Intent(Workout.this, BMI.class);
                        startActivity(intent_1);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                    case R.id.workout:
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

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }

}