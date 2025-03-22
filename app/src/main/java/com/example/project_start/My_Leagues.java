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

public class My_Leagues extends AppCompatActivity implements View.OnClickListener {


    RecyclerView rv;
    ArrayList<League> leaguesList;
    FirebaseDatabase firebaseDatabase;
    Button btnSearch;
    EditText etSearch;
    FirebaseAuth firebaseAuth;
    final String uid = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_leagues);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        leaguesList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        etSearch = (EditText) findViewById(R.id.etSearch);


        btnSearch.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        getLeagues();
    }

    public void getLeagues() {
        firebaseDatabase.getReference("Leagues").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaguesList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    League league = data.getValue(League.class);
                    leaguesList.add(league);
                }
                Leagues_Adapter adapter = new Leagues_Adapter(My_Leagues.this, leaguesList, new Leagues_Adapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(League league) {
                        onItemClick2(league);
                    }
                });
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        public void onItemClick2 (League league)
        {
            Intent tmpIntent;
            tmpIntent = new Intent(My_Leagues.this, NonStartedLeague.class);
            tmpIntent.putExtra("currentName", league.getLeagueName());
            startActivity(tmpIntent);
        }

        @Override
        public void onClick (View v){
            if (v == btnSearch)
            {
                String search = etSearch.getText().toString();
                if (search.isEmpty())
                    getLeagues();
                else
                    getLeagueBySearch(search);
            }
        }

    public void getLeagueBySearch(String search){
        firebaseDatabase.getReference("Leagues").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaguesList = new ArrayList<>();
                for(DataSnapshot data : snapshot.getChildren()){
                    League league= data.getValue(League.class);
                    if(league.getLeagueName().equalsIgnoreCase(search)){
                        leaguesList.add(league);
                    }
                }
                if (leaguesList.size()==0)
                {
                    Toast.makeText(My_Leagues.this, "League name not found", Toast.LENGTH_LONG).show();
                    getLeagues();
                }
                else {
                    Leagues_Adapter adapter = new Leagues_Adapter(My_Leagues.this, leaguesList, new Leagues_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(League league) {
                            onItemClick2(league);
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
