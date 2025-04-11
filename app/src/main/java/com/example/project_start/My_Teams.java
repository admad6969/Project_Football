package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class My_Teams extends AppCompatActivity implements View.OnClickListener {


    RecyclerView rv;
    ArrayList<Team> teamsList;
    FirebaseDatabase firebaseDatabase;
    Button btnSearch;
    EditText etSearch;
    FirebaseAuth firebaseAuth;
    final String uid = FirebaseAuth.getInstance().getUid();
    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnExplorer, btnView, btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_teams);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        getTeams();

        btnSearch.setOnClickListener(this);

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
        btnCreate.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnExplorer.setOnClickListener(this);

    }

    public void onItemClick2(Team team)
    {
        if (!(team.getAccepted()))
        {
            Intent tmpIntent;
            tmpIntent = new Intent(My_Teams.this, NonLeagueTeam.class);
            tmpIntent.putExtra("currentName", team.getTeamName());
            startActivity(tmpIntent);
        }
        if (team.getAccepted())
        {
            Intent tmpIntent;
            tmpIntent = new Intent(My_Teams.this, StartedTeams.class);
            tmpIntent.putExtra("LeagueUid", team.getInLeague().getUid());
            tmpIntent.putExtra("LeagueName", team.getInLeague().getLeagueName());
            startActivity(tmpIntent);
        }

    }

    @Override
    public void onClick(View v)
    {
        if (v == btnCreateTeam)
        {
            Intent intent = new Intent(My_Teams.this, Create_Team.class);
            startActivity(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(My_Teams.this, Create_League.class);
            startActivity(intent);
        }

        if (v == btnMyLeague)
        {
            Intent intent = new Intent(My_Teams.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btnExplorer)
        {
            Intent intent = new Intent(My_Teams.this, Main_Page.class);
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
        if (v == btnSearch)
        {
            String search = etSearch.getText().toString();
            if (search.isEmpty())
                getTeams();
            else
                getTeamBySearch(search);
        }
    }

    public void getTeams(){
        firebaseDatabase.getReference("Teams").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamsList = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Team team = data.getValue(Team.class);
                    teamsList.add(team);
                }
                Teams_Adapter adapter = new Teams_Adapter(My_Teams.this, teamsList, new Teams_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Team team) {
                        onItemClick2(team);
                    }
                });
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getTeamBySearch(String search){
        firebaseDatabase.getReference("Teams").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamsList = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    Team team = data.getValue(Team.class);
                    if(team.getTeamName().equalsIgnoreCase(search)){
                        teamsList.add(team);
                    }
                }
                if (teamsList.size()==0)
                {
                    Toast.makeText(My_Teams.this, "team name not found", Toast.LENGTH_LONG).show();
                    getTeams();
                }
                else {
                    Teams_Adapter adapter = new Teams_Adapter(My_Teams.this, teamsList, new Teams_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Team team)
                        {
                            onItemClick2(team);
                        }
                    });
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}