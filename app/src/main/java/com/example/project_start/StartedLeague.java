package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Comparator;

public class StartedLeague extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rv;
    League selectedLeague;
    FirebaseDatabase firebaseDatabase;
    Intent intent;
    String currentName;
    String leagueUid;
    ArrayList<League> leaguesList;
    View dialogView;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    TextView tvDialogTeamName, tvDialogMp, tvDialogPts, tvDialogWins, tvDialogDraws, tvDialogLosees;
    ImageButton btnDialogPlusMp, btnDialogPlusPts, btnDialogPlusWins, btnDialogPlusDraws, btnDialogPlusLosses;
    ImageButton btnDialogMinusMp, btnDialogMinusPts, btnDialogMinusWins, btnDialogMinusDraws, btnDialogMinusLosses;
    Button btnDialogConfrim;
    ImageView ivDialogLogo;
    int mp, pts, w, d, l;
    Team selectedTeam;
    ArrayList<Team> leaguesTeams;
    String uid = FirebaseAuth.getInstance().getUid();
    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnExplorer, btnView, btnCreate;
    TextView tvtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_started_league);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rv = (RecyclerView) findViewById(R.id.rvLeagueTable);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        tvtTitle = (TextView) findViewById(R.id.tvTitle);

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        intent = getIntent();
        currentName = intent.getStringExtra("LeagueName");
        leagueUid = intent.getStringExtra("LeagueUid");

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.match_editor_dialog, null);
        builder = new AlertDialog.Builder(StartedLeague.this);
        builder.setView(dialogView);
        dialog = builder.create();

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
                tvtTitle.setText(selectedLeague.getLeagueName() + " table");
                leaguesTeams = selectedLeague.getTeamsInLeague();
                Collections.sort(leaguesTeams, new Comparator<Team>()
                {
                    @Override
                    public int compare(Team team1, Team team2) {
                        return Integer.compare(team2.getPoints(), team1.getPoints());
                    }
                });


                TeamsInLeague_Adapter adapter = new TeamsInLeague_Adapter(StartedLeague.this, leaguesTeams, new TeamsInLeague_Adapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(Team team) {
                        onClickTeam(team);
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

    public void onClickTeam(Team team)
    {
        selectedTeam = team;

        tvDialogTeamName = (TextView) dialogView.findViewById(R.id.tvDialogTeamName);
        tvDialogMp = (TextView) dialogView.findViewById(R.id.tvMp);
        tvDialogPts = (TextView) dialogView.findViewById(R.id.tvPts);
        tvDialogWins = (TextView) dialogView.findViewById(R.id.tvWins);
        tvDialogDraws = (TextView) dialogView.findViewById(R.id.tvDraws);
        tvDialogLosees = (TextView) dialogView.findViewById(R.id.tvLosses);
        btnDialogConfrim  = (Button) dialogView.findViewById(R.id.btnDialogConfirm);
        ivDialogLogo = (ImageView) dialogView.findViewById(R.id.ivLogo);

        btnDialogPlusMp = (ImageButton) dialogView.findViewById(R.id.IbPlusMp);
        btnDialogPlusPts = (ImageButton) dialogView.findViewById(R.id.IbPlusPts);
        btnDialogPlusWins = (ImageButton) dialogView.findViewById(R.id.IbPlusW);
        btnDialogPlusDraws = (ImageButton) dialogView.findViewById(R.id.IbPlusD);
        btnDialogPlusLosses = (ImageButton) dialogView.findViewById(R.id.IbPlusL);

        btnDialogMinusMp = (ImageButton) dialogView.findViewById(R.id.IbMinusMp);
        btnDialogMinusPts = (ImageButton) dialogView.findViewById(R.id.IbMinusPts);
        btnDialogMinusWins = (ImageButton) dialogView.findViewById(R.id.IbMinusW);
        btnDialogMinusDraws = (ImageButton) dialogView.findViewById(R.id.IbMinusD);
        btnDialogMinusLosses = (ImageButton) dialogView.findViewById(R.id.IbMinusL);


        mp = team.getMatchesPlayed();
        pts = team.getPoints();
        w = team.getWins();
        d = team.getDraws();
        l = team.getLosses();

        ivDialogLogo.setImageBitmap(team.picToBitmap());
        tvDialogTeamName.setText(team.getTeamName());
        tvDialogMp.setText(Integer.toString(mp));
        tvDialogPts.setText(Integer.toString(pts));
        tvDialogWins.setText(Integer.toString(w));
        tvDialogDraws.setText(Integer.toString(d));
        tvDialogLosees.setText(Integer.toString(l));

        dialog.show();

        btnDialogMinusLosses.setOnClickListener(this);
        btnDialogMinusDraws.setOnClickListener(this);
        btnDialogMinusWins.setOnClickListener(this);
        btnDialogMinusMp.setOnClickListener(this);
        btnDialogMinusPts.setOnClickListener(this);

        btnDialogPlusPts.setOnClickListener(this);
        btnDialogPlusMp.setOnClickListener(this);
        btnDialogPlusWins.setOnClickListener(this);
        btnDialogPlusDraws.setOnClickListener(this);
        btnDialogPlusLosses.setOnClickListener(this);

        btnDialogConfrim.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnCreateTeam)
        {
            Intent intent = new Intent(StartedLeague.this, Create_Team.class);
            startActivity(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(StartedLeague.this, Create_League.class);
            startActivity(intent);
        }

        if (v == btnMyLeague)
        {
            Intent intent = new Intent(StartedLeague.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btnExplorer)
        {
            Intent intent = new Intent(StartedLeague.this, Main_Page.class);
            startActivity(intent);
        }
        if (v==btnMyTeams)
        {
            Intent intent = new Intent(StartedLeague.this, My_Teams.class);
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
        if (v==btnDialogConfrim)
        {
            DatabaseReference refrenceLeagues = firebaseDatabase.getReference("Leagues").child(uid);

            selectedTeam.setPoints(pts);
            selectedTeam.setMatchesPlayed(mp);
            selectedTeam.setWins(w);
            selectedTeam.setDraws(d);
            selectedTeam.setLosses(l);

            leaguesTeams.set(findTeamLocationByname(selectedTeam.getTeamName(),leaguesTeams),selectedTeam);
            selectedLeague.setTeamsInLeague(leaguesTeams);
            leaguesList.set(findLeagueLocationByname(selectedLeague.getLeagueName(),leaguesList), selectedLeague);

            refrenceLeagues.setValue(leaguesList);

            dialog.dismiss();
        }
        if (v==btnDialogPlusLosses)
        {
            l++;
            mp++;
            tvDialogLosees.setText(Integer.toString(l));
            tvDialogMp.setText(Integer.toString(mp));
        }
        if (v==btnDialogPlusDraws)
        {
            d++;
            mp++;
            pts++;
            tvDialogDraws.setText(Integer.toString(d));
            tvDialogMp.setText(Integer.toString(mp));
            tvDialogPts.setText(Integer.toString(pts));
        }
        if (v==btnDialogPlusWins)
        {
            w++;
            mp++;
            pts = pts+3;
            tvDialogWins.setText(Integer.toString(w));
            tvDialogMp.setText(Integer.toString(mp));
            tvDialogPts.setText(Integer.toString(pts));
        }
        if (v==btnDialogPlusMp)
        {
            mp++;
            tvDialogMp.setText(Integer.toString(mp));
        }
        if (v==btnDialogPlusPts)
        {
            pts++;
            tvDialogPts.setText(Integer.toString(pts));
        }
        if (v==btnDialogMinusPts)
        {
            pts--;
            tvDialogPts.setText(Integer.toString(pts));
        }
        if (v==btnDialogMinusMp)
        {
            if (mp>0)
            {
                mp--;
                tvDialogMp.setText(Integer.toString(mp));
            }
        }
        if (v==btnDialogMinusWins)
        {
            if (w>0)
            {
                w--;
                mp--;
                pts = pts - 3;
                tvDialogWins.setText(Integer.toString(w));
                tvDialogMp.setText(Integer.toString(mp));
                tvDialogPts.setText(Integer.toString(pts));
            }
        }
        if (v==btnDialogMinusDraws)
        {
            if (d>0)
            {
                d--;
                mp--;
                pts--;
                tvDialogDraws.setText(Integer.toString(d));
                tvDialogMp.setText(Integer.toString(mp));
                tvDialogPts.setText(Integer.toString(pts));
            }
        }
        if (v==btnDialogMinusLosses)
        {
            if (l>0)
            {
                l--;
                mp--;
                tvDialogLosees.setText(Integer.toString(l));
                tvDialogMp.setText(Integer.toString(mp));
            }
        }
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
}
