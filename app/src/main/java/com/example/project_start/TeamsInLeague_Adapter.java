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

public class TeamsInLeague_Adapter extends RecyclerView.Adapter<TeamsInLeague_Adapter.PostViewHolder> {
    private Context context;
    private List<Team> teamList;
    private TeamsInLeague_Adapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Team team);
    }

    public TeamsInLeague_Adapter(Context newContext , List<Team>newTeamsList, OnItemClickListener listener){
        this.context = newContext;
        this.teamList = newTeamsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamsInLeague_Adapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_team_in_league, null);
        return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.tvTeamName.setText(team.getTeamName());
        holder.tvPoints.setText(Integer.toString(team.getPoints()));
        holder.tvPlayed.setText(Integer.toString(team.getMatchesPlayed()));
        holder.tvWins.setText(Integer.toString(team.getWins()));
        holder.tvDraws.setText(Integer.toString(team.getDraws()));
        holder.tvLosses.setText(Integer.toString(team.getLosses()));
        if(!team.getLogo().isEmpty()){
            holder.ivTeamLogo.setImageBitmap(team.picToBitmap());
        }
        holder.itemView.setOnClickListener(view -> {
            if (listener != null)
            {
                listener.onItemClick(team);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName,tvPlayed, tvWins, tvDraws, tvLosses, tvPoints;
        ImageView ivTeamLogo;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPoints = (TextView) itemView.findViewById(R.id.tvPoints);
            tvTeamName = (TextView) itemView.findViewById(R.id.tvTeamName);
            tvPlayed  = (TextView) itemView.findViewById(R.id.tvPlayed);
            tvWins = (TextView) itemView.findViewById(R.id.tvWins);
            tvDraws = (TextView) itemView.findViewById(R.id.tvDraws);
            tvLosses = (TextView) itemView.findViewById(R.id.tvLosses);
            ivTeamLogo = (ImageView) itemView.findViewById(R.id.ivLogo);

        }
    }
}
