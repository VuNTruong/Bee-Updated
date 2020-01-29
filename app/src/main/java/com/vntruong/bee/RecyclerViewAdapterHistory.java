package com.vntruong.bee;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> dates;
    private ArrayList<String> times;
    private ArrayList<String> durations;
    private ArrayList<String> scores;
    private ArrayList<String> quiznumbers;

    public RecyclerViewAdapterHistory (ArrayList<String> dates, ArrayList<String> times, ArrayList<String> durations
            , ArrayList<String> scores, ArrayList<String> quiznumbers, Context context) {
        this.dates = dates;
        this.times = times;
        this.durations = durations;
        this.scores = scores;
        this.quiznumbers = quiznumbers;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.attempt_practice, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TextView date = holder.date;
        TextView time = holder.time;
        TextView duration = holder.duration;
        TextView score = holder.score;
        TextView quiznumber = holder.quiznumber;

        date.setText(this.dates.get(position));
        time.setText(this.times.get(position));
        duration.setText(this.durations.get(position));
        score.setText(this.scores.get(position));
        quiznumber.setText(this.quiznumbers.get(position));
    }

    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView date;
        TextView time;
        TextView duration;
        TextView score;
        TextView quiznumber;

        ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            duration = itemView.findViewById(R.id.duration);
            score = itemView.findViewById(R.id.score);
            quiznumber = itemView.findViewById(R.id.quizDone);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
