package com.example.moodtracker;

public class MoodEntry {
    String mood;
    String note;
    String dateTime;

    public MoodEntry(String mood, String note, String dateTime) {
        this.mood = mood;
        this.note = note;
        this.dateTime = dateTime;
    }


    // Getter for mood
    public String getMood() {
        return mood;
    }

    // Getter for note (valgfrit)
    public String getNote() {
        return note;
    }

    // Getter for datetime (valgfrit)
    public String getDatetime() {
        return dateTime;
    }
}
