package com.example.m08_pr_1_2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Result> data;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, ArrayList<Result> data)
    {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        String[] rProperties = data.get(position).getData();
        holder.tvNick.setText(rProperties[0]);
        holder.tvTries.setText(rProperties[1]);
        holder.tvTime.setText(rProperties[2]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNick, tvTries, tvTime;


        ViewHolder(View itemView) {
            super(itemView);
            tvNick = itemView.findViewById(R.id.tvNick);
            tvTries= itemView.findViewById(R.id.tvTries);
            tvTime = itemView.findViewById(R.id.tvTime);
        }


    }
}
