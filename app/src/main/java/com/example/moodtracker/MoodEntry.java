package com.example.moodtracker;

/**
 * En simpel modelklasse, der repræsenterer én humørregistrering.
 * Indeholder humør-ikon, note og tidspunkt for registreringen.
 */
public class MoodEntry {
    String mood;      // Emoji eller tekst for humør
    String note;      // Note til humøret
    String dateTime;  // Tidspunkt for registreringen

    /**
     * Konstruktør der opretter en MoodEntry med humør, note og tidspunkt.
     */
    public MoodEntry(String mood, String note, String dateTime) {
        this.mood = mood;
        this.note = note;
        this.dateTime = dateTime;
    }

    /**
     * Getter for humør.
     */
    public String getMood() {
        return mood;
    }

    /**
     * Getter for note.
     */
    public String getNote() {
        return note;
    }

    /**
     * Getter for tidspunkt.
     */
    public String getDatetime() {
        return dateTime;
    }
}
