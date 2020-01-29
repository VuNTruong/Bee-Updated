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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapterQuizSelector extends RecyclerView.Adapter<RecyclerViewAdapterQuizSelector.ViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<String> quiznumber;
    private ArrayList<String> datestarted;
    private ArrayList<String> duedate;
    private ArrayList<String> timeallowed;

    // String value to hold the start time
    public static String startTime;

    public static String selectedQuizID;

    public RecyclerViewAdapterQuizSelector (ArrayList<String> quiznumber, ArrayList<String> datestarted, ArrayList<String> duedate
            , ArrayList<String> timeallowed, Context context) {
        this.quiznumber = quiznumber;
        this.datestarted = datestarted;
        this.duedate = duedate;
        this.timeallowed = timeallowed;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.quiz_selector, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TextView quiznumber = holder.quiznumber;
        TextView datestarted = holder.datestarted;
        TextView duedate = holder.duedate;
        TextView timeallowed = holder.timeallowed;

        quiznumber.setText(this.quiznumber.get(position));
        datestarted.setText(this.datestarted.get(position));
        duedate.setText(this.duedate.get(position));
        timeallowed.setText(this.timeallowed.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Message", quiznumber.getText().toString());
                selectedQuizID = quiznumber.getText().toString();

                // Date and time when the quiz begins
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd G 'at' HH:mm:ss");

                // Set the start time to the startTime
                startTime = sdf.format(new Date());

                Intent intent = new Intent(v.getContext(), PracticeActivities.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return quiznumber.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView quiznumber;
        TextView datestarted;
        TextView duedate;
        TextView timeallowed;
        View mView;

        ViewHolder(View itemView) {
            super(itemView);

            quiznumber = itemView.findViewById(R.id.quiznumber);
            datestarted = itemView.findViewById(R.id.datestarted);
            duedate = itemView.findViewById(R.id.duedate);
            timeallowed = itemView.findViewById(R.id.timeallowed);
            mView = itemView;
        }

        @Override
        public void onClick(View view) {

        }
    }
}
