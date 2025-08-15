package com.example.moodtracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * Adapter til at forbinde en liste af MoodEntry-objekter
 * med RecyclerView, så de kan vises på skærmen.
 */
public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    private ArrayList<MoodEntry> moodList; // Liste med gemte humørregistreringer

    // Modtager listen med humørdata, når adapteren oprettes
    public MoodAdapter(ArrayList<MoodEntry> moodList) {
        this.moodList = moodList;
    }

    /**
     * Opretter en ViewHolder ved at "inflatte" layoutet item_mood.xml
     */
    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(v);
    }

    /**
     * Binder data fra et MoodEntry-objekt til elementerne i ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        MoodEntry entry = moodList.get(position); // Henter ét humør fra listen

        // Viser tidspunkt, humør-ikon og note
        holder.tvDateTime.setText(entry.dateTime);
        holder.tvMood.setText(entry.mood);
        holder.tvNote.setText(entry.note);

        // Skifter baggrundsfarve afhængigt af humøret
        switch (entry.mood) {
            case "😀":
                holder.itemView.setBackgroundColor(Color.parseColor("#C8E6C9")); // grønlig
                break;
            case "😐":
                holder.itemView.setBackgroundColor(Color.parseColor("#FFF9C4")); // gul
                break;
            case "😔":
                holder.itemView.setBackgroundColor(Color.parseColor("#FFCDD2")); // rødlig
                break;
            default:
                holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0")); // grå
        }
    }

    /**
     * Returnerer hvor mange elementer der er i listen.
     */
    @Override
    public int getItemCount() {
        return moodList.size();
    }

    /**
     * ViewHolder der holder referencer til UI-elementerne i item_mood.xml
     */
    public static class MoodViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateTime, tvMood, tvNote;

        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvMood = itemView.findViewById(R.id.tvMood);
            tvNote = itemView.findViewById(R.id.tvNote);
        }
    }
}
