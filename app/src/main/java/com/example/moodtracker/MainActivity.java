package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    TextView tvSelectedMood, tvDateTime;
    Button btnHappy, btnSad, btnAngry, btnSaveNote, btnViewHistoryTop; // Ændret navn til topbar
    EditText etMoodNote;

    String selectedMood = "";
    private static final String PREFS_NAME = "MoodPrefs";
    private static final String KEY_HISTORY = "history";

    Gson gson = new Gson();
    ArrayList<MoodEntry> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSelectedMood = findViewById(R.id.tvSelectedMood);
        tvDateTime = findViewById(R.id.tvDateTime);
        btnHappy = findViewById(R.id.btnHappy);
        btnSad = findViewById(R.id.btnSad);
        btnAngry = findViewById(R.id.btnAngry);
        etMoodNote = findViewById(R.id.etMoodNote);
        btnSaveNote = findViewById(R.id.btnSaveNote);

        // NYT: Topbar knap
        btnViewHistoryTop = findViewById(R.id.btnViewHistoryTop);

        updateDateTime();
        loadHistory();

        btnHappy.setOnClickListener(v -> {
            selectedMood = "😊";
            tvSelectedMood.setText("Dit humør: " + selectedMood);
            updateDateTime();
        });

        btnSad.setOnClickListener(v -> {
            selectedMood = "😢";
            tvSelectedMood.setText("Dit humør: " + selectedMood);
            updateDateTime();
        });

        btnAngry.setOnClickListener(v -> {
            selectedMood = "😡";
            tvSelectedMood.setText("Dit humør: " + selectedMood);
            updateDateTime();
        });

        btnSaveNote.setOnClickListener(v -> {
            String note = etMoodNote.getText().toString().trim();

            if (selectedMood.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vælg et humør først", Toast.LENGTH_SHORT).show();
                return;
            }

            if (note.isEmpty()) {
                Toast.makeText(MainActivity.this, "Skriv en note", Toast.LENGTH_SHORT).show();
                return;
            }

            String dateTime = tvDateTime.getText().toString();
            MoodEntry entry = new MoodEntry(selectedMood, note, dateTime);

            historyList.add(entry);
            saveHistory();

            Toast.makeText(MainActivity.this,
                    "Gemt: " + selectedMood + " - " + note + "\n" + dateTime,
                    Toast.LENGTH_LONG).show();

            etMoodNote.setText("");
        });

        // NYT: Topbar knap åbner historik
        btnViewHistoryTop.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class))
        );
    }

    private void updateDateTime() {
        String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        tvDateTime.setText("Tidspunkt: " + currentDateTime);
    }

    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(historyList);
        editor.putString(KEY_HISTORY, json);
        editor.apply();
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_HISTORY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<MoodEntry>>() {}.getType();
            historyList = gson.fromJson(json, type);
        }
    }
}
