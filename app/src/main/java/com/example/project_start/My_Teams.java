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

        teamsList = new ArrayList<>();


        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);


        var uid = FirebaseAuth.getInstance().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        firebaseDatabase.getReference("Teams").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Team team = dataSnapshot.getValue(Team.class);
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

    public void onItemClick2(Team team)
    {
        Intent tmpIntent;
        tmpIntent= new Intent(My_Teams.this, NonLeagueTeam.class);
        tmpIntent.putExtra("currentName",team.getTeamName());
        startActivity(tmpIntent);
    }

    @Override
    public void onClick(View v)
    {
        /*if (v == btnSearch)
        {
            String search = etSearch.getText().toString();
            if (search.isEmpty())
                getTeams();

            else
                getPostsBySearch(search);
        }*/
    }

    public void getTeams(){
        firebaseDatabase.getReference("Teams").addValueEventListener(new ValueEventListener() {
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

                    }
                });
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*public void getPostsBySearch(String search){
        firebaseDatabase.getReference("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamsList = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    Team team = data.getValue(Team.class);
                    if(team.getTeamName().equalsIgnoreCase(search)){
                        teamsList.add(team);
                    }
                }
                Teams_Adapter adapter = new Teams_Adapter(My_Teams.this, teamsList);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}