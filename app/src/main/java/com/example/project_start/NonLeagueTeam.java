package com.example.project_start;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NonLeagueTeam extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Team> teamsList;
    String currentName;
    LinearLayout linierframe;
    ArrayList<League> copyLeagueList, leaguesList;
    FirebaseDatabase firebaseDatabase;
    TextView captain,manager,name, tvDialogLeagueName, tvDialogDescription, tvDialogCapacity, tvDialogAdvancers, tvDialogRelegation ;
    ImageView logo, leagueLogo, ivDialogLeagueLogo;
    RecyclerView rv;
    Team team;
    EditText etSearch;
    Button accept,decline,btnSearch, btnChangeLeague;
    TextView tvRequestTitle, tvLeagueName, tvCapacity;
    String uid = FirebaseAuth.getInstance().getUid();
    DatabaseReference refrenceRequests,referenceTeams;
    ArrayList<Team> requests;
    View dialogView;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    League selectedLeague;
    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnExplorer, btnView, btnCreate;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_non_league_team);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        team = new Team();

        Intent intent = getIntent();
        currentName = intent.getStringExtra("currentName");
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.request_dialog, null);
        builder = new AlertDialog.Builder(NonLeagueTeam.this);
        builder.setView(dialogView);
        dialog = builder.create();

        rv = (RecyclerView) findViewById(R.id.rv);
        linierframe = (LinearLayout) findViewById(R.id.linier2);
        name = (TextView) findViewById(R.id.tvTeamName);
        captain = (TextView) findViewById(R.id.tvCaptain);
        manager = (TextView) findViewById(R.id.tvManager);
        logo = (ImageView) findViewById(R.id.ivLogo);
        tvCapacity = (TextView) findViewById(R.id.tvCapacity);
        tvLeagueName = (TextView) findViewById(R.id.tvLeagueName);
        leagueLogo = (ImageView) findViewById(R.id.ivLeagueLogo);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnChangeLeague = (Button) findViewById(R.id.btnChangeLeague);

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
        btnMyTeams.setOnClickListener(this);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));



        var uid = FirebaseAuth.getInstance().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        getTeam();
        getLeague();


        firebaseDatabase.getReference("Teams").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                teamsList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    team = dataSnapshot.getValue(Team.class);
                    teamsList.add(team);
                }
                Intent intent = getIntent();
                String currentName = intent.getStringExtra("currentName");
                team = findTeamByName(currentName, teamsList);
                if (team.getInLeague().getCapacity()!=0)
                {
                    getRequests(team.getInLeague());

                    linierframe.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    etSearch.setVisibility(View.GONE);

                    selectedLeague = team.getInLeague();

                    tvLeagueName.setText(team.getInLeague().getLeagueName());
                    tvCapacity.setText(Integer.toString(team.getInLeague().getCapacity()));
                    leagueLogo.setImageBitmap(team.getInLeague().picToBitmap());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        btnChangeLeague.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    private void getLeague()
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
                            if (!(league.getStarted()))
                            {
                            leaguesList.add(league);
                            }
                        }
                    }

                }
                Leagues_Adapter adapter = new Leagues_Adapter(NonLeagueTeam.this, leaguesList, new Leagues_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(League league) {
                        onClick2(league);
                    }
                });
                rv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Team findTeamByName(String currentName, ArrayList<Team> teamsList)
    {
        boolean flag = true;
        ArrayList<Team> copyTeamsList = teamsList;
        for (int i = 0; i < copyTeamsList.size(); i++)
        {
            if (copyTeamsList.get(i).getTeamName().equals(currentName))
                return copyTeamsList.get(i);
        }
        return new Team();
    }
    public int findTeamLocationByname(String currentName, ArrayList<Team> teamsList)
    {
        boolean flag = true;
        ArrayList<Team> copyTeamsList = teamsList;
        for (int i = 0; i < copyTeamsList.size(); i++)
        {
            if (copyTeamsList.get(i).getTeamName().equals(currentName))
                return i;
        }
        return 1;
    }
public void getRequests(League league)
{
    requests = new ArrayList<>();
    firebaseDatabase.getReference("Team requests").child(league.getUid()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists())
            {
                for (DataSnapshot snap : snapshot.getChildren())
                {
                    var a = snap.getValue(Team.class);
                    if (a != null && !requests.stream().anyMatch(t -> t.getUid() == a.getUid()))
                        requests.add(a);
                }
            }
        }



        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}

    public void onClick2(League league)
    {
        getRequests(league);

        accept = (Button) dialogView.findViewById(R.id.btnAccept);
        decline = (Button) dialogView.findViewById(R.id.btnDecline);
        tvRequestTitle = (TextView) dialogView.findViewById(R.id.tvRequestText);
        tvDialogLeagueName = (TextView) dialogView.findViewById(R.id.tvDialogLeagueName);
        tvDialogDescription = (TextView) dialogView.findViewById(R.id.tvDialogDescription);
        tvDialogCapacity = (TextView) dialogView.findViewById(R.id.tvDialogCapacity);
        tvDialogAdvancers = (TextView) dialogView.findViewById(R.id.tvDialogAdvancers);
        tvDialogRelegation = (TextView) dialogView.findViewById(R.id.tvDialogRelegation);
        ivDialogLeagueLogo = (ImageView) dialogView.findViewById(R.id.ivDialogLeagueLogo);

        selectedLeague = league;

        tvDialogLeagueName.setText(selectedLeague.getLeagueName());
        tvDialogDescription.setText(selectedLeague.getDescription());
        tvDialogCapacity.setText(Integer.toString(selectedLeague.getCapacity()));
        tvDialogAdvancers.setText(Integer.toString(selectedLeague.getAdvancers()));
        tvDialogRelegation.setText(Integer.toString(selectedLeague.getRelegation()));
        ivDialogLeagueLogo.setImageBitmap(selectedLeague.picToBitmap());

        tvRequestTitle.setText("send request to join " + selectedLeague.getLeagueName() + " ?");

        dialog.show();

        accept.setOnClickListener(this);

        decline.setOnClickListener(this);

    }
    public void getTeam ()
    {
        teamsList = new ArrayList<>();
        firebaseDatabase.getReference("Teams").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    team = dataSnapshot.getValue(Team.class);
                    teamsList.add(team);
                }
                Intent intent = getIntent();
                String currentName = intent.getStringExtra("currentName");
                team = findTeamByName(currentName, teamsList);
                manager.setText(team.getManager());
                captain.setText(team.getCaptain());
                name.setText(team.getTeamName());
                logo.setImageBitmap(team.picToBitmap());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnCreateTeam)
        {
            Intent intent = new Intent(NonLeagueTeam.this, Create_Team.class);
            startActivity(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(NonLeagueTeam.this, Create_League.class);
            startActivity(intent);
        }

        if (v == btnMyLeague)
        {
            Intent intent = new Intent(NonLeagueTeam.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btnExplorer)
        {
            Intent intent = new Intent(NonLeagueTeam.this, Main_Page.class);
            startActivity(intent);
        }
        if (v==btnMyTeams)
        {
            Intent intent = new Intent(NonLeagueTeam.this, My_Teams.class);
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
        if (selectedLeague!=null)
        {
            refrenceRequests = firebaseDatabase.getReference("Team requests").child(selectedLeague.getUid());
            referenceTeams = firebaseDatabase.getReference("Teams").child(uid);
            if (v == btnSearch) {
                String search = etSearch.getText().toString();
                if (search.isEmpty())
                    Toast.makeText(NonLeagueTeam.this, "please type valid league name", Toast.LENGTH_LONG).show();
                else
                    getLeagueBySearch(search);
            }
            if (v == accept) {
                teamsList.remove(findTeamLocationByname(team.getTeamName(), teamsList));
                team.setInLeague(selectedLeague);
                teamsList.add(team);
                referenceTeams.setValue(teamsList);

                team.setInLeague(selectedLeague);
                requests.add(team);
                refrenceRequests.setValue(requests);

                requests = new ArrayList<>();

                Toast.makeText(NonLeagueTeam.this, "request sent", Toast.LENGTH_LONG).show();


                dialog.dismiss();

                linierframe.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                etSearch.setVisibility(View.GONE);

                tvLeagueName.setText(selectedLeague.getLeagueName());
                tvCapacity.setText(Integer.toString(selectedLeague.getCapacity()));
                leagueLogo.setImageBitmap(selectedLeague.picToBitmap());
            }
            if (v == decline) {
                dialog.dismiss();
            }
            if (v == btnChangeLeague) {
                team.setInLeague(new League());
                teamsList.remove(findTeamLocationByname(team.getTeamName(), teamsList));
                teamsList.add(team);
                referenceTeams.setValue(teamsList);

                requests.remove(findTeamLocationByname(currentName, requests));
                refrenceRequests.setValue(requests);

                linierframe.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                etSearch.setVisibility(View.VISIBLE);

            }
        }
    }

        public void getLeagueBySearch(String search)
        {
            copyLeagueList = new ArrayList<>();
            for (int i = 0; i < leaguesList.size(); i++)
            {
                if (leaguesList.get(i).getLeagueName().equals(etSearch.getText().toString()))
                {
                    copyLeagueList.add(leaguesList.get(i));
                }
            }
                if (copyLeagueList.size()==0)
                {
                    Toast.makeText(NonLeagueTeam.this, "League name not found", Toast.LENGTH_LONG).show();
                    copyLeagueList = leaguesList;
                    Leagues_Adapter adapter = new Leagues_Adapter(NonLeagueTeam.this, leaguesList, new Leagues_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(League league) {
                            onClick2(league);
                        }
                    });
                    rv.setAdapter(adapter);
                }
                else {
                    Leagues_Adapter adapter = new Leagues_Adapter(NonLeagueTeam.this, copyLeagueList, new Leagues_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(League league) {
                            onClick2(league);
                        }
                    });
                    rv.setAdapter(adapter);
                }
            }

}
