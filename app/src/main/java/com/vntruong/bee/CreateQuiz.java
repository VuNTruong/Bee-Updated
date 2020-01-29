package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateQuiz extends AppCompatActivity {
    // Reference to the database
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        // Initiate the connection
        this.database = FirebaseFirestore.getInstance();
    }

    private void setQuiz (final String quizName) {
        Map<String, Object> questions = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            questions.put("Question " + i, new HashMap<>());
        }

        final DocumentReference documentReference = database.collection("computer science").document(quizName);

        documentReference.set(questions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        // Set information for the quiz info which is to be added to the quiz later
        Map<String, Object> quizInfo = new HashMap<>();

        EditText dateCreatedEditText = findViewById(R.id.dateCreatedEditText);
        EditText dueDateEditText = findViewById(R.id.dueDateEditText);
        EditText timeAllowedEditText = findViewById(R.id.timeAllowedEditText);

        Map<String, Object> info = new HashMap<>();
        info.put("Date started", dateCreatedEditText.getText().toString());
        info.put("Due date", dueDateEditText.getText().toString());
        info.put("Time allowed", timeAllowedEditText.getText().toString());

        quizInfo.put("Quiz info", info);

        documentReference.set(quizInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void createQuiz (View view) {
        EditText quizNumberCreate = findViewById(R.id.quizNumberCreate);

        setQuiz(quizNumberCreate.getText().toString());

        this.finish();

        Toast.makeText(this, "Quiz created", Toast.LENGTH_SHORT);
    }

}
