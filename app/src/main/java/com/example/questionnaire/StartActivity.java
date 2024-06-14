package com.example.questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button button;


    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);


        button= findViewById(R.id.button);
        button.setOnClickListener(this::onStartButtonClick);

    }

    private void onStartButtonClick(View view){
     Intent intent= new Intent(StartActivity.this,MainActivity.class);
     startActivity(intent);
    }
}
