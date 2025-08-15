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

    // Navn på SharedPreferences filen og nøglen til historik
    private static final String PREFS_NAME = "MoodPrefs";
    private static final String KEY_HISTORY = "history";

    private RecyclerView recyclerViewHistory;
    private Button btnBack, btnShowGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("HistoryActivity", "onCreate called");

        // Indsætter layoutet activity_history.xml
        setContentView(R.layout.activity_history);
        Log.d("HistoryActivity", "Layout set: activity_history");

        // Finder knapper og listevisning i layoutet
        btnBack = findViewById(R.id.btnBack);
        btnShowGraph = findViewById(R.id.btnShowGraph);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        Log.d("HistoryActivity", "UI elements initialized");

        // Sætter layoutmanager til RecyclerView (lodret liste)
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        Log.d("HistoryActivity", "RecyclerView layout manager set");

        // Indlæser og viser gemt historik
        loadAndShowHistory();
        Log.d("HistoryActivity", "History loaded");

        // Tilbage-knap lukker denne aktivitet
        btnBack.setOnClickListener(v -> {
            Log.d("HistoryActivity", "Back button clicked");
            finish();
        });

        // "Vis graf"-knap åbner GraphActivity
        btnShowGraph.setOnClickListener(v -> {
            Log.d("HistoryActivity", "Show Graph button clicked");
            Intent intent = new Intent(HistoryActivity.this, GraphActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Indlæser gemt humørhistorik fra SharedPreferences
     * og viser den i RecyclerView via MoodAdapter.
     */
    private void loadAndShowHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_HISTORY, null);

        if (json != null) {
            // Konverterer JSON til ArrayList<MoodEntry>
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<MoodEntry>>() {}.getType();
            ArrayList<MoodEntry> historyList = gson.fromJson(json, type);

            // Sætter adapteren på RecyclerView for at vise listen
            MoodAdapter adapter = new MoodAdapter(historyList);
            recyclerViewHistory.setAdapter(adapter);
        }
    }
}
