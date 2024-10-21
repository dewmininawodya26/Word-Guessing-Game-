package com.example.wordguessinggame;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface LeaderboardApi {
    @GET("leaderboard") // Adjust the endpoint as necessary
    Call<List<LeaderboardEntry>> getLeaderboard();
}
