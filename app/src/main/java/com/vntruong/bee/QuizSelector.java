package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizSelector extends AppCompatActivity {

    FirebaseFirestore database;

    RecyclerViewAdapterQuizSelector adapter;

    public ArrayList<String> quiznumber = new ArrayList<>();
    public ArrayList<String> datestarted = new ArrayList<>();
    public ArrayList<String> duedate = new ArrayList<>();
    public ArrayList<String> timeallowed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selector);

        database = FirebaseFirestore.getInstance();

        getDocuments();
    }

    private void getDocuments () {
        FirebaseFirestore db;

        db = FirebaseFirestore.getInstance();

        db.collection("computer science")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> map;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map = (HashMap<String, Object>) document.getData().get("Quiz info");
                                //Log.d("Message", document.getId() + " => " + document.getData().get("Quiz info"));
                                Log.d("Message", document.getId());
                                quiznumber.add(document.getId());
                                Log.d("Message", map.get("Date started").toString());
                                datestarted.add(map.get("Date started").toString());
                                Log.d("Message", map.get("Due date").toString());
                                duedate.add(map.get("Due date").toString());
                                Log.d("Message", map.get("Time allowed").toString());
                                timeallowed.add(map.get("Time allowed").toString());

                                Log.d("Message", "" + timeallowed.size());

                                RecyclerView recyclerView = findViewById(R.id.quizzesOption);
                                recyclerView.setLayoutManager(new LinearLayoutManager(QuizSelector.this));
                                adapter = new RecyclerViewAdapterQuizSelector(quiznumber, datestarted, duedate, timeallowed, QuizSelector.this);
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Log.d("Message", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
