package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;

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

public class StartedTeams extends AppCompatActivity {

    RecyclerView rv;
    League selectedLeague;
    FirebaseDatabase firebaseDatabase;
    Intent intent;
    String currentName;
    String leagueUid;
    ArrayList<League> leaguesList;




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

        intent = getIntent();
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
                ArrayList<Team> leaguesTeams = selectedLeague.getTeamsInLeague();
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