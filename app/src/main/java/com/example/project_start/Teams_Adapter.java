package com.example.project_start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Teams_Adapter extends RecyclerView.Adapter<Teams_Adapter.PostViewHolder>{
    private Context context;
    private List<Team> teamList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Team team);
    }

    public Teams_Adapter(Context newContext , List<Team>newTeamsList, OnItemClickListener listener){
        this.context = newContext;
        this.teamList = newTeamsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_teams, null);
        return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.tvTeamName.setText(team.getTeamName());
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
        TextView tvTeamName;
        ImageView ivTeamLogo;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = (TextView) itemView.findViewById(R.id.tvTeamName);
            ivTeamLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        }
    }

}
