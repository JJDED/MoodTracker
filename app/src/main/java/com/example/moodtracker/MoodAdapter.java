package com.example.moodtracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    private ArrayList<MoodEntry> moodList;

    public MoodAdapter(ArrayList<MoodEntry> moodList) {
        this.moodList = moodList;
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        MoodEntry entry = moodList.get(position);

        holder.tvDateTime.setText(entry.dateTime);
        holder.tvMood.setText(entry.mood);
        holder.tvNote.setText(entry.note);

        // Farvekoder baseret på humør
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

    @Override
    public int getItemCount() {
        return moodList.size();
    }

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
