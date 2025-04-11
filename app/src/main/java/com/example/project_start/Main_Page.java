package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Comparator;

public class Main_Page extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnSearch, btnExplorer, btnView, btncreate;
    EditText etSearch;
    ImageButton btnDismiss;
    TextView tvLeagueName;
    RecyclerView rvLeagues, rvTeams;
    FirebaseDatabase firebaseDatabase;
    ArrayList<League> leaguesList;
    String  uid;
    LinearLayout llLeagues, llTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCreateTeam = (Button) findViewById(R.id.btnCreateTeam);
        btnCreateLeague = (Button) findViewById(R.id.btnCreateLeague);
        btnMyTeams = (Button) findViewById(R.id.btnMyTeams);
        btnMyLeague = (Button) findViewById(R.id.btnMyLeagues);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        rvLeagues = (RecyclerView) findViewById(R.id.rvLeagues);
        rvTeams = (RecyclerView) findViewById(R.id.rvTeams);
        llLeagues = (LinearLayout) findViewById(R.id.llLeagues);
        llTable = (LinearLayout) findViewById(R.id.llTeams);
        tvLeagueName = (TextView) findViewById(R.id.tvLeagueName);
        btnDismiss = (ImageButton) findViewById(R.id.btnDismiss);
        btnExplorer = (Button) findViewById(R.id.btnExplorer);
        btnView = (Button) findViewById(R.id.btnView);
        btncreate = (Button) findViewById(R.id.btnCreateOpener);

        rvLeagues.setHasFixedSize(true);
        rvLeagues.setLayoutManager(new LinearLayoutManager(this));
        rvTeams.setHasFixedSize(true);
        rvTeams.setLayoutManager(new LinearLayoutManager(this));

        btnCreateLeague.setOnClickListener(this);
        btnCreateTeam.setOnClickListener(this);
        btnMyTeams.setOnClickListener(this);
        btnMyLeague.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnDismiss.setOnClickListener(this);
        btncreate.setOnClickListener(this);
        btnView.setOnClickListener(this);

        uid = FirebaseAuth.getInstance().getUid();

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");

        getLeagues();
    }


    public void getLeagues()
    {
        firebaseDatabase.getReference("Leagues").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaguesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        League league = data.getValue(League.class);
                        if (!(league.getUid().equals(uid)))
                        {
                            if (league.getStarted())
                            {
                                leaguesList.add(league);
                            }
                        }
                    }

                }
                Leagues_Adapter adapter = new Leagues_Adapter(Main_Page.this, leaguesList, new Leagues_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(League league) {
                        onClickTeam(league);
                    }
                });
                rvLeagues.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getLeagueBySearch(String search)
    {
        ArrayList<League> copyLeagueList = new ArrayList<>();
        for (int i = 0; i < leaguesList.size(); i++)
        {
            if (leaguesList.get(i).getLeagueName().equalsIgnoreCase(etSearch.getText().toString()))
            {
                copyLeagueList.add(leaguesList.get(i));
            }
        }
        if (copyLeagueList.size()==0)
        {
            Toast.makeText(Main_Page.this, "League name not found", Toast.LENGTH_LONG).show();
            copyLeagueList = leaguesList;
            Leagues_Adapter adapter = new Leagues_Adapter(Main_Page.this, leaguesList, new Leagues_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(League league) {
                    onClickTeam(league);
                }
            });
            rvLeagues.setAdapter(adapter);
        }
        else {
            Leagues_Adapter adapter = new Leagues_Adapter(Main_Page.this, copyLeagueList, new Leagues_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(League league) {
                    onClickTeam(league);
                }
            });
            rvLeagues.setAdapter(adapter);
        }
    }

@Override
    public void onClick(View v)
    {
        if (v==btnSearch)
        {
            String search = etSearch.getText().toString();
            if (search.isEmpty())
                Toast.makeText(Main_Page.this, "please type valid league name", Toast.LENGTH_LONG).show();
            else
                getLeagueBySearch(search);
        }
        if (v==btnDismiss)
        {
            llTable.setVisibility(View.GONE);
            llLeagues.setVisibility(View.VISIBLE);
        }
        if (v == btnCreateTeam)
        {
            Intent intent = new Intent(Main_Page.this, Create_Team.class);
            startActivity(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(Main_Page.this, Create_League.class);
            startActivity(intent);
        }
        if (v == btnMyTeams)
        {
            Intent intent = new Intent(Main_Page.this, My_Teams.class);
            startActivity(intent);
        }
        if (v == btnMyLeague)
        {
            Intent intent = new Intent(Main_Page.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btncreate)
        {
            btncreate.setVisibility(View.GONE);
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

    public void onClickTeam(League league)
    {
        llTable.setVisibility(View.VISIBLE);
        llLeagues.setVisibility(View.GONE);
        tvLeagueName.setText(league.getLeagueName() + " table");
        ArrayList<Team> leaguesTeams = league.getTeamsInLeague();
        Collections.sort(leaguesTeams, new Comparator<Team>()
        {
            @Override
            public int compare(Team team1, Team team2) {
                return Integer.compare(team2.getPoints(), team1.getPoints());
            }
        });
        TeamsInLeague_Adapter adapter = new TeamsInLeague_Adapter(Main_Page.this, leaguesTeams, new TeamsInLeague_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Team team) {

            }
        });
        rvTeams.setAdapter(adapter);
    }
}