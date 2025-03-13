package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NonLeagueTeam extends AppCompatActivity {

    ArrayList<Team> teamsList;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    TextView captain,manager,name;
    ImageView logo;

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
        teamsList = new ArrayList<>();

        Intent intent = getIntent();
        String currentName = intent.getStringExtra("currentName");
        name = (TextView) findViewById(R.id.tvTeamName);
        captain = (TextView) findViewById(R.id.tvCaptain);
        manager = (TextView) findViewById(R.id.tvManager);
        logo = (ImageView) findViewById(R.id.ivLogo);




        var uid = FirebaseAuth.getInstance().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        firebaseDatabase.getReference("Teams").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Team team = dataSnapshot.getValue(Team.class);
                    teamsList.add(team);
                }
                Team team = findTeamByName(currentName, teamsList);
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
}
