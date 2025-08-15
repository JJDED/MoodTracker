package com.example.moodtracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * En speciallavet View, der tegner et søjlediagram over antal humørregistreringer.
 * Hver søjle repræsenterer et humør og højden viser hvor ofte det er registreret.
 */
public class MoodGraphView extends View {
    // Gemmer hvor mange gange hvert humør er registreret
    private Map<String, Integer> moodCount = new HashMap<>();

    // Paint-objekter til søjler og tekst
    private Paint barPaint;
    private Paint textPaint;

    /**
     * Konstruktør der initialiserer pensler til at tegne søjler og tekst.
     */
    public MoodGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Søjlefarve (ændres dynamisk senere)
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        // Tekstfarve og størrelse
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
    }

    /**
     * Modtager en historikliste og tæller hvor mange gange hvert humør optræder.
     * Derefter beder den view’et om at tegne igen.
     */
    public void setHistory(List<MoodEntry> historyList) {
        moodCount.clear();
        for (MoodEntry entry : historyList) {
            String mood = entry.getMood();
            moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
        }
        invalidate(); // Beder systemet om at tegne view’et igen
    }

    /**
     * Tegner søjlediagrammet baseret på moodCount.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (moodCount.isEmpty()) return; // Ingenting at tegne

        int width = getWidth();
        int height = getHeight();

        // Beregn bredden på hver søjle
        int barWidth = width / moodCount.size();

        // Find det højeste antal for at skalere søjlerne
        int maxCount = 0;
        for (int count : moodCount.values()) {
            if (count > maxCount) maxCount = count;
        }

        int i = 0;
        for (Map.Entry<String, Integer> entry : moodCount.entrySet()) {
            String mood = entry.getKey();
            int count = entry.getValue();

            // Vælg farve baseret på humør (ændr efter behov)
            switch (mood.toLowerCase()) {
                case "glad":
                    barPaint.setColor(Color.YELLOW);
                    break;
                case "neutral":
                    barPaint.setColor(Color.GRAY);
                    break;
                case "trist":
                    barPaint.setColor(Color.BLUE);
                    break;
                default:
                    barPaint.setColor(Color.LTGRAY);
                    break;
            }

            // Beregn højden på søjlen (proportional med maxCount)
            int barHeight = (int) ((count / (float) maxCount) * (height - 100));

            // Koordinater for søjlen
            int left = i * barWidth + 20;
            int top = height - barHeight;
            int right = (i + 1) * barWidth - 20;
            int bottom = height;

            // Tegn søjlen
            canvas.drawRect(left, top, right, bottom, barPaint);

            // Tegn humørtekst under søjlen
            canvas.drawText(mood, left, height - 20, textPaint);

            i++;
        }
    }
}
