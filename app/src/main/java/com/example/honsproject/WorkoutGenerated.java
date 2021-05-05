package com.example.honsproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.honsproject.Workout.clickedItemPositions;
import static com.example.honsproject.Workout.wMuscle;

public class WorkoutGenerated extends AppCompatActivity {


    private ListView List;
    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://hons-project-55b17-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef;
    private ArrayList<String> musclesArray = new ArrayList<String>();
    private Random random = new Random();
    private Integer eID;
    private List<String> exList = new ArrayList<String>();
    private List<String> keyList = new ArrayList<String>();
    private Integer count = 0;
    private Button Favourite;

    public static String exercise = "";
    public String favOutput = "";
    public static String exMuscle = "";
    private HashMap<String, ArrayList<String>> hashMap = new HashMap<>();





    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_generated);

        Favourite = findViewById(R.id.wgFav);
        List = findViewById(R.id.gList);

        setMuscles(musclesArray);
        generate();



        Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                myRef = db.getReference().child("Users").child(user).child("Favourite");

                myRef.push().setValue(favOutput);

            }
        });

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                exercise = List.getItemAtPosition(position).toString();
                int leftIndex = exercise.indexOf("(");
                int rightIndex = exercise.indexOf(")");
                exMuscle = exercise.substring(leftIndex + 1, rightIndex);
                Log.v("ryan", exMuscle);
                exercise = exercise.substring(2, leftIndex - 1);



                Intent intent = new Intent(WorkoutGenerated.this, Exersize.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generate(){
        for(int x = 0; x < musclesArray.size(); x++){
            myRef = db.getReference(musclesArray.get(x));

            List<Integer> nums = IntStream.range(0,3).boxed().collect(Collectors.toList());
            Collections.shuffle(nums);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    count++;

                    for(DataSnapshot pSnapshot: snapshot.getChildren()){
                        keyList.add(String.valueOf(pSnapshot.getKey()));
                    }

                    hashMap.put(snapshot.getKey(), new ArrayList<>());

                    for(int y = 0; y < 2; y++){
                        eID = nums.get(y);

                        hashMap.get(snapshot.getKey()).add(keyList.get(eID));

                    }
                    if(count >= musclesArray.size()){

                        int tempCount = 0;
                        for(String Key: hashMap.keySet()){
                            for(String Exercise: hashMap.get(Key)){

                                tempCount++;
                                exList.add(tempCount + " " + Exercise + " (" + Key + ")");

                                Integer tempEID = snapshot.child(Exercise).child("eID").getValue(Integer.class);
                                favOutput = favOutput + tempEID + ",";

                            }
                        }

                        ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(WorkoutGenerated.this, android.R.layout.simple_list_item_1, exList);
                        List.setAdapter(arrayAdapter);
                    }
                    keyList.clear();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }


    private void setMuscles(ArrayList<String> muscles){

        for(int i = 0; i < Workout.wMuscle.length; i++){
            if(clickedItemPositions.get(i)){
                muscles.add(Workout.wMuscle[i]);
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