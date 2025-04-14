package com.example.project_start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class StartedTeams extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rv;
    League selectedLeague;
    FirebaseDatabase firebaseDatabase;
    String currentName;
    String leagueUid;
    ArrayList<League> leaguesList;
    TextView tvHasStarted, tvTitle;
    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnExplorer, btnView, btnCreate;


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

        tvTitle = (TextView) findViewById(R.id.tvTitle);

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");

        tvHasStarted = (TextView) findViewById(R.id.tvStartedLeague);

        Intent intent = getIntent();
        currentName = intent.getStringExtra("LeagueName");
        leagueUid = intent.getStringExtra("LeagueUid");

        btnCreateLeague = (Button) findViewById(R.id.btnCreateLeagueTab);
        btnCreateTeam = (Button) findViewById(R.id.btnCreateTeam);
        btnMyTeams = (Button) findViewById(R.id.btnMyTeams);
        btnMyLeague = (Button) findViewById(R.id.btnMyLeagues);
        btnExplorer = (Button) findViewById(R.id.btnExplorer);
        btnView = (Button) findViewById(R.id.btnView);
        btnCreate = (Button) findViewById(R.id.btnCreateOpener);

        btnCreateTeam.setOnClickListener(this);
        btnCreateLeague.setOnClickListener(this);
        btnMyLeague.setOnClickListener(this);
        btnMyTeams.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnExplorer.setOnClickListener(this);


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
                tvTitle.setText(selectedLeague.getLeagueName()+ " table");
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

    @Override
    public void onClick(View v)
    {
        if (v == btnCreateTeam)
        {
            Intent intent = new Intent(StartedTeams.this, Create_Team.class);
            startActivity(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(StartedTeams.this, Create_League.class);
            startActivity(intent);
        }

        if (v == btnMyLeague)
        {
            Intent intent = new Intent(StartedTeams.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btnExplorer)
        {
            Intent intent = new Intent(StartedTeams.this, Main_Page.class);
            startActivity(intent);
        }
        if (v==btnMyTeams)
        {
            Intent intent = new Intent(StartedTeams.this, My_Teams.class);
            startActivity(intent);
        }
        if (v== btnCreate)
        {
            btnCreate.setVisibility(View.GONE);
            btnCreateTeam.setVisibility(View.VISIBLE);
            btnCreateLeague.setVisibility(View.VISIBLE);
        }
        if (v==btnView)
        {
            btnView.setVisibility(View.GONE);
            btnMyLeague.setVisibility(View.VISIBLE);
            btnMyTeams.setVisibility(View.VISIBLE);
        }
    }
}