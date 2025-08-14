package com.example.moodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MoodPrefs";
    private static final String KEY_HISTORY = "history";

    // >>> FARVER (ændr dem frit)
    private static final int COLOR_HAPPY   = Color.parseColor("#A1EB7C"); // gul
    private static final int COLOR_NEUTRAL = Color.parseColor("#FF5E5E"); // grå
    private static final int COLOR_SAD     = Color.parseColor("#FBFF5E"); // cornflower blue

    // Palette bruges til ukendte labels, så de ikke alle bliver grønne
    private static final int[] PALETTE = new int[] {
            Color.parseColor("#8BD3E6"),
            Color.parseColor("#F39C12"),
            Color.parseColor("#9B59B6"),
            Color.parseColor("#2ECC71"),
            Color.parseColor("#E74C3C"),
            Color.parseColor("#1ABC9C"),
            Color.parseColor("#E67E22"),
            Color.parseColor("#34495E")
    };
    // <<<

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Button btnBack = findViewById(R.id.btnBackToHistory);
        btnBack.setOnClickListener(v -> finish());

        FrameLayout container = findViewById(R.id.graphContainer);
        container.addView(new GraphView(this));
    }

    private class GraphView extends View {
        private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private final Map<String, Integer> moodCount = new HashMap<>();
        private int maxCount = 0;

        public GraphView(Context context) {
            super(context);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(40f);

            shadowPaint.setColor(Color.argb(50, 0, 0, 0));

            loadCounts();
        }

        private void loadCounts() {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String json = prefs.getString(KEY_HISTORY, null);
            if (json != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<MoodEntry>>() {}.getType();
                ArrayList<MoodEntry> history = gson.fromJson(json, type);

                for (MoodEntry e : history) {
                    String key = e.getMood() == null ? "" : e.getMood();
                    moodCount.put(key, moodCount.getOrDefault(key, 0) + 1);
                }
            }
            for (int c : moodCount.values()) {
                if (c > maxCount) maxCount = c;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (moodCount.isEmpty()) {
                textPaint.setTextSize(50f);
                canvas.drawText("Ingen data at vise", 50, 100, textPaint);
                return;
            }

            int width = getWidth();
            int height = getHeight();

            int n = moodCount.size();
            int barWidth = Math.max(60, width / Math.max(1, n * 2));  // bredde pr. søjle
            int spacing = Math.max(20, barWidth / 3);                  // mellemrum inde i “slotten”
            int x = barWidth;                                          // start-x
            int bottom = height - 100;

            for (Map.Entry<String, Integer> entry : moodCount.entrySet()) {
                String labelRaw = entry.getKey();
                int count = entry.getValue();

                // Vælg farve (robust match + fallback palette)
                barPaint.setColor(getColorForMood(labelRaw));

                int barHeight = (int) ((count / (float) maxCount) * (height - 220));
                int top = bottom - barHeight;

                // Skygge
                RectF shadow = new RectF(x + spacing, top + 5, x + barWidth - spacing, bottom + 5);
                canvas.drawRoundRect(shadow, 20, 20, shadowPaint);

                // Søjle
                RectF bar = new RectF(x + spacing, top, x + barWidth - spacing, bottom);
                canvas.drawRoundRect(bar, 20, 20, barPaint);

                // Label + tal
                canvas.drawText(labelRaw, x, bottom + 40, textPaint);
                canvas.drawText(String.valueOf(count), x, top - 10, textPaint);

                x += barWidth * 2;
            }
        }
    }

    // --- Hjælpefunktioner til farvevalg ---

    private static int getColorForMood(String raw) {
        if (raw == null) raw = "";

        // Hurtige emoji-tjek først
        if (raw.contains("😊") || raw.contains("😁") || raw.contains("😄")) return COLOR_HAPPY;
        if (raw.contains("😐") || raw.contains("😑")) return COLOR_NEUTRAL;
        if (raw.contains("😢") || raw.contains("😭") || raw.contains("☹")) return COLOR_SAD;

        // Normaliser tekst: fjern diakritik, behold kun bogstaver, til lower-case
        String norm = normalizeLetters(raw);

        // Match både dansk/engelsk nøgleord
        if (norm.contains("glad") || norm.contains("happy"))     return COLOR_HAPPY;
        if (norm.contains("neutral") || norm.contains("neutralt")) return COLOR_NEUTRAL;
        if (norm.contains("trist") || norm.contains("sad"))      return COLOR_SAD;

        // Ukendte labels: vælg en stabil farve fra palette baseret på hash
        return colorFromPalette(norm);
    }

    private static String normalizeLetters(String s) {
        // NFD -> fjern diakritik -> behold kun bogstaver -> lower
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        n = n.replaceAll("\\p{M}+", "");
        n = n.replaceAll("[^\\p{L}]+", "").toLowerCase();
        return n;
    }

    private static int colorFromPalette(String key) {
        if (key == null || key.isEmpty()) key = "unknown";
        int idx = Math.abs(key.hashCode());
        return PALETTE[idx % PALETTE.length];
        // Hvis du altid vil have grøn som fallback, returnér i stedet:
        // return Color.parseColor("#90EE90");
    }
}
