package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
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

public class CompetitorResultShow extends AppCompatActivity {

    FirebaseFirestore database;
    private RecyclerViewAdapterCompetitorResult adapter;

    private ArrayList <String> startTime;
    private ArrayList <String> endTime;
    private ArrayList <String> durations;
    private ArrayList <String> scores;
    private ArrayList <String> testNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_result_show);

        database = FirebaseFirestore.getInstance();

        startTime = new ArrayList<>();
        endTime = new ArrayList<>();
        durations = new ArrayList<>();
        scores = new ArrayList<>();
        testNumbers = new ArrayList<>();

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
        final DocumentReference documentReference = database.collection("userattempts")
                .document(RecyclerViewAdapterLeaderboard.selectedUserEmail);

        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int numOfItems;

                        Map <String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("Test result");

                        numOfItems = map.size();

                        Log.d("Message", "" + numOfItems);

                        // If there's nothing in the database for this, show the user that there's nothing to show
                        if (numOfItems == 0) {
                            setContentView(R.layout.activity_nothing_here_yet);
                            return;
                        }

                        for (int i = 0; i < numOfItems; i++) {
                            Map <String, Object> map1;
                            map1 = (HashMap) map.get("test" + (i + 1));

                            durations.add(map1.get("Duration").toString());
                            endTime.add(map1.get("End time").toString());
                            scores.add(map1.get("Score").toString());
                            startTime.add(map1.get("Start time").toString());
                            testNumbers.add("TEST " + (i + 1));
                        }

                        RecyclerView recyclerView = findViewById(R.id.competitorResults);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CompetitorResultShow.this));
                        adapter = new RecyclerViewAdapterCompetitorResult(startTime, endTime, scores, durations, testNumbers,CompetitorResultShow.this);
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
