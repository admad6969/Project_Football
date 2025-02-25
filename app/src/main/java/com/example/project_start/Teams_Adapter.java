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

    public Teams_Adapter(Context newContext , List<Team>newTeamsList){
        this.context = newContext;
        this.teamList = newTeamsList;
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
