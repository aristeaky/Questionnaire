package com.example.questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private Button leaderboardButton;
    private String playerName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        titleTextView = findViewById(R.id.titleTextView);
        answersTextView = findViewById(R.id.answersTextView);
        tryAgainButton = findViewById(R.id.tryAgainButton);
        exitButton = findViewById(R.id.exitButton);
        reviewButton = findViewById(R.id.reviewButton);
        pieChart = findViewById(R.id.pieChart);
        leaderboardButton = findViewById(R.id.leaderboardButton);

        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra("CORRECT_ANSWERS_COUNT", 0);
        int totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0);
        correctAnswerStrings = intent.getStringArrayListExtra("CORRECT_ANSWERS_STRINGS");
        selectedAnswers = intent.getStringArrayListExtra("SELECTED_ANSWERS");
        playerName = intent.getStringExtra("PLAYER_NAME");

        percentage = (correctAnswers * 100) / totalQuestions;

        scoreTextView.setText("Score: " + percentage + "%");
        answersTextView.setText("Correct Answers: " + correctAnswers + "/" + totalQuestions);

        SharedPreferences sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int existingScore = sharedPreferences.getInt(playerName, -1);
        if (existingScore == -1 || percentage > existingScore) {
            editor.putInt(playerName, percentage);
            editor.apply();
        }

        setupPieChart(correctAnswers, totalQuestions);
        passOrFail();

        tryAgainButton.setOnClickListener(this::onTryAgainButtonClick);
        exitButton.setOnClickListener(this::onExitButtonClick);
        reviewButton.setOnClickListener(this::onReviewButtonClick);
        leaderboardButton.setOnClickListener(this::onLeaderboardButtonClick);
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

    private void onLeaderboardButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leaderboard");

        // Retrieve high scores from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        // Filter and sort high scores
        List<Map.Entry<String, Integer>> sortedEntries = allEntries.entrySet().stream()
                .filter(entry -> entry.getValue() instanceof Integer)
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), (Integer) entry.getValue()))
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toList());

        // Display top 10 scores
        int count = 0;
        StringBuilder leaderboardText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            leaderboardText.append(entry.getKey()).append(": ").append(entry.getValue()).append("%\n");
            count++;
            if (count >= 10) {
                break;
            }
        }

        builder.setMessage(leaderboardText.toString());
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void onTryAgainButtonClick(View view) {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.putExtra("PLAYER_NAME", playerName);
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
        intent.putExtra("PLAYER_NAME", playerName);
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
