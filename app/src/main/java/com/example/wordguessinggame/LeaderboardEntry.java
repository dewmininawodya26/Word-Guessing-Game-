package com.example.wordguessinggame;

public class LeaderboardEntry {
    private String name;
    private int score;

    // Empty constructor is necessary for Retrofit to deserialize the JSON
    public LeaderboardEntry() {}

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return (name != null ? name : "Unknown") + " - " + score; // Handle null names
    }
}
