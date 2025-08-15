package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MoodPrefs";
    private static final String KEY_HISTORY = "history";

    private RecyclerView recyclerViewHistory;
    private Button btnBack, btnShowGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HistoryActivity", "onCreate called");

        setContentView(R.layout.activity_history);
        Log.d("HistoryActivity", "Layout set: activity_history");

        // Find UI-elementer
        btnBack = findViewById(R.id.btnBack);
        btnShowGraph = findViewById(R.id.btnShowGraph);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        Log.d("HistoryActivity", "UI elements initialized");

        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        Log.d("HistoryActivity", "RecyclerView layout manager set");

        // Load historik
        loadAndShowHistory();
        Log.d("HistoryActivity", "History loaded");

        // Tilbage-knap
        btnBack.setOnClickListener(v -> {
            Log.d("HistoryActivity", "Back button clicked");
            finish();
        });

        // Vis graf-knap
        btnShowGraph.setOnClickListener(v -> {
            Log.d("HistoryActivity", "Show Graph button clicked");
            Intent intent = new Intent(HistoryActivity.this, GraphActivity.class);
            startActivity(intent);
        });
    }


    private void loadAndShowHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_HISTORY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<MoodEntry>>() {}.getType();
            ArrayList<MoodEntry> historyList = gson.fromJson(json, type);

            MoodAdapter adapter = new MoodAdapter(historyList);
            recyclerViewHistory.setAdapter(adapter);
        }
    }
}
