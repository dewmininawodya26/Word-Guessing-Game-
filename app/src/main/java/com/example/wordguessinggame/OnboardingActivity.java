package com.example.wordguessinggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {
    private EditText etUserName;
    private Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        etUserName = findViewById(R.id.etUserName);
        btnStartGame = findViewById(R.id.btnStartGame);

        btnStartGame.setOnClickListener(v -> {
            String userName = etUserName.getText().toString().trim();
            if (userName.isEmpty()) {
                Toast.makeText(OnboardingActivity.this, "Please enter a user name!", Toast.LENGTH_SHORT).show();
            } else {
                saveUserName(userName);
                startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
                finish(); // Close onboarding activity
            }
        });
    }

    private void saveUserName(String name) {
        SharedPreferences prefs = getSharedPreferences("WordGuessingGame", MODE_PRIVATE);
        prefs.edit().putString("userName", name).apply();
    }
}
