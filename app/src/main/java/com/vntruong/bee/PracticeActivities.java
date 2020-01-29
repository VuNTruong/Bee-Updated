package com.vntruong.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PracticeActivities extends AppCompatActivity {

    FirebaseFirestore database;
    public static ArrayList<String> optionA;
    public static ArrayList<String> optionB;
    public static ArrayList<String> optionC;
    public static ArrayList<String> optionD;
    public static ArrayList<String> questions;
    private ArrayList<String> questionNumbers;
    public static ArrayList<String> questionIDs;
    public static ArrayList<String> pointsEarned;
    private ArrayList<String> answerKey;
    public static ArrayList<String> correctAnswerArray;

    // Public static variable for score to be accessed by the result class
    public static int score = 0;

    // static int value to be accessed by the Result class about the duration of the quiz
    public static String secondTaken = "00";
    public static String minuteTaken = "00";

    // This is going to be accessed by the result class
    public static String startTime;

    // TextView to display information about second
    TextView secondView;

    // TextView to display information about minute
    TextView minuteView;

    final Handler handler = new Handler();

    Runnable run = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 1000);

            String secondString = secondView.getText().toString();
            String minuteString = minuteView.getText().toString();

            int second = Integer.parseInt(secondString);
            int minute = Integer.parseInt(minuteString);

            if (second <= 59) {
                second += 1;

                if (second >= 0 && second < 10) {
                    secondView.setText("0" + second);
                    secondTaken = "0" + second;
                }
                else {
                    secondView.setText("" + second);
                    secondTaken = "" + second;
                }
            }

            if (second == 60) {
                minute += 1;
                if (minute >= 0 && minute < 10) {
                    minuteView.setText("0" + minute);
                    minuteTaken = "0" + minute;
                } else {
                    minuteView.setText("" + minute);
                    minuteTaken = "" + minute;
                }

                second = 00;
                secondView.setText("0" + second);
                secondTaken = "0" + second;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_activities);

        database = FirebaseFirestore.getInstance();

        optionA = new ArrayList<>();
        optionB = new ArrayList<>();
        optionC = new ArrayList<>();
        optionD = new ArrayList<>();
        questions = new ArrayList<>();
        questionNumbers = new ArrayList<>();
        questionIDs = new ArrayList<>();
        answerKey = new ArrayList<>();
        correctAnswerArray = new ArrayList<>();
        pointsEarned = new ArrayList<>();

        // TextView to display information about second
        secondView = findViewById(R.id.secondNew);

        // TextView to display information about minute
        minuteView = findViewById(R.id.minuteNew);

        handler.post(run);

        getDataQuestion(10, RecyclerViewAdapterQuizSelector.selectedQuizID);

    }

    private void getDataQuestion(final int numQuestions, final String quizNumber) {
        // DocumentReference which is used to reference to the collection which contains quizzes
        final DocumentReference documentReference = database.collection("computer science")
                .document(quizNumber);

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

                            System.out.println(map.size());

                            // Set of String which contains all keys of the maps that contains questions
                            Set<String> keys = map.keySet();

                            Iterator iterator = keys.iterator();

                            questionNumbers.add("Question " + j);
                            // Add components to the ArrayList that contains information about the quiz
                            // switch case is used to make sure that right field is placed in the right place
                            while (iterator.hasNext()) {
                                String keyValue = (String) iterator.next();

                                switch (keyValue) {
                                    // If the key is "Question", add the corresponding question content into the questions ArrayList
                                    case "Question":
                                        questions.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Option A", add the corresponding question content into the optionAs ArrayList
                                    case "Option A":
                                        optionA.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Option B", add the corresponding question content into the optionBs ArrayList
                                    case "Option B":
                                        optionB.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Option C", add the corresponding question content into the optionCs ArrayList
                                    case "Option C":
                                        optionC.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Option D", add the corresponding question content into the optionDs ArrayList
                                    case "Option D":
                                        optionD.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;

                                    // If the key is "Answer", add the corresponding question content into the questionKeys ArrayList
                                    case "Answer":
                                        answerKey.add(map.get(keyValue).toString());
                                        correctAnswerArray.add(map.get(keyValue).toString().replace("_n", "\n"));
                                        break;
                                }
                            }
                        }

                        Button nextButton = findViewById(R.id.newNextButton);
                        Button backButton = findViewById(R.id.newBackButton);
                        Button saveButton = findViewById(R.id.saveButton);
                        Button submitButton = findViewById(R.id.submitButton);
                        final RadioButton optionARadio = findViewById(R.id.optionANew);
                        final RadioButton optionBRadio = findViewById(R.id.optionBNew);
                        final RadioButton optionCRadio = findViewById(R.id.optionCNew);
                        final RadioButton optionDRadio = findViewById(R.id.optionDNew);
                        TextView questionTextView = findViewById(R.id.questionNew);

                        final TextView question = findViewById(R.id.questionNew);

                        final int currentQuestion = 0;

                        final String userAnswers [] = {"No response", "No response", "No response", "No response", "No response", "No response", "No response", "No response", "No response", "No response"};

                        question.setText(questions.get(currentQuestion));
                        optionARadio.setText(optionA.get(currentQuestion));
                        optionBRadio.setText(optionB.get(currentQuestion));
                        optionCRadio.setText(optionC.get(currentQuestion));
                        optionDRadio.setText(optionD.get(currentQuestion));
                        questionTextView.setMovementMethod(new ScrollingMovementMethod());

                        // Set these, so that when the user select one of the option, deselect others
                        optionARadio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                optionBRadio.setChecked(false);
                                optionCRadio.setChecked(false);
                                optionDRadio.setChecked(false);
                            }
                        });

                        optionBRadio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                optionARadio.setChecked(false);
                                optionCRadio.setChecked(false);
                                optionDRadio.setChecked(false);
                            }
                        });

                        optionCRadio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                optionARadio.setChecked(false);
                                optionBRadio.setChecked(false);
                                optionDRadio.setChecked(false);
                            }
                        });

                        optionDRadio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                optionARadio.setChecked(false);
                                optionBRadio.setChecked(false);
                                optionCRadio.setChecked(false);
                            }
                        });

                        nextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final int currentQuestion = questions.indexOf(question.getText().toString());
                                try {
                                    question.setText(questions.get(currentQuestion + 1));
                                    optionARadio.setText(optionA.get(currentQuestion + 1));
                                    optionBRadio.setText(optionB.get(currentQuestion + 1));
                                    optionCRadio.setText(optionC.get(currentQuestion + 1));
                                    optionDRadio.setText(optionD.get(currentQuestion + 1));

                                    if (userAnswers[currentQuestion + 1] == "No response") {
                                        optionARadio.setChecked(false);
                                        optionBRadio.setChecked(false);
                                        optionCRadio.setChecked(false);
                                        optionDRadio.setChecked(false);
                                    }

                                    if (userAnswers[currentQuestion + 1] == "") {
                                        optionARadio.setChecked(false);
                                        optionBRadio.setChecked(false);
                                        optionCRadio.setChecked(false);
                                        optionDRadio.setChecked(false);
                                    } else {
                                        if (userAnswers[currentQuestion + 1] == "A") {
                                            optionARadio.setChecked(true);
                                            optionBRadio.setChecked(false);
                                            optionCRadio.setChecked(false);
                                            optionDRadio.setChecked(false);
                                        } else if (userAnswers[currentQuestion + 1] == "B") {
                                            optionBRadio.setChecked(true);
                                            optionARadio.setChecked(false);
                                            optionCRadio.setChecked(false);
                                            optionDRadio.setChecked(false);
                                        } else if (userAnswers[currentQuestion + 1] == "C") {
                                            optionCRadio.setChecked(true);
                                            optionARadio.setChecked(false);
                                            optionBRadio.setChecked(false);
                                            optionDRadio.setChecked(false);
                                        } else if (userAnswers[currentQuestion + 1] == "D") {
                                            optionDRadio.setChecked(true);
                                            optionARadio.setChecked(false);
                                            optionBRadio.setChecked(false);
                                            optionCRadio.setChecked(false);
                                        }
                                    }
                                } catch (IndexOutOfBoundsException ex) {
                                    Toast toast = Toast.makeText(PracticeActivities.this, "End of test", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });

                        backButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int currentQuestion = questions.indexOf(question.getText().toString());
                                try {
                                    question.setText(questions.get(currentQuestion - 1));
                                    optionARadio.setText(optionA.get(currentQuestion - 1));
                                    optionBRadio.setText(optionB.get(currentQuestion - 1));
                                    optionCRadio.setText(optionC.get(currentQuestion - 1));
                                    optionDRadio.setText(optionD.get(currentQuestion - 1));

                                    if (userAnswers[currentQuestion - 1] == "No response") {
                                        optionARadio.setChecked(false);
                                        optionBRadio.setChecked(false);
                                        optionCRadio.setChecked(false);
                                        optionDRadio.setChecked(false);
                                    } else {
                                        if (userAnswers[currentQuestion - 1] == "A") {
                                            optionARadio.setChecked(true);
                                            optionBRadio.setChecked(false);
                                            optionCRadio.setChecked(false);
                                            optionDRadio.setChecked(false);
                                        } else if (userAnswers[currentQuestion - 1] == "B") {
                                            optionBRadio.setChecked(true);
                                            optionARadio.setChecked(false);
                                            optionCRadio.setChecked(false);
                                            optionDRadio.setChecked(false);
                                        } else if (userAnswers[currentQuestion - 1] == "C") {
                                            optionCRadio.setChecked(true);
                                            optionARadio.setChecked(false);
                                            optionBRadio.setChecked(false);
                                            optionDRadio.setChecked(false);
                                        } else if (userAnswers[currentQuestion - 1] == "D") {
                                            optionDRadio.setChecked(true);
                                            optionARadio.setChecked(false);
                                            optionBRadio.setChecked(false);
                                            optionCRadio.setChecked(false);
                                        }
                                    }
                                } catch (IndexOutOfBoundsException ex) {
                                    Toast toast = Toast.makeText(PracticeActivities.this, "First question of test", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        });

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int currentQuestion = questions.indexOf(question.getText().toString());

                                if (optionARadio.isChecked()) {
                                    userAnswers[currentQuestion] = "A";
                                } else if (optionBRadio.isChecked()) {
                                    userAnswers[currentQuestion] = "B";
                                } else if (optionCRadio.isChecked()) {
                                    userAnswers[currentQuestion] = "C";
                                } else if (optionDRadio.isChecked()) {
                                    userAnswers[currentQuestion] = "D";
                                }

                                Toast toast = Toast.makeText(PracticeActivities.this, "Answer saved " + currentQuestion, Toast.LENGTH_SHORT);
                                toast.show();

                            }
                        });

                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (check(userAnswers, "No response")) {
                                    new AlertDialog.Builder(PracticeActivities.this)
                                            .setTitle("Unfinished submission")
                                            .setMessage("You are not done yet. Are you sure you want to submit this quiz?")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Grade the quiz
                                                    score = gradeQuiz(userAnswers, answerKey);

                                                    for (int i = 0; i < 10; i++) {
                                                        if (gradeEachQuestion(userAnswers[i], answerKey.get(i))) {
                                                            pointsEarned.add("10/10");
                                                        } else {
                                                            pointsEarned.add("0/10");
                                                        }
                                                    }

                                                    // Instantiate the result activity
                                                    Intent intent = new Intent(PracticeActivities.this, Result.class);
                                                    startActivity(intent);

                                                    // Stop the clock
                                                    handler.removeCallbacks(run);

                                                    // Set the score variable of the Result class to the score of the quiz
                                                    Result.scoreStatic = score;

                                                    // Set the pointsEarned variable to of the Result class to the pointsEarned of the user
                                                    Result.pointsEarned = pointsEarned;

                                                    // Set the userAnswer and answer key to the Result class
                                                    Result.userAnswer = userAnswers;
                                                    Result.answerArray = correctAnswerArray;
                                                    Result.optionA = optionA;
                                                    Result.optionB = optionB;
                                                    Result.optionC = optionC;
                                                    Result.optionD = optionD;
                                                    Result.pointsEarned = pointsEarned;
                                                    Result.questions = questions;

                                                    // Finish the quiz activity
                                                    PracticeActivities.this.finish();
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                } else {
                                    new AlertDialog.Builder(PracticeActivities.this)
                                            .setTitle("Submit test")
                                            .setMessage("Are you sure you want to submit this quiz?")

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Grade the quiz
                                                    score = gradeQuiz(userAnswers, answerKey);

                                                    for (int i = 0; i < 10; i++) {
                                                        if (gradeEachQuestion(userAnswers[i], answerKey.get(i))) {
                                                            pointsEarned.add("10/10");
                                                        } else {
                                                            pointsEarned.add("0/10");
                                                        }
                                                    }

                                                    // Instantiate the result activity
                                                    Intent intent = new Intent(PracticeActivities.this, Result.class);
                                                    startActivity(intent);

                                                    // Stop the clock
                                                    handler.removeCallbacks(run);

                                                    // Set the score variable of the Result class to the score of the quiz
                                                    Result.scoreStatic = score;

                                                    // Set the pointsEarned variable to of the Result class to the pointsEarned of the user
                                                    Result.pointsEarned = pointsEarned;

                                                    // Set the userAnswer and answer key to the Result class
                                                    Result.userAnswer = userAnswers;
                                                    Result.answerArray = correctAnswerArray;
                                                    Result.optionA = optionA;
                                                    Result.optionB = optionB;
                                                    Result.optionC = optionC;
                                                    Result.optionD = optionD;
                                                    Result.pointsEarned = pointsEarned;
                                                    Result.questions = questions;

                                                    // Finish the quiz activity
                                                    PracticeActivities.this.finish();
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(PracticeActivities.this, "Fail to load test", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private static boolean check(String[] arr, String toCheckValue)
    {
        // check if the specified element
        // is present in the array or not
        // using Linear Search method
        boolean test = false;
        for (String element : arr) {
            if (element == toCheckValue) {
                test = true;
                break;
            }
        }

        return test;
    }

    private boolean gradeEachQuestion (String answer, String answerKey) {
        return answer.equals(answerKey);
    }

    private int gradeQuiz (String userAnswer [], ArrayList<String> answerKey) {
        int score = 0;

        for (int i = 0; i < 10; i++) {
            // Compare the answer given by the user with the answer key
            // If the user give the right answer, give them 10 point.
            // Otherwise, don't give them point
            if (userAnswer[i].equals(answerKey.get(i))) {
                score += 10;
            } else {
                score += 0;
            }
        }

        return score;
    }

    // Public static method for the result class to get access to the end time of the quiz
    public static String getEndTime () {
        String endTime;

        // Get the current date and time
        // and put it into the database as the time at which the quiz is done
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss");
        endTime = sdf.format(new Date());

        return endTime;
    }
}
