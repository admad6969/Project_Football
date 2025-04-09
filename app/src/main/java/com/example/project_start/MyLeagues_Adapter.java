package com.example.project_start;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyLeagues_Adapter extends RecyclerView.Adapter<MyLeagues_Adapter.PostViewHolder>{
    private Context context;
    private List<League> LeagueList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(League league);
    }

    public MyLeagues_Adapter(Context newContext , List<League>newLeagueList, OnItemClickListener listener){
        this.context = newContext;
        this.LeagueList = newLeagueList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_myleagues, null);
        return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        League league = LeagueList.get(position);
        holder.tvLeagueName.setText(league.getLeagueName());
        if (league.getStarted())
        {
            holder.tvStarted.setText("league started");
            holder.tvStarted.setTextColor(Color.parseColor("#008000"));
        }
        else
        {
            holder.tvStarted.setText("league not started");
            holder.tvStarted.setTextColor(Color.parseColor("#C41E3A"));
        }
        if(!league.getLogo().isEmpty()){
            holder.ivLeagueLogo.setImageBitmap(league.picToBitmap());
        }
        holder.itemView.setOnClickListener(view -> {
            if (listener != null)
            {
                listener.onItemClick(league);
            }
        });

    }

    @Override
    public int getItemCount() {
        return LeagueList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeagueName, tvStarted;
        ImageView ivLeagueLogo;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeagueName = (TextView) itemView.findViewById(R.id.tvLeagueName);
            tvStarted = (TextView) itemView.findViewById(R.id.tvStarted);
            ivLeagueLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        }
    }

}
