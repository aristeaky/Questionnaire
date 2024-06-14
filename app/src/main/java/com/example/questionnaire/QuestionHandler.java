package com.example.questionnaire;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionHandler {

    private List<Question> questions;
    private Map<Integer, String> userAnswers = new HashMap<>();

    public QuestionHandler(Context context) {
        questions = loadQuestions(context);
    }

    private List<Question> loadQuestions(Context context) {
        List<Question> questionList = new ArrayList<>();
        try {

            InputStream is = context.getResources().openRawResource(R.raw.questions);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class); // Parse as JSON object

            JsonArray jsonArray = jsonObject.getAsJsonArray("questions");

            for (JsonElement element : jsonArray) {
                Question question = gson.fromJson(element, Question.class);
                questionList.add(question);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return questionList;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getUserAnswer(int questionIndex) {
        if (userAnswers.containsKey(questionIndex)) {
            String answer = userAnswers.get(questionIndex);
            return answer;
        } else {

            return null;
        }
    }

    public void setUserAnswer(int questionIndex, String answer) {
        userAnswers.put(questionIndex, answer);
    }


    public int getScore() {
        int correctCount = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = userAnswers.get(i);

            if (question != null && userAnswer != null && question.getCorrectAnswer() != null && question.getCorrectAnswer().equals(userAnswer)) {

                correctCount++;

            }
        }

        return correctCount;
    }

    public void resetUserAnswers() {
        userAnswers.clear();
    }
}


