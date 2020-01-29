package com.vntruong.bee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PracticeScoreOrTestScore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_score_or_test_score);
    }

    public void showTestScore (View view) {

    }

    public void showPracticeScore (View view) {
        Intent intent = new Intent(getApplicationContext(), AttemptHistory.class);
        startActivity(intent);
    }
}
