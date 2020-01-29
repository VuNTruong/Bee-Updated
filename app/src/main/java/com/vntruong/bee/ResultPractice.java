package com.vntruong.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultPractice extends AppCompatActivity {

    public ArrayList<String> questions = new ArrayList<>();
    public ArrayList<String> optionAs = new ArrayList<>();
    public ArrayList<String> optionBs = new ArrayList<>();
    public ArrayList<String> optionCs = new ArrayList<>();
    public ArrayList<String> optionDs = new ArrayList<>();
    public String userAnswer [];
    public ArrayList<String> answerArray = new ArrayList<>();
    public ArrayList<String> pointsEarned = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_practice);

        // Fill the information related to the quiz into this class in order to make everything appear
        questions = Result.questions;
        optionAs = Result.optionA;
        optionBs = Result.optionB;
        optionCs = Result.optionC;
        optionDs = Result.optionD;
        userAnswer = Result.userAnswer;
        answerArray = Result.answerArray;
        pointsEarned = Result.pointsEarned;

        // Back and next button as well as other UI components
        Button nextButton = findViewById(R.id.nextResult);
        Button backButton = findViewById(R.id.backResult);
        final TextView questionView = findViewById(R.id.questionSolution);
        final TextView optionAView = findViewById(R.id.optionAResults);
        final TextView optionBView = findViewById(R.id.optionBResults);
        final TextView optionCView = findViewById(R.id.optionCResults);
        final TextView optionDView = findViewById(R.id.optionDResults);
        final TextView userAnswerView = findViewById(R.id.userAnswerResult);
        final TextView correctAnswerView = findViewById(R.id.correntAnswerResult);
        final TextView pointsEarnedView = findViewById(R.id.pointsEarnedResult);

        // Set scroll method for the questionView
        questionView.setMovementMethod(new ScrollingMovementMethod());

        // Set questions and all answers as well as user's answer to the TextViews
        questionView.setText(questions.get(0));
        optionAView.setText(optionAs.get(0));
        optionBView.setText(optionBs.get(0));
        optionCView.setText(optionCs.get(0));
        optionDView.setText(optionDs.get(0));
        userAnswerView.setText(userAnswer[0]);
        correctAnswerView.setText(answerArray.get(0));
        pointsEarnedView.setText(pointsEarned.get(0));

        // Handle event for the next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the index of current question
                int currentQuestion;
                currentQuestion = questions.indexOf(questionView.getText().toString());

                try {
                    // Add 1 to the current question to move to the next question
                    questionView.setText(questions.get(currentQuestion + 1));
                    optionAView.setText(optionAs.get(currentQuestion + 1));
                    optionBView.setText(optionBs.get(currentQuestion + 1));
                    optionCView.setText(optionCs.get(currentQuestion + 1));
                    optionDView.setText(optionDs.get(currentQuestion + 1));
                    userAnswerView.setText(userAnswer[currentQuestion + 1]);
                    correctAnswerView.setText(answerArray.get(currentQuestion + 1));
                    pointsEarnedView.setText(pointsEarned.get(currentQuestion + 1));
                } catch (IndexOutOfBoundsException ex) {
                    Toast.makeText(ResultPractice.this, "End of result", Toast.LENGTH_SHORT);
                }
            }
        });

        // Handle event for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the index of current question
                int currentQuestion;
                currentQuestion = questions.indexOf(questionView.getText().toString());

                try {
                    // Subtract 1 to the current question to move to the next question
                    questionView.setText(questions.get(currentQuestion - 1));
                    optionAView.setText(optionAs.get(currentQuestion - 1));
                    optionBView.setText(optionBs.get(currentQuestion - 1));
                    optionCView.setText(optionCs.get(currentQuestion - 1));
                    optionDView.setText(optionDs.get(currentQuestion - 1));
                    userAnswerView.setText(userAnswer[currentQuestion - 1]);
                    correctAnswerView.setText(answerArray.get(currentQuestion - 1));
                    pointsEarnedView.setText(pointsEarned.get(currentQuestion - 1));
                } catch (IndexOutOfBoundsException ex) {
                    Toast.makeText(ResultPractice.this, "End of result", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
