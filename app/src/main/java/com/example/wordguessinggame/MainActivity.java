package com.example.wordguessinggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // UI Components
    private TextView tvScore, tvTimer, tvHint, tvWordLength;
    private EditText etGuess;
    private Button btnSubmit, btnGetLetterCount, btnGetWordLength, btnGiveHint, btnViewLeaderboard;


    // Game Variables
    private String secretWord = "apple"; // Default word; will be randomized
    private int score = 100;
    private int attempts = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUIComponents();

        // Load saved user name
        checkForSavedUserName();

        startNewGame(); // Start a new game
        startTimer(); // Start the game timer

        // Set up button listeners
        setButtonListeners();
    }

    private void initializeUIComponents() {
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        tvHint = findViewById(R.id.tvHint);
        tvWordLength = findViewById(R.id.tvWordLength);
        etGuess = findViewById(R.id.etGuess);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnGetLetterCount = findViewById(R.id.btnGetLetterCount);
        btnGetWordLength = findViewById(R.id.btnGetWordLength);
        btnGiveHint = findViewById(R.id.btnGiveHint);
        btnViewLeaderboard = findViewById(R.id.btnViewLeaderboard);
    }

    private void checkForSavedUserName() {
        SharedPreferences prefs = getSharedPreferences("WordGuessingGame", MODE_PRIVATE);
        String savedName = prefs.getString("userName", "");
        if (savedName.isEmpty()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish(); // Close this activity
        }
    }

    private void setButtonListeners() {
        btnSubmit.setOnClickListener(v -> checkGuess());
        btnGetLetterCount.setOnClickListener(v -> getLetterCount());
        btnGetWordLength.setOnClickListener(v -> getWordLength());
        btnGiveHint.setOnClickListener(v -> giveHint());
        btnViewLeaderboard.setOnClickListener(v -> viewLeaderboard());
    }

    private void startNewGame() {
        secretWord = getRandomWord();
        attempts = 0;
        score = 100;
        updateScoreAndWordLengthDisplay();
        etGuess.setText("");
        startTimer();
    }

    private void updateScoreAndWordLengthDisplay() {
        tvScore.setText("Score: " + score);
        tvWordLength.setText("Word Length: " + secretWord.length());
    }

    private void checkGuess() {
        String guess = etGuess.getText().toString().trim();
        if (guess.isEmpty()) {
            Toast.makeText(this, "Please enter a guess!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (guess.equalsIgnoreCase(secretWord)) {
            Toast.makeText(this, "Correct! You win!", Toast.LENGTH_SHORT).show();
            submitScore();
            resetGame();
        } else {
            Toast.makeText(this, "Wrong guess! Try again.", Toast.LENGTH_SHORT).show();
            attempts++;
            score -= 10; // Deduct score for wrong attempt
            tvScore.setText("Score: " + score);
            checkAttempts();
        }
    }

    private void checkAttempts() {
        if (attempts >= 10) {
            Toast.makeText(this, "You've used all your attempts! The word was: " + secretWord, Toast.LENGTH_SHORT).show();
            resetGame();
        }
    }

    private void submitScore() {
        // Call your API to submit the score and username
        String userName = getSharedPreferences("WordGuessingGame", MODE_PRIVATE).getString("userName", "Guest");
        Toast.makeText(this, "Score submitted for " + userName + ": " + score, Toast.LENGTH_SHORT).show();
    }

    private void viewLeaderboard() {
        startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
    }

    private void getLetterCount() {
        String guess = etGuess.getText().toString();
        if (guess.isEmpty()) {
            Toast.makeText(this, "Please enter a letter!", Toast.LENGTH_SHORT).show();
            return;
        }

        char letter = guess.charAt(0);
        long count = secretWord.chars().filter(c -> c == letter).count();
        Toast.makeText(this, "Letter Count: " + count, Toast.LENGTH_SHORT).show();
        score -= 5;
        tvScore.setText("Score: " + score);
    }

    private void getWordLength() {
        Toast.makeText(this, "Word Length: " + secretWord.length(), Toast.LENGTH_SHORT).show();
        score -= 5;
        tvScore.setText("Score: " + score);
    }

    private void giveHint() {
        if (attempts >= 5) {
            String[] hints = {"fruit", "round", "green"};
            Toast.makeText(this, "Hint: " + hints[new Random().nextInt(hints.length)], Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Hints available after 5 attempts!", Toast.LENGTH_SHORT).show();
        }
    }
    private String getHintForWord(String word) {
        switch (word) {
            case "apple":
                return "A common fruit that is often red or green.";
            case "banana":
                return "A yellow fruit that monkeys love.";
            case "grape":
                return "Small and can be purple or green.";
            case "orange":
                return "Citrus fruit that is round.";
            case "mango":
                return "A tropical stone fruit.";
            default:
                return "No hint available.";
        }
    }


    private void resetGame() {
        startNewGame();
    }

    private String getRandomWord() {
        String[] words = {"apple", "banana", "grape", "orange", "mango","banana","teacher","woodapple"};
        return words[new Random().nextInt(words.length)];
    }

    private void startTimer() {
        // Reset the start time to the current time
        startTime = System.currentTimeMillis();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                tvTimer.setText("Timer: " + elapsedTime + "s");
                handler.postDelayed(this, 1000); // Repeat every second
            }
        }, 1000);
    }
}
