package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // UI-elementer
    TextView tvSelectedMood, tvDateTime;
    Button btnHappy, btnSad, btnAngry, btnSaveNote, btnViewHistoryTop; // Knapper
    EditText etMoodNote; // Tekstfelt til note

    // Gemmer det valgte humør
    String selectedMood = "";

    // Navne til SharedPreferences
    private static final String PREFS_NAME = "MoodPrefs";
    private static final String KEY_HISTORY = "history";

    // JSON-håndtering
    Gson gson = new Gson();

    // Liste over gemte humørdata
    ArrayList<MoodEntry> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate called");

        // Indlæser layoutet til aktiviteten
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "Layout set: activity_main");

        // Finder UI-elementerne i layoutet
        tvSelectedMood = findViewById(R.id.tvSelectedMood);
        tvDateTime = findViewById(R.id.tvDateTime);
        btnHappy = findViewById(R.id.btnHappy);
        btnSad = findViewById(R.id.btnSad);
        btnAngry = findViewById(R.id.btnAngry);
        etMoodNote = findViewById(R.id.etMoodNote);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnViewHistoryTop = findViewById(R.id.btnViewHistoryTop);
        Log.d("MainActivity", "UI elements initialized");

        // Viser dato og tid på skærmen
        updateDateTime();
        Log.d("MainActivity", "Date/time updated");

        // Indlæser tidligere gemte humørdata
        loadHistory();
        Log.d("MainActivity", "History loaded. Items: " + historyList.size());

        // Klik på "Glad"-knap
        btnHappy.setOnClickListener(v -> {
            selectedMood = "😊";
            tvSelectedMood.setText("Dit humør: " + selectedMood);
            updateDateTime();
            Log.d("MainActivity", "Mood selected: Happy");
        });

        // Klik på "Trist"-knap
        btnSad.setOnClickListener(v -> {
            selectedMood = "😢";
            tvSelectedMood.setText("Dit humør: " + selectedMood);
            updateDateTime();
            Log.d("MainActivity", "Mood selected: Sad");
        });

        // Klik på "Vred"-knap
        btnAngry.setOnClickListener(v -> {
            selectedMood = "😡";
            tvSelectedMood.setText("Dit humør: " + selectedMood);
            updateDateTime();
            Log.d("MainActivity", "Mood selected: Angry");
        });

        // Klik på "Gem note"-knap
        btnSaveNote.setOnClickListener(v -> {
            String note = etMoodNote.getText().toString().trim();
            Log.d("MainActivity", "Save button clicked. Note: " + note);

            // Hvis der ikke er valgt humør
            if (selectedMood.isEmpty()) {
                Log.d("MainActivity", "Save failed - No mood selected");
                Toast.makeText(MainActivity.this, "Vælg et humør først", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hvis noten er tom
            if (note.isEmpty()) {
                Log.d("MainActivity", "Save failed - Note is empty");
                Toast.makeText(MainActivity.this, "Skriv en note", Toast.LENGTH_SHORT).show();
                return;
            }

            // Opret ny humør-post
            String dateTime = tvDateTime.getText().toString();
            MoodEntry entry = new MoodEntry(selectedMood, note, dateTime);

            // Tilføj til historik og gem
            historyList.add(entry);
            saveHistory();
            Log.d("MainActivity", "Mood entry saved: " + selectedMood + " - " + note + " - " + dateTime);

            // Vis bekræftelse til brugeren
            Toast.makeText(MainActivity.this,
                    "Gemt: " + selectedMood + " - " + note + "\n" + dateTime,
                    Toast.LENGTH_LONG).show();

            // Ryd tekstfeltet
            etMoodNote.setText("");
        });

        // Klik på "Se historik"-knap (øverst i topbaren)
        btnViewHistoryTop.setOnClickListener(v -> {
            Log.d("MainActivity", "View History button clicked");
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });

        // Klik på "Hjælp"-knap (åbner ekstern side)
        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(v -> {
            String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }

    // Opdaterer dato/tid i tekstfeltet
    private void updateDateTime() {
        String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        tvDateTime.setText("Tidspunkt: " + currentDateTime);
    }

    // Gemmer historik i SharedPreferences som JSON
    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(historyList);
        editor.putString(KEY_HISTORY, json);
        editor.apply();
    }

    // Indlæser historik fra SharedPreferences
    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_HISTORY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<MoodEntry>>() {}.getType();
            historyList = gson.fromJson(json, type);
        }
    }
}
