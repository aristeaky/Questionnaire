package com.example.questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView titleTextView;
    private TextView answersTextView;
    private Button tryAgainButton;
    private Button exitButton;
    private Button reviewButton;
    private ArrayList<String> correctAnswerStrings;
    private ArrayList<String> selectedAnswers;
    private PieChart pieChart;
    private int percentage;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        titleTextView= findViewById(R.id.titleTextView);
        answersTextView = findViewById(R.id.answersTextView);
        tryAgainButton = findViewById(R.id.tryAgainButton);
        exitButton = findViewById(R.id.exitButton);
        reviewButton = findViewById(R.id.reviewButton);
        pieChart = findViewById(R.id.pieChart);

        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra("CORRECT_ANSWERS_COUNT", 0);
        int totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0);
        correctAnswerStrings = intent.getStringArrayListExtra("CORRECT_ANSWERS_STRINGS");
        selectedAnswers = intent.getStringArrayListExtra("SELECTED_ANSWERS");


        percentage = (correctAnswers * 100) / totalQuestions;

        scoreTextView.setText("Score: " + percentage + "%");
        answersTextView.setText("Correct Answers: " + correctAnswers + "/" + totalQuestions);

        setupPieChart(correctAnswers, totalQuestions);
        passOrFail();

        tryAgainButton.setOnClickListener(this::onTryAgainButtonClick);
        exitButton.setOnClickListener(this::onExitButtonClick);
        reviewButton.setOnClickListener(this::onReviewButtonClick);
    }

    private void setupPieChart(int correctAnswers, int totalQuestions) {
        int incorrectAnswers = totalQuestions - correctAnswers;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(correctAnswers, "Correct"));
        entries.add(new PieEntry(incorrectAnswers, "Incorrect"));

        PieDataSet dataSet = new PieDataSet(entries, "Answers");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);

        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);


        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);

        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();


        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateY(1000);


    }
    private void onTryAgainButtonClick(View view) {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void onExitButtonClick(View view) {
        finishAffinity();
    }
    private void onReviewButtonClick(View view) {
        Intent intent = new Intent(ResultActivity.this, ReviewActivity.class);
        intent.putExtra("SELECTED_ANSWERS", selectedAnswers);
        startActivity(intent);
    }
    private void passOrFail(){
        if(percentage>=50){
            titleTextView.setText("PASS" +"\u2714");
            titleTextView.setTextColor(Color.GREEN);
        }
         else {
            titleTextView.setText("FAIL" + "\u2716");
            titleTextView.setTextColor(Color.RED);
        }
    }
}
