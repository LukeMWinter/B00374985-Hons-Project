package com.example.honsproject;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class Exersize extends AppCompatActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://hons-project-55b17.appspot.com");
    private FirebaseDatabase db = FirebaseDatabase.getInstance("https://hons-project-55b17-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference myRef;
    private StorageReference storageReference;
    private ImageView Image;
    private ListView List;
    private TextView Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exersize);

        Title = findViewById(R.id.eText);
        Title.setText(WorkoutGenerated.exercise);
        List = findViewById(R.id.eList);
        Image = findViewById(R.id.eImage);
        Image.setVisibility(View.INVISIBLE);
        myRef = db.getReference(WorkoutGenerated.exMuscle);

        myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                gatherInfo(snapshot);
            }
        });

        storageReference = storage.getReferenceFromUrl("gs://hons-project-55b17.appspot.com/images").child(WorkoutGenerated.exercise + ".png");

        try {
            final File files = File.createTempFile("img", "png");
            storageReference.getFile(files).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(files.getAbsolutePath());
                    Image.setImageBitmap(bitmap);
                    Image.setVisibility(View.VISIBLE);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void gatherInfo(DataSnapshot snapshot){
        ExerciseInfo test = snapshot.child(WorkoutGenerated.exercise).getValue(ExerciseInfo.class);

        ArrayList<String> array = new ArrayList<>();
        array.add(test.getStep_1());
        array.add(test.getStep_2());
        array.add(test.getStep_3());
        array.add(test.getStep_4());
        array.add(test.getStep_5());


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array);
        List.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }

}