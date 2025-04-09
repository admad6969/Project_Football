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
    private ValueEventListener leaguesListener;

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

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);

        btnSearch.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        getLeagues();
    }

    public void getLeagues() {
        leaguesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaguesList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    League league = data.getValue(League.class);
                    leaguesList.add(league);
                }

                MyLeagues_Adapter adapter = new MyLeagues_Adapter(My_Leagues.this, leaguesList, new MyLeagues_Adapter.OnItemClickListener() {
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
        };

        firebaseDatabase.getReference("Leagues").child(uid).addValueEventListener(leaguesListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (leaguesListener != null) {
            firebaseDatabase.getReference("Leagues").child(uid).removeEventListener(leaguesListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (leaguesListener != null) {
            firebaseDatabase.getReference("Leagues").child(uid).removeEventListener(leaguesListener);
        }
    }

    public void onItemClick2(League league)
    {
        if (league.getStarted())
        {
            Intent intent = new Intent(My_Leagues.this, StartedLeague.class);
            intent.putExtra("LeagueName", league.getLeagueName());
            intent.putExtra("LeagueUid", league.getUid());
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(My_Leagues.this, NonStartedLeague.class);
            intent.putExtra("currentName", league.getLeagueName());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSearch) {
            String search = etSearch.getText().toString();
            if (search.isEmpty())
                getLeagues();
            else
                getLeagueBySearch(search);
        }
    }

    public void getLeagueBySearch(String search) {
        firebaseDatabase.getReference("Leagues").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaguesList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    League league = data.getValue(League.class);
                    if (league.getLeagueName().equalsIgnoreCase(search)) {
                        leaguesList.add(league);
                    }
                }
                if (leaguesList.size() == 0) {
                    Toast.makeText(My_Leagues.this, "League name not found", Toast.LENGTH_LONG).show();
                    getLeagues();
                } else {
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
