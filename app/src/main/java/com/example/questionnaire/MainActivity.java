package com.example.questionnaire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionCounterTextView;
    private TextView questionTextView;
    private Button nextButton;
    private Button prvButton;
    private Button submitButton;
    private Button answerAButton;
    private Button answerBButton;
    private Button answerCButton;
    private Button answerDButton;
    private QuestionHandler questionHandler;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Button[] answerButtons;
    private String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionHandler = new QuestionHandler(this);
        questions = questionHandler.getQuestions();
        Log.d("MainActivity", "Questions loaded: " + questions.size());

        Intent intent = getIntent();
        playerName = intent.getStringExtra("PLAYER_NAME");

        questionCounterTextView = findViewById(R.id.questionCounterTextView);
        questionTextView = findViewById(R.id.questionTextView);
        nextButton = findViewById(R.id.nextButton);
        prvButton = findViewById(R.id.prevButton);
        submitButton = findViewById(R.id.submitButton);
        answerAButton = findViewById(R.id.answerAButton);
        answerAButton.setBackgroundResource(R.drawable.button_background);
        answerBButton = findViewById(R.id.answerBButton);
        answerCButton = findViewById(R.id.answerCButton);
        answerDButton = findViewById(R.id.answerDButton);
        answerButtons = new Button[]{answerAButton, answerBButton, answerCButton, answerDButton};
        setupButtonBackground(answerAButton);
        setupButtonBackground(answerBButton);
        setupButtonBackground(answerCButton);
        setupButtonBackground(answerDButton);

        nextButton.setOnClickListener(this::onNextClick);
        prvButton.setOnClickListener(this::onPrvClick);
        submitButton.setOnClickListener(this::onSubmitClick);

        for (Button button : answerButtons) {
            setupButtonBackground(button);
            button.setOnClickListener(this::onAnswerClick);
        }



        answerAButton.setOnClickListener(view -> handleAnswerClick("A"));
        answerBButton.setOnClickListener(view -> handleAnswerClick("B"));
        answerCButton.setOnClickListener(view -> handleAnswerClick("C"));
        answerDButton.setOnClickListener(view -> handleAnswerClick("D"));


        displayQuestion(currentQuestionIndex);
        updateButtonStates();


    }

    private void displayQuestion(int index) {
        if (index < questions.size()) {
            Question question = questions.get(index);
            questionCounterTextView.setText("Question " + (index + 1) + "/" + questions.size());
            questionTextView.setText(question.getQuestion());

            answerAButton.setText(question.getAnswerA());
            answerBButton.setText(question.getAnswerB());
            answerCButton.setText(question.getAnswerC());
            answerDButton.setText(question.getAnswerD());

            String selectedAnswerLetter = questionHandler.getUserAnswer(index);
            if (selectedAnswerLetter != null) {
                String selectedAnswerText = null;
                switch (selectedAnswerLetter) {
                    case "A":
                        selectedAnswerText = question.getAnswerA();
                        System.out.println("i click this");
                        break;
                    case "B":
                        selectedAnswerText = question.getAnswerB();
                        break;
                    case "C":
                        selectedAnswerText = question.getAnswerC();
                        break;
                    case "D":
                        selectedAnswerText = question.getAnswerD();
                        break;
                    default:
                        break;
                }

                for (Button button : answerButtons) {
                    boolean isSelected = button.getText().toString().trim().equalsIgnoreCase(selectedAnswerText.trim());
                    button.setSelected(isSelected);
                    Log.d("MainActivity", "Button Text: " + button.getText().toString());
                    Log.d("MainActivity", "Selected Answer Text: " + selectedAnswerText);
                    Log.d("MainActivity", "Button Selected: " + isSelected);
                }
            } else {
                for (Button button : answerButtons) {
                    button.setSelected(false);
                }
            }

            highlightSelectedAnswer(index);
        }
    }

    private void highlightSelectedAnswer(int questionIndex) {
        String selectedAnswerLetter = questionHandler.getUserAnswer(questionIndex);
        String selectedAnswerText = null;

        if (selectedAnswerLetter != null) {
            switch (selectedAnswerLetter) {
                case "A":
                    selectedAnswerText = questions.get(questionIndex).getAnswerA();
                    break;
                case "B":
                    selectedAnswerText = questions.get(questionIndex).getAnswerB();
                    break;
                case "C":
                    selectedAnswerText = questions.get(questionIndex).getAnswerC();
                    break;
                case "D":
                    selectedAnswerText = questions.get(questionIndex).getAnswerD();
                    break;
                default:
                    break;
            }
        }

        for (Button button : answerButtons) {
            if (selectedAnswerText != null) {
                boolean isSelected = button.getText().toString().equals(selectedAnswerText);
                button.setSelected(isSelected);
                Log.d("MainActivity", "Button " + button.getText().toString() + " selected: " + isSelected);
            } else {
                button.setSelected(false);
            }
        }
    }
    private void onNextClick(View view) {
        currentQuestionIndex++;
        displayQuestion(currentQuestionIndex);
        updateButtonStates();
    }

    private void onPrvClick(View view) {
        currentQuestionIndex--;
        displayQuestion(currentQuestionIndex);
        updateButtonStates();
    }

    private void onSubmitClick(View view) {
        int correctAnswersCount = questionHandler.getScore();
        int totalQuestions = questions.size();
        int scorePercentage = (correctAnswersCount * 100) / totalQuestions;

        List<String> correctAnswerStrings = new ArrayList<>();
        for (Question question : questions) {
            correctAnswerStrings.add(question.getCorrectAnswer());
        }

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("PLAYER_NAME", playerName);
        intent.putExtra("CORRECT_ANSWERS_COUNT", correctAnswersCount);
        intent.putStringArrayListExtra("CORRECT_ANSWERS_STRINGS", (ArrayList<String>) correctAnswerStrings);
        intent.putExtra("TOTAL_QUESTIONS", totalQuestions);

        SharedPreferences sharedPreferences = getSharedPreferences("HighScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int existingScore = sharedPreferences.getInt(playerName, -1);
        if (existingScore == -1 || scorePercentage > existingScore) {
            editor.putInt(playerName, scorePercentage);
            editor.apply();
        }

        ArrayList<String> selectedAnswers = new ArrayList<>();
        for (int i = 0; i < totalQuestions; i++) {
            selectedAnswers.add(questionHandler.getUserAnswer(i));
        }
        intent.putStringArrayListExtra("SELECTED_ANSWERS", selectedAnswers);

        startActivity(intent);
    }
    private void onAnswerClick(View view) {
        Button clickedButton = (Button) view;
        String answer = clickedButton.getText().toString();
        questionHandler.setUserAnswer(currentQuestionIndex, answer);
        highlightSelectedAnswer(currentQuestionIndex);
        Log.d("MainActivity", "Answer clicked: " + answer);
    }


    private void updateButtonStates() {
        prvButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);

    }
    private void handleAnswerClick(String answer) {
        questionHandler.setUserAnswer(currentQuestionIndex, answer);

        int totalQuestions = questions.size();

        if (currentQuestionIndex == totalQuestions - 1) {
            int correctAnswers = questionHandler.getScore();
            int scorePercentage = (correctAnswers * 100) / totalQuestions;

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("SCORE", scorePercentage);
            intent.putExtra("CORRECT_ANSWERS_COUNT", correctAnswers);
            intent.putExtra("TOTAL_QUESTIONS", totalQuestions);

            ArrayList<String> selectedAnswers = new ArrayList<>();
            for (int i = 0; i < totalQuestions; i++) {
                selectedAnswers.add(questionHandler.getUserAnswer(i));
            }
            intent.putStringArrayListExtra("SELECTED_ANSWERS", selectedAnswers);

            startActivity(intent);
        } else {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
            updateButtonStates();
        }
    }
    private void setupButtonBackground(Button button) {
        StateListDrawable drawable = new StateListDrawable();

        GradientDrawable pressedDrawable = new GradientDrawable();
        pressedDrawable.setColor(Color.parseColor("#014A91"));
        pressedDrawable.setCornerRadius(getResources().getDimension(R.dimen.button_corner_radius));
        drawable.addState(new int[] { android.R.attr.state_pressed }, pressedDrawable);

        GradientDrawable selectedDrawable = new GradientDrawable();
        selectedDrawable.setColor(Color.parseColor("#014A91"));
        selectedDrawable.setCornerRadius(getResources().getDimension(R.dimen.button_corner_radius));
        drawable.addState(new int[] { android.R.attr.state_selected }, selectedDrawable);

        GradientDrawable defaultDrawable = new GradientDrawable();
        defaultDrawable.setColor(Color.parseColor("#248BDD"));
        defaultDrawable.setCornerRadius(getResources().getDimension(R.dimen.button_corner_radius));
        drawable.addState(new int[] {}, defaultDrawable);

        button.setBackground(drawable);
    }
}
