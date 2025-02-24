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
    private List<Team> postList;

    public Teams_Adapter(Context newContext , List<Team>newPostList){
        this.context = newContext;
        this.postList = newPostList;
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
        Team product = postList.get(position);
        holder.tvTeamName.setText(product.getTeamName());
        if(!product.getLogo().isEmpty()){
            holder.ivTeamLogo.setImageBitmap(product.picToBitmap());
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
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
