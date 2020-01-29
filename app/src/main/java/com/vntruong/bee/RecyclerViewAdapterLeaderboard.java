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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapterLeaderboard extends RecyclerView.Adapter<RecyclerViewAdapterLeaderboard.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> name;
    private ArrayList<String> email;

    public static String selectedUserEmail;

    public RecyclerViewAdapterLeaderboard(ArrayList<String> name, ArrayList<String> email, Context context) {
        this.name = name;
        this.email = email;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.competitor_show, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView quiznumber = holder.name;

        quiznumber.setText(this.name.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedUserEmail = email.get(position);
                Intent intent = new Intent(v.getContext(), CompetitorResultShow.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.competitorName);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }
}
