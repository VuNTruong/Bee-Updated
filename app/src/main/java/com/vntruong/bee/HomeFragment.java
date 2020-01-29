package com.vntruong.bee;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {
    private FirebaseFirestore database;
    private TextView nameTextView;
    public static String startTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_menu, container, false);

        // Initialization begins
        database = FirebaseFirestore.getInstance();

        nameTextView = view.findViewById(R.id.name);
        // Initialization ends

        // Handle event for the practice button
        ImageButton practice = view.findViewById(R.id.practice);
        practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Date and time when the quiz begins
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss");

                // Set the start time to the startTime
                startTime = sdf.format(new Date());

                // Let the user choose the quiz
                Intent intent = new Intent(v.getContext(), QuizSelector.class);
                startActivity(intent);
            }
        });

        // Handle event for the quiz creator button (It will be forum button for now)
        ImageButton forum = view.findViewById(R.id.fight);
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // This is to show information of user at the beginning of page
        getUserInformation();

        // Handle event for the leaderboard button
        ImageButton leaderboard = view.findViewById(R.id.leaderboard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(v.getContext(), Leaderboard.class);
                startActivity(intent);
            }
        });

        // Handle event for the signout button
        ImageButton signout = view.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.mAuth != null) {
                    Login.mAuth.signOut();
                    Intent intent = new Intent(v.getContext(), Welcome.class);
                    startActivity(intent);

                } else {
                    Welcome.mAuth.signOut();
                    Intent intent = new Intent(v.getContext(), Welcome.class);
                    startActivity(intent);
                }
            }
        });

        // Handle event for the score button
        ImageButton score = view.findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(v.getContext(), PracticeScoreOrTestScore.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUserInformation () {

        DocumentReference documentReference;

        if (Login.user != null) {
            documentReference = database.collection("user").document(Login.user.getEmail());
        } else {
            documentReference = database.collection("user").document(Welcome.user.getEmail());
        }

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("firstname") + " " +
                                documentSnapshot.getString("middlename") + " " +
                                documentSnapshot.getString("lastname");


                        nameTextView.setText(name);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
