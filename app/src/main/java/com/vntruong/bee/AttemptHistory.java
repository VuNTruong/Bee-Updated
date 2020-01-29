package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttemptHistory extends AppCompatActivity {

    FirebaseFirestore database;
    private RecyclerViewAdapterHistory adapter;

    private ArrayList <String> dates;
    private ArrayList <String> times;
    private ArrayList <String> durations;
    private ArrayList <String> scores;
    private ArrayList <String> quiznumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_history);

        database = FirebaseFirestore.getInstance();

        dates = new ArrayList<>();
        times = new ArrayList<>();
        durations = new ArrayList<>();
        scores = new ArrayList<>();
        quiznumbers = new ArrayList<>();

        getDataHistory();
    }

    private void getDataHistory() {
        final String currentUserEmail;

        if (Login.user != null) {
            currentUserEmail = Login.user.getEmail();
        } else {
            currentUserEmail = Welcome.user.getEmail();
        }

        // DocumentReference which is used to reference to the collection which contains quizzes
        final DocumentReference documentReference = database.collection("user")
                .document(currentUserEmail);

        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;

                        Map <String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("attempts");

                        numOfItems = map.size();

                        Log.d("Message", "" + numOfItems);

                        // If there's nothing in the database for this, show the user that there's nothing to show
                        if (numOfItems == 0) {
                            setContentView(R.layout.activity_nothing_here_yet);
                            return;
                        }

                        for (int i = 0; i < numOfItems; i++) {
                            String parts [];
                            parts = map.get("attempt" + (i + 1)).toString().split(",");

                            // Shibakun
                            dates.add(parts[0]);
                            times.add(parts[1]);
                            durations.add(parts[2]);
                            scores.add(parts[3]);
                            quiznumbers.add(parts[4]);
                        }

                        RecyclerView recyclerView = findViewById(R.id.historyPane);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AttemptHistory.this));
                        adapter = new RecyclerViewAdapterHistory(dates, times, scores, durations, quiznumbers, AttemptHistory.this);
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
