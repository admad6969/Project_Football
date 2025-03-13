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

public class Leagues_Adapter extends RecyclerView.Adapter<Leagues_Adapter.PostViewHolder> {


    private Context context;
    private List<League> leaguesList;
    private OnItemClickListener listener;



    public interface OnItemClickListener
    {
        void onItemClick(League league);
    }

    public Leagues_Adapter(Context newContext , List<League>newLeaguesList, OnItemClickListener listener){
        this.context = newContext;
        this.leaguesList = newLeaguesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Leagues_Adapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_leagues, null);
        return new PostViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Leagues_Adapter.PostViewHolder holder, int position) {
        League league = leaguesList.get(position);
        holder.tvLeagueName.setText(league.getLeagueName());
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
        return leaguesList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeagueName;
        ImageView ivLeagueLogo;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeagueName = (TextView) itemView.findViewById(R.id.tvLeagueName);
            ivLeagueLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
        }
    }

}
