package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity {

    public static ArrayList<String> optionA;
    public static ArrayList<String> optionB;
    public static ArrayList<String> optionC;
    public static ArrayList<String> optionD;
    public static ArrayList<String> questions;
    public static ArrayList<String> answerArray;
    public static String userAnswer [];
    public static int scoreStatic;

    private FirebaseFirestore db;

    // This going to be pushed to the database as information for attempt history

    private String duration;

    public static ArrayList<String> pointsEarned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView score = findViewById(R.id.scorePracticeResult);

        score.setText("" + scoreStatic);

        this.duration = PracticeActivities.minuteTaken + ":" + PracticeActivities.secondTaken;

        this.db = FirebaseFirestore.getInstance();

        setData(RecyclerViewAdapterQuizSelector.startTime + "," + PracticeActivities.getEndTime() + ","
                + scoreStatic + "," + duration + "," + RecyclerViewAdapterQuizSelector.selectedQuizID);
    }

    public void review (View view) {
        Intent intent = new Intent(getApplicationContext(), SolutionAndExplanation.class);
        startActivity(intent);
    }

    public void mainmenu (View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        this.finish();
    }

    public void detail (View view) {
        Intent intent = new Intent(getApplicationContext(), ResultPractice.class);
        startActivity(intent);
    }

    // This function is used to bring information to the database
    private void setData (final String attemptInformation) {
        String currentUserEmail;

        if (Login.user != null) {
            currentUserEmail = Login.user.getEmail();
        } else {
            currentUserEmail = Welcome.user.getEmail();
        }

        final DocumentReference documentReference = db.collection("user")
                .document(currentUserEmail);

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int numOfItems;
                        HashMap <String, Object> map;

                        map = (HashMap<String, Object>) documentSnapshot.get("attempts");

                        numOfItems = map.size();

                        DocumentReference documentReference;

                        if (Login.user != null) {
                            documentReference = db.collection("user").document(Login.user.getEmail());
                        } else {
                            documentReference = db.collection("user").document(Welcome.user.getEmail());
                        }

                        Map <String, Object> data = new HashMap<>();
                        data.put("attempts.attempt" + (numOfItems + 1), attemptInformation);

                        documentReference
                                .update(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Message", "Error adding document", e);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
