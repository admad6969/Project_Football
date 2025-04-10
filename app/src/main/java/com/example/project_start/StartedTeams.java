package com.example.project_start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StartedTeams extends AppCompatActivity {

    RecyclerView rv;
    League selectedLeague;
    FirebaseDatabase firebaseDatabase;
    String currentName;
    String leagueUid;
    ArrayList<League> leaguesList;
    TextView tvHasStarted;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_started_teams);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rv = (RecyclerView) findViewById(R.id.rvLeagueTable);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");

        tvHasStarted = (TextView) findViewById(R.id.tvStartedLeague);

        Intent intent = getIntent();
        currentName = intent.getStringExtra("LeagueName");
        leagueUid = intent.getStringExtra("LeagueUid");

        getLeagues();
    }

    public void getLeagues() {
        firebaseDatabase.getReference("Leagues").child(leagueUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaguesList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    League league = data.getValue(League.class);
                    leaguesList.add(league);
                }
                selectedLeague = findLeagueByname(currentName, leaguesList);
                if (!(selectedLeague.getStarted()))
                {
                    tvHasStarted.setVisibility(View.VISIBLE);
                }
                ArrayList<Team> leaguesTeams = selectedLeague.getTeamsInLeague();

                Collections.sort(leaguesTeams, new Comparator<Team>()
                        {
                            @Override
                            public int compare(Team team1, Team team2) {
                                return Integer.compare(team2.getPoints(), team1.getPoints());
                            }
                        });


                TeamsInLeague_Adapter adapter = new TeamsInLeague_Adapter(StartedTeams.this, leaguesTeams, new TeamsInLeague_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Team team) {

                    }
                });
                rv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public League findLeagueByname(String currentName, ArrayList<League> LeagueList)
    {
        boolean flag = true;
        ArrayList<League> copyLeagueList = LeagueList;
        for (int i = 0; i < copyLeagueList.size(); i++)
        {
            if (copyLeagueList.get(i).getLeagueName().equals(currentName))
                return copyLeagueList.get(i);
        }
        return new League();
    }
}