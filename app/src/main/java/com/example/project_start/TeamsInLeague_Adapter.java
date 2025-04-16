package com.example.project_start;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
    {
        Team team = teamList.get(position);
        if (team.getInLeague().getStarted())
        {
            if (position == 0)
            {
                holder.llBack.setBackgroundColor(Color.rgb(255, 220, 115));
            }
            else if (team.getInLeague().getAdvancers() >= 2 && position == 1)
            {
                holder.llBack.setBackgroundColor(Color.rgb(216, 216, 216));
            }
            else if (team.getInLeague().getAdvancers() >= 3 && position == 2)
            {
                holder.llBack.setBackgroundColor(Color.rgb(206, 137, 70));
            }
            else if (position <= team.getInLeague().getAdvancers() && position > 2)
            {
                holder.llBack.setBackgroundColor(Color.rgb(176, 216, 230));
            }
            else if (team.getInLeague().getCapacity() - position <= team.getInLeague().getRelegation())
            {
                holder.llBack.setBackgroundColor(Color.rgb(255, 155, 158));
            }
        }
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
        LinearLayout llBack;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            llBack = (LinearLayout) itemView.findViewById(R.id.llBackground);
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
