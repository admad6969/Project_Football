package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

public class NonStartedLeague extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvRequests, rvTeamsInLeague;
    LinearLayout llRequestButtons;
    String currentName;
    ArrayList<League> leaguesList;
    ArrayList<Team> requests, teamsList, teamsInleagueList;
    String uid = FirebaseAuth.getInstance().getUid();
    League selectedLeague;
    View dialogView;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    TextView tvTitle,tvCaptain,tvManager, tvLeagueName, tvNoRequests, tvNoTeams;
    Button btnDecline, btnAccept, btnStartLeague, btnRemoveTeam;
    ImageView ivTeamLogo;
    Team selectedTeam;
    Boolean flag = true;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference refrenceRequests = firebaseDatabase.getReference("Team requests").child(uid);
    DatabaseReference  refrenceLeagues = firebaseDatabase.getReference("Leagues").child(uid);
    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnExplorer, btnView, btnCreate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_non_started_league);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvRequests = (RecyclerView) findViewById(R.id.rvRequests);
        rvRequests.setHasFixedSize(true);
        rvRequests.setLayoutManager(new LinearLayoutManager(this));

        rvTeamsInLeague = (RecyclerView) findViewById(R.id.rvTeamsInLeaue);
        rvTeamsInLeague.setHasFixedSize(true);
        rvTeamsInLeague.setLayoutManager(new LinearLayoutManager(this));

        tvNoRequests = (TextView) findViewById(R.id.tvNoRequests);
        tvNoTeams = (TextView) findViewById(R.id.tvNoTeams);
        btnStartLeague = (Button) findViewById(R.id.btnStart);
        tvLeagueName = (TextView) findViewById(R.id.tvLeagueName);

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.get_request, null);
        builder = new AlertDialog.Builder(NonStartedLeague.this);
        builder.setView(dialogView);
        dialog = builder.create();

        Intent intent = getIntent();
        currentName = intent.getStringExtra("currentName");
        getRequests();
        getLeague();

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

        btnStartLeague.setOnClickListener(this);
    }

    private void getLeague()
    {
        firebaseDatabase.getReference("Leagues").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                leaguesList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    for (DataSnapshot data : dataSnapshot.getChildren())
                    {
                        League league = data.getValue(League.class);
                        if ((league.getUid().equals(uid)))
                        {
                            leaguesList.add(league);
                        }
                    }
                }
                selectedLeague = findLeagueByName(currentName,leaguesList);
                tvLeagueName.setText(selectedLeague.getLeagueName());
                ArrayList<Team> tempTeamList = selectedLeague.getTeamsInLeague();
                if(tempTeamList.get(0).getTeamName() != null)
                {
                    if (!(tempTeamList.get(0).getTeamName().equals("")))
                    {
                        tvNoTeams.setVisibility(View.GONE);
                        rvTeamsInLeague.setVisibility(View.VISIBLE);
                        TeamsInLeague_Adapter adapter2 = new TeamsInLeague_Adapter(NonStartedLeague.this, tempTeamList, new TeamsInLeague_Adapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Team team) {
                                onClickTeams(team);
                            }
                        });
                        rvTeamsInLeague.setAdapter(adapter2);
                    }
                    else
                    {
                        tvNoTeams.setVisibility(View.VISIBLE);
                        rvTeamsInLeague.setVisibility(View.GONE);
                    }
                }
                else
                {
                    tvNoTeams.setVisibility(View.VISIBLE);
                    rvTeamsInLeague.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getRequests()
    {
        requests = new ArrayList<>();
        firebaseDatabase.getReference("Team requests").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Team team = dataSnapshot.getValue(Team.class);
                    if (team != null && !requests.stream().anyMatch(t -> t.getUid() == team.getUid()))
                        requests.add(team);

                }
                if (requests.size()!=0)
                {
                    tvNoRequests.setVisibility(View.GONE);
                    rvRequests.setVisibility(View.VISIBLE);
                    TeamsInLeague_Adapter adapter = new TeamsInLeague_Adapter(NonStartedLeague.this, requests, new TeamsInLeague_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Team team) {
                            onClickRequests(team);
                        }
                    });
                    rvRequests.setAdapter(adapter);
                }
                else
                {
                    tvNoRequests.setVisibility(View.VISIBLE);
                    rvRequests.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public League findLeagueByName(String currentName, ArrayList<League> leaguesList)
    {
        boolean flag = true;
        ArrayList<League> copyLeaguesList = leaguesList;
        for (int i = 0; i < copyLeaguesList.size(); i++)
        {
            if (copyLeaguesList.get(i).getLeagueName().equals(currentName))
                return copyLeaguesList.get(i);
        }
        return new League();
    }

    public int findLeagueLocationByname(String currentName, ArrayList<League> LeagueList)
    {
        boolean flag = true;
        ArrayList<League> copyLeagueList = LeagueList;
        for (int i = 0; i < copyLeagueList.size(); i++)
        {
            if (copyLeagueList.get(i).getLeagueName().equals(currentName))
                return i;
        }
        return 1;
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

    public void getTeamsInSelectedLeague(String LeagueName)
    {
        teamsInleagueList = new ArrayList<>();
        firebaseDatabase.getReference("Teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (flag)
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ArrayList<Team> tempTeamList = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Team tempTeam = data.getValue(Team.class);
                            tempTeamList.add(tempTeam);
                        }

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Team tempTeam = data.getValue(Team.class);
                            if (tempTeam.getInLeague().getCapacity() != 0) {
                                if (tempTeam.getInLeague().getLeagueName().equals(LeagueName) && tempTeam.getAccepted()) {
                                    DatabaseReference referenceTeams = firebaseDatabase.getReference("Teams").child(tempTeam.getUid());
                                    League tempInLeague = tempTeam.getInLeague();
                                    tempInLeague.setStarted(true);
                                    tempTeam.setInLeague(tempInLeague);
                                    tempTeamList.set(findTeamLocationByname(tempTeam.getTeamName(), tempTeamList), tempTeam);
                                    referenceTeams.setValue(tempTeamList);
                                }
                            }
                        }
                    }
                    flag = false;
                    Intent intent = new Intent(NonStartedLeague.this, StartedLeague.class);
                    intent.putExtra("LeagueName", selectedLeague.getLeagueName());
                    intent.putExtra("LeagueUid", selectedLeague.getUid());
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getTeams(String thisUid)
    {
        teamsList = new ArrayList<>();
        firebaseDatabase.getReference("Teams").child(thisUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Team team = dataSnapshot.getValue(Team.class);
                    teamsList.add(team);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onClickTeams(Team team)
    {
        selectedTeam = team;

        tvTitle = (TextView) dialogView.findViewById(R.id.tvDialogTitle);
        tvManager = (TextView) dialogView.findViewById(R.id.tvDialogManager);
        tvCaptain = (TextView) dialogView.findViewById(R.id.tvDialogCaptain);
        ivTeamLogo = (ImageView) dialogView.findViewById(R.id.ivDialogLeagueLogo);
        btnAccept = (Button) dialogView.findViewById(R.id.btnAccept);
        btnDecline = (Button) dialogView.findViewById(R.id.btnDecline);
        llRequestButtons = (LinearLayout) dialogView.findViewById(R.id.llDialogRequest);
        btnRemoveTeam = (Button) dialogView.findViewById(R.id.btnRemove);

        llRequestButtons.setVisibility(View.GONE);
        btnRemoveTeam.setVisibility(View.VISIBLE);

        tvTitle.setText(team.getTeamName());
        tvManager.setText("manager: " + team.getManager());
        tvCaptain.setText("captain: " + team.getCaptain());
        ivTeamLogo.setImageBitmap(team.picToBitmap());

        dialog.show();

        getTeams(team.getUid());

        btnRemoveTeam.setOnClickListener(this);
    }

    public void onClickRequests(Team team)
    {
        selectedTeam = team;

        tvTitle = (TextView) dialogView.findViewById(R.id.tvDialogTitle);
        tvManager = (TextView) dialogView.findViewById(R.id.tvDialogManager);
        tvCaptain = (TextView) dialogView.findViewById(R.id.tvDialogCaptain);
        ivTeamLogo = (ImageView) dialogView.findViewById(R.id.ivDialogLeagueLogo);
        btnAccept = (Button) dialogView.findViewById(R.id.btnAccept);
        btnDecline = (Button) dialogView.findViewById(R.id.btnDecline);

        tvTitle.setText(team.getTeamName() + " wants to join your league!");
        tvManager.setText("manager: " + team.getManager());
        tvCaptain.setText("captain: " + team.getCaptain());
        ivTeamLogo.setImageBitmap(team.picToBitmap());

        btnAccept.setVisibility(View.VISIBLE);
        btnDecline.setVisibility(View.VISIBLE);

        dialog.show();

        btnDecline.setOnClickListener(this);
        btnAccept.setOnClickListener(this);

        getTeams(selectedTeam.getUid());
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnCreateTeam)
        {
            Intent intent = new Intent(NonStartedLeague.this, Create_Team.class);
            startActivity(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(NonStartedLeague.this, Create_League.class);
            startActivity(intent);
        }

        if (v == btnMyLeague)
        {
            Intent intent = new Intent(NonStartedLeague.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btnExplorer)
        {
            Intent intent = new Intent(NonStartedLeague.this, Main_Page.class);
            startActivity(intent);
        }
        if (v==btnMyTeams)
        {
            Intent intent = new Intent(NonStartedLeague.this, My_Teams.class);
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
        if (v==btnAccept)
        {
            if (selectedLeague.getCapacity()>selectedLeague.getTeamsInLeague().size())
            {
                DatabaseReference referenceTeams = firebaseDatabase.getReference("Teams").child(selectedTeam.getUid());
                selectedTeam.setAccepted(true);
                teamsList.remove(findTeamLocationByname(selectedTeam.getTeamName(), teamsList));
                teamsList.add(selectedTeam);
                referenceTeams.setValue(teamsList);

                ArrayList<Team> tempList = selectedLeague.getTeamsInLeague();
                if (tempList.get(0).getTeamName().equals("")) {
                    tempList.remove(0);
                }

                tempList.add(selectedTeam);
                selectedLeague.setTeamsInLeague(tempList);
                leaguesList.remove(findLeagueLocationByname(selectedLeague.getLeagueName(), leaguesList));
                leaguesList.add(selectedLeague);
                refrenceLeagues.setValue(leaguesList);

                requests.remove(findTeamLocationByname(selectedTeam.getTeamName(), requests));
                refrenceRequests.setValue(requests);

                getLeague();

                dialog.dismiss();
            }
            else
                Toast.makeText(NonStartedLeague.this, "League full", Toast.LENGTH_LONG).show();
        }
        if (v==btnDecline)
        {
            DatabaseReference referenceTeams = firebaseDatabase.getReference("Teams").child(selectedTeam.getUid());
            requests.remove(findTeamLocationByname(selectedTeam.getTeamName(),requests));
            refrenceRequests.setValue(requests);

            Team tempTeam = selectedTeam;
            tempTeam.setInLeague(new League());
            teamsList.remove(findTeamLocationByname(selectedTeam.getTeamName(),teamsList));
            teamsList.add(tempTeam);
            referenceTeams.setValue(teamsList);
            dialog.dismiss();
        }
        if (v==btnStartLeague)
        {
            if (selectedLeague.getCapacity()!=selectedLeague.getTeamsInLeague().size())
            {
                Toast.makeText(NonStartedLeague.this, "must have " + (selectedLeague.getCapacity()-selectedLeague.getTeamsInLeague().size()) + " more teams to start League", Toast.LENGTH_LONG).show();
            }
            if (selectedLeague.getCapacity()==selectedLeague.getTeamsInLeague().size())
            {
                selectedLeague.setStarted(true);
                leaguesList.remove(findLeagueLocationByname(selectedLeague.getLeagueName(),leaguesList));

                ArrayList<Team> inLeagueTeams = selectedLeague.getTeamsInLeague();
                for (int i = 0; i < inLeagueTeams.size(); i++)
                {
                    inLeagueTeams.get(i).getInLeague().setStarted(true);
                }
                selectedLeague.setTeamsInLeague(inLeagueTeams);

                leaguesList.add(selectedLeague);
                refrenceLeagues.setValue(leaguesList);

                //getTeamsInSelectedLeague(selectedLeague.getLeagueName());

                Intent intent = new Intent(NonStartedLeague.this, StartedLeague.class);
                intent.putExtra("LeagueName", selectedLeague.getLeagueName());
                intent.putExtra("LeagueUid", selectedLeague.getUid());
                startActivity(intent);
            }
        }
        if (v==btnRemoveTeam)
        {
            ArrayList<Team> tempTeamsListInLeague = selectedLeague.getTeamsInLeague();
            tempTeamsListInLeague.remove(findTeamLocationByname(selectedTeam.getTeamName(),tempTeamsListInLeague));
            if (tempTeamsListInLeague.size()==0)
            {
                tempTeamsListInLeague.add(new Team());
            }
            selectedLeague.setTeamsInLeague(tempTeamsListInLeague);

            leaguesList.remove(findLeagueLocationByname(selectedLeague.getLeagueName(), leaguesList));
            leaguesList.add(selectedLeague);
            refrenceLeagues.setValue(leaguesList);

            DatabaseReference referenceTeams = firebaseDatabase.getReference("Teams").child(selectedTeam.getUid());
            selectedTeam.setAccepted(false);
            selectedTeam.setInLeague(new League());
            teamsList.remove(findTeamLocationByname(selectedTeam.getTeamName(),teamsList));
            teamsList.add(selectedTeam);
            referenceTeams.setValue(teamsList);

            dialog.dismiss();

            llRequestButtons.setVisibility(View.VISIBLE);
            btnRemoveTeam.setVisibility(View.GONE);

            getLeague();

        }
    }
}