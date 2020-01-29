package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SolutionAndExplanation extends AppCompatActivity {

    FirebaseFirestore database;
    private ArrayList<String> questions = new ArrayList<>();
    private ArrayList<String> answerStrings = new ArrayList<>();
    private ArrayList<String> explanations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_and_explanation);

        database = FirebaseFirestore.getInstance();

        getDataQuestion(10);
    }

    private void getDataQuestion (final int numQuestions) {
        // DocumentReference which is used to reference to the collection which contains quizzes
        final DocumentReference documentReference = database.collection("computer science")
                .document(RecyclerViewAdapterQuizSelector.selectedQuizID);

        // Use the DocumentReference to get the information
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // HashMap which is used to store HashMap with corresponding information
                        /*
                        For instance
                        Question: "ABC"
                        Option A: "XYZ"
                        */
                        HashMap<String, Object> map;

                        // for loop which iterate through all questions of the quiz
                        for (int j = 1; j <= numQuestions; j++) {

                            // Get the question and put in the HashMap
                            map = (HashMap<String, Object>) documentSnapshot.get("Question " + j);

                            // Set of String which contains all keys of the maps that contains questions
                            Set<String> keys = map.keySet();

                            // The iterator which go through the map to determine how many keys are there in the map
                            Iterator iterator = keys.iterator();

                            // Add components to the ArrayList that contains information about the quiz
                            // switch case is used to make sure that right field is placed in the right place
                            while (iterator.hasNext()) {
                                String keyValue = (String) iterator.next();

                                switch (keyValue) {
                                    // If the key is "Answer string", add the corresponding question content into the questions ArrayList
                                    case "Question":
                                        questions.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Answer string", add the corresponding question content into the explanation ArrayList
                                    case "Answer string":
                                        answerStrings.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Explanation", add the corresponding question content into the explanation ArrayList
                                    case "Explanation":
                                        explanations.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;
                                }
                            }
                        }

                        // UI elements
                        final TextView questionView = findViewById(R.id.questionSolution);
                        final TextView answerView = findViewById(R.id.answerSolution);
                        final TextView explanationView = findViewById(R.id.explanationSolution);
                        Button nextButton = findViewById(R.id.nextSolution);
                        Button backButton = findViewById(R.id.backSolution);

                        // Set the text for the UI elements to show current question
                        questionView.setText(questions.get(0));
                        answerView.setText(answerStrings.get(0));
                        explanationView.setText(explanations.get(0));

                        // Handle event for buttons
                        nextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Get index of the current question
                                int currentQuestion;
                                currentQuestion = questions.indexOf(questionView.getText().toString());

                                try {
                                    questionView.setText(questions.get(currentQuestion + 1));
                                    answerView.setText(answerStrings.get(currentQuestion + 1));
                                    explanationView.setText(explanations.get(currentQuestion + 1));
                                } catch (IndexOutOfBoundsException ex) {
                                    Toast.makeText(SolutionAndExplanation.this, "End of solution", Toast.LENGTH_SHORT);
                                }
                            }
                        });

                        backButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Get index of the current question
                                int currentQuestion;
                                currentQuestion = questions.indexOf(questionView.getText().toString());

                                try {
                                    questionView.setText(questions.get(currentQuestion - 1));
                                    answerView.setText(answerStrings.get(currentQuestion - 1));
                                    explanationView.setText(explanations.get(currentQuestion - 1));
                                } catch (IndexOutOfBoundsException ex) {
                                    Toast.makeText(SolutionAndExplanation.this, "End of solution", Toast.LENGTH_SHORT);
                                }
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
