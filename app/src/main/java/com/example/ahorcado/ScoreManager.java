package com.example.ahorcado;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    private static final String PREFS_NAME = "ahorcado_scores";
    private static final String SCORES_KEY = "scores";
    private static final int MAX_SCORE = 200;

    private SharedPreferences sharedPreferences;

    public ScoreManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveScore(String playerName, int score) {
        List<String> scores = getScores();
        boolean playerExists = false;

        for (int i = 0; i < scores.size(); i++) {
            String[] scoreEntry = scores.get(i).split(":");
            if (scoreEntry[0].equals(playerName)) {
                int newScore = Math.min(Integer.parseInt(scoreEntry[1]) + score, MAX_SCORE);
                scores.set(i, playerName + ":" + newScore);
                playerExists = true;
                break;
            }
        }

        if (!playerExists) {
            scores.add(playerName + ":" + Math.min(score, MAX_SCORE));
        }

        Collections.sort(scores, (s1, s2) -> {
            int score1 = Integer.parseInt(s1.split(":")[1]);
            int score2 = Integer.parseInt(s2.split(":")[1]);
            return Integer.compare(score2, score1);
        });

        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }

        sharedPreferences.edit().putString(SCORES_KEY, String.join(",", scores)).apply();
    }

    public void resetScore(String playerName) {
        List<String> scores = getScores();
        scores.removeIf(score -> score.split(":")[0].equals(playerName));
        sharedPreferences.edit().putString(SCORES_KEY, String.join(",", scores)).apply();
    }

    public List<String> getScores() {
        String scoresString = sharedPreferences.getString(SCORES_KEY, "");
        if (scoresString.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] scoresArray = scoresString.split(",");
            List<String> scores = new ArrayList<>();
            Collections.addAll(scores, scoresArray);
            return scores;
        }
    }

    public int getPlayerScore(String playerName) {
        List<String> scores = getScores();
        for (String scoreEntry : scores) {
            String[] parts = scoreEntry.split(":");
            if (parts[0].equals(playerName)) {
                return Integer.parseInt(parts[1]);
            }
        }
        return 0;
    }
}
