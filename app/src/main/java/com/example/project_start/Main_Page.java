package com.example.project_start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Main_Page extends AppCompatActivity implements View.OnClickListener {

    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague;



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



        btnCreateLeague.setOnClickListener(this);
        btnCreateTeam.setOnClickListener(this);
        btnMyTeams.setOnClickListener(this);
        btnMyLeague.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
    }
}