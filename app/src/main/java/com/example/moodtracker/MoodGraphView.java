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

public class MoodGraphView extends View {
    private Map<String, Integer> moodCount = new HashMap<>();
    private Paint barPaint;
    private Paint textPaint;

    public MoodGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
    }

    public void setHistory(List<MoodEntry> historyList) {
        moodCount.clear();
        for (MoodEntry entry : historyList) {
            String mood = entry.getMood();
            moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
        }
        invalidate(); // Tegn igen
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (moodCount.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();

        int barWidth = width / moodCount.size();
        int maxCount = 0;
        for (int count : moodCount.values()) {
            if (count > maxCount) maxCount = count;
        }

        int i = 0;
        for (Map.Entry<String, Integer> entry : moodCount.entrySet()) {
            String mood = entry.getKey();
            int count = entry.getValue();

            // Vælg farve baseret på humør
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

            int barHeight = (int) ((count / (float) maxCount) * (height - 100));
            int left = i * barWidth + 20;
            int top = height - barHeight;
            int right = (i + 1) * barWidth - 20;
            int bottom = height;

            // Tegn søjlen
            canvas.drawRect(left, top, right, bottom, barPaint);

            // Tegn tekst under søjlen
            canvas.drawText(mood, left, height - 20, textPaint);

            i++;
        }
    }
}
