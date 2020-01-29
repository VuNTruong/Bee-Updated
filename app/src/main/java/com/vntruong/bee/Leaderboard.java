package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Leaderboard extends AppCompatActivity {

    FirebaseFirestore database;

    RecyclerViewAdapterLeaderboard adapter;

    public ArrayList<String> email = new ArrayList<>();
    public ArrayList<String> name = new ArrayList<>();

    // String ID of the selected quiz
    String selectedQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        database = FirebaseFirestore.getInstance();

        getDocuments();
    }

    private void getDocuments () {
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        db.collection("userattempts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> map;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map = (HashMap<String, Object>) document.getData().get("user info");

                                email.add(document.getId());
                                name.add(map.get("firstname").toString() + " " + map.get("middlename").toString() + " " + map.get("lastname"));

                                RecyclerView recyclerView = findViewById(R.id.competitors);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Leaderboard.this));
                                adapter = new RecyclerViewAdapterLeaderboard(name, email, Leaderboard.this);
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d("Message", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
