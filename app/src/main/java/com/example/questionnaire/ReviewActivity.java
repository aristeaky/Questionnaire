package com.example.questionnaire;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private TextView questionCounterTextView;
    private TextView questionTextView;
    private Button answerAButton;
    private Button answerBButton;
    private Button answerCButton;
    private Button answerDButton;
    private Button prevButton;
    private Button nextButton;
    private Button tryAgainButton;
    private Button exitButton;
    private TextView answerFeedbackTextView;
    private QuestionHandler questionHandler;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private ArrayList<String> selectedAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        questionHandler = new QuestionHandler(this);
        questions = questionHandler.getQuestions();

        Intent intent = getIntent();
        selectedAnswers = intent.getStringArrayListExtra("SELECTED_ANSWERS");
        if (selectedAnswers != null) {
            for (String answer : selectedAnswers) {
                Log.d("ReviewActivity", "Selected Answer: " + answer);
            }
        } else {
            Log.d("ReviewActivity", "Selected Answers is null");
        }

        questionCounterTextView = findViewById(R.id.questionCounterTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answerAButton = findViewById(R.id.answerAButton);
        answerBButton = findViewById(R.id.answerBButton);
        answerCButton = findViewById(R.id.answerCButton);
        answerDButton = findViewById(R.id.answerDButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        tryAgainButton = findViewById(R.id.tryAgainButton);
        exitButton = findViewById(R.id.exitButton);
        answerFeedbackTextView = findViewById(R.id.answerFeedbackTextView);


        prevButton.setOnClickListener(view -> showPreviousQuestion());
        nextButton.setOnClickListener(view -> showNextQuestion());
        tryAgainButton.setOnClickListener(view -> startQuizAgain());
        exitButton.setOnClickListener(view -> finish());

        displayQuestion(currentQuestionIndex);
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

            highlightSelectedAnswer(index);
        }
    }

    private void highlightSelectedAnswer(int questionIndex) {
        String selectedAnswerLetter = selectedAnswers.get(questionIndex);
        String correctAnswerLetter = questions.get(questionIndex).getCorrectAnswer();

        String selectedAnswerText = null;
        String correctAnswerText = null;

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

        if (correctAnswerLetter != null) {
            switch (correctAnswerLetter) {
                case "A":
                    correctAnswerText = questions.get(questionIndex).getAnswerA();
                    break;
                case "B":
                    correctAnswerText = questions.get(questionIndex).getAnswerB();
                    break;
                case "C":
                    correctAnswerText = questions.get(questionIndex).getAnswerC();
                    break;
                case "D":
                    correctAnswerText = questions.get(questionIndex).getAnswerD();
                    break;
                default:
                    break;
            }
        }
        Log.d("ReviewActivity", "Selected Answer Letter: " + selectedAnswerLetter);
        Log.d("ReviewActivity", "Selected Answer Text: " + selectedAnswerText);
        Log.d("ReviewActivity", "Correct Answer Letter: " + correctAnswerLetter);
        Log.d("ReviewActivity", "Correct Answer Text: " + correctAnswerText);

        resetAnswerButtonBackgrounds();
        answerFeedbackTextView.setText("");

        boolean isCorrect = false;

        if (selectedAnswerText != null) {

            for (Button button : new Button[]{answerAButton, answerBButton, answerCButton, answerDButton}) {
                if (button.getText().toString().equals(selectedAnswerText)) {
                    isCorrect = selectedAnswerText.equals(correctAnswerText);
                    highlightAnswerButton(button, isCorrect);
                    Log.d("ReviewActivity", "Highlighted Button: " + button.getText().toString() + ", Is Correct: " + isCorrect);
                }
            }

            if (isCorrect) {
                answerFeedbackTextView.setText("Correct");
                answerFeedbackTextView.setTextColor(Color.parseColor("#8BC34A"));
            } else {
                answerFeedbackTextView.setText("Incorrect");
                answerFeedbackTextView.setTextColor(Color.parseColor("#EF5350"));
            }
        }

        if(selectedAnswerText==null){
            answerFeedbackTextView.setText("Unanswered");
            answerFeedbackTextView.setTextColor(Color.parseColor("#EF5350"));
        }

        if (correctAnswerText != null && !correctAnswerText.equals(selectedAnswerText)) {
            for (Button button : new Button[]{answerAButton, answerBButton, answerCButton, answerDButton}) {
                if (button.getText().toString().equals(correctAnswerText)) {
                    highlightCorrectAnswer(button);
                }
            }
        }
    }

    private void highlightAnswerButton(Button button, boolean isCorrect) {
        int backgroundColor = isCorrect ? Color.parseColor("#8BC34A") : Color.parseColor("#EF5350");
        button.setBackgroundColor(backgroundColor);
    }

    private void highlightCorrectAnswer(Button button) {
        button.setBackgroundColor(Color.parseColor("#8BC34A"));
    }

    private void resetAnswerButtonBackgrounds() {
        int defaultColor = Color.parseColor("#FFFFFF");
        answerAButton.setBackgroundColor(defaultColor);
        answerBButton.setBackgroundColor(defaultColor);
        answerCButton.setBackgroundColor(defaultColor);
        answerDButton.setBackgroundColor(defaultColor);
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
        }
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion(currentQuestionIndex);
        }
    }

    private void startQuizAgain() {
        questionHandler.resetUserAnswers();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
