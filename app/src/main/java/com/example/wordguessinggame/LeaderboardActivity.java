package com.example.wordguessinggame;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;
    private ArrayList<LeaderboardEntry> leaderboardEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        leaderboardEntries = new ArrayList<>();

        // Set up the RecyclerView
        leaderboardAdapter = new LeaderboardAdapter(leaderboardEntries);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        rvLeaderboard.setAdapter(leaderboardAdapter);

        loadLeaderboard();
    }

    private void loadLeaderboard() {
        LeaderboardApi api = RetrofitClient.getClient().create(LeaderboardApi.class);
        Call<List<LeaderboardEntry>> call = api.getLeaderboard();

        call.enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override
            public void onResponse(Call<List<LeaderboardEntry>> call, Response<List<LeaderboardEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LeaderboardEntry> entries = response.body();
                    Log.d("LeaderboardActivity", "Entries received: " + entries.size());
                    leaderboardEntries.clear();
                    leaderboardEntries.addAll(entries);
                    leaderboardAdapter.notifyDataSetChanged();
                } else {
                    Log.e("LeaderboardActivity", "Response unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<LeaderboardEntry>> call, Throwable t) {
                Log.e("LeaderboardActivity", "Failed to fetch leaderboard: " + t.getMessage());
            }
        });
    }
}
