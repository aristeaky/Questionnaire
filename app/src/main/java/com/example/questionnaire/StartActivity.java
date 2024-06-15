package com.example.questionnaire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button button;
    private EditText playerNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        playerNameEditText = findViewById(R.id.playerName);
        button = findViewById(R.id.button);
        button.setOnClickListener(this::onStartButtonClick);
    }

    private void onStartButtonClick(View view) {
        String playerName = playerNameEditText.getText().toString().trim();
        if (!playerName.isEmpty() && !playerName.equals(playerNameEditText.getHint().toString())) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("PLAYER_NAME", playerName);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
    }
}