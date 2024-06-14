package com.example.questionnaire;
import com.google.gson.annotations.SerializedName;

public class Question {
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String answerCorrect;
    private int position;


    public Question(String questionText, String answerA, String answerB, String answerC, String answerD, String correctAnswer, int position) {
        this.question = questionText;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.answerCorrect = correctAnswer;
        this.position = position;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public String getCorrectAnswer() {
        return answerCorrect;
    }
}
