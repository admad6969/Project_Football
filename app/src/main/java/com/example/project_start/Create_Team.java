package com.example.project_start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Create_Team extends AppCompatActivity implements View.OnClickListener {

    EditText etTeamName, etMananger, etCaptain;
    Button btnCreate, btnLogo;
    Button btnCreateTeam, btnCreateLeague, btnMyTeams, btnMyLeague, btnExplorer, btnView, btncreate;
    ImageView ivPic;
    Bitmap php;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    ArrayList<Team> teams;
    ActivityResultLauncher<Intent> activityResultLauncherUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_team);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();

        teams = new ArrayList<>();


        etTeamName = (EditText) findViewById(R.id.etTeamName);
        etMananger = (EditText) findViewById(R.id.etManager);
        etCaptain = (EditText) findViewById(R.id.etCaptain);
        btnCreate = (Button) findViewById(R.id.btnSubmit);
        btnLogo = (Button) findViewById(R.id.btnUploadLogo);
        ivPic = (ImageView) findViewById(R.id.ivLogo);

        btnCreateTeam = (Button) findViewById(R.id.btnCreateTeam);
        btnCreateLeague = (Button) findViewById(R.id.btnCreateLeague);
        btnMyTeams = (Button) findViewById(R.id.btnMyTeams);
        btnMyLeague = (Button) findViewById(R.id.btnMyLeagues);
        btnExplorer = (Button) findViewById(R.id.btnExplorer);
        btnView = (Button) findViewById(R.id.btnView);
        btncreate = (Button) findViewById(R.id.btnCreateOpener);


        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        php = null;
        initiallizedActivityResults();
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogo.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        btnCreateLeague.setOnClickListener(this);
        btnCreateTeam.setOnClickListener(this);
        btnMyTeams.setOnClickListener(this);
        btnMyLeague.setOnClickListener(this);
        btncreate.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnExplorer.setOnClickListener(this);


        ref = firebaseDatabase.getReference("Teams").child(firebaseAuth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot snap : snapshot.getChildren())
                    {
                        teams.add(snap.getValue(Team.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnCreate)
        {
            if (!(etTeamName.getText().toString().isEmpty()) && !(etMananger.getText().toString().isEmpty() && !(etCaptain .getText().toString().isEmpty())))
            {
                if (searchForName(teams,etTeamName.getText().toString()))
                {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    String name = etTeamName.getText().toString();
                    String manager = etMananger.getText().toString();
                    String captain = etCaptain.getText().toString();
                    Team team = new Team(uid, name, captain, manager);
                    team.setPicAsString(php);
                    teams.add(team);
                    ref.setValue(teams);
                    Toast.makeText(Create_Team.this, "team created", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(Create_Team.this, "you already have a team with the same name", Toast.LENGTH_LONG).show();

            }
            else
                Toast.makeText(Create_Team.this, "must fill all info", Toast.LENGTH_LONG).show();

        }
        else if (v==btnLogo)
        {
            btnLogo.setText("Change Logo");
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            activityResultLauncherUpload.launch(intent);
        }
        if (v == btnCreateLeague)
        {
            Intent intent = new Intent(Create_Team.this, Create_League.class);
            startActivity(intent);
        }
        if (v == btnMyTeams)
        {
            Intent intent = new Intent(Create_Team.this, My_Teams.class);
            startActivity(intent);
        }
        if (v == btnMyLeague)
        {
            Intent intent = new Intent(Create_Team.this, My_Leagues.class);
            startActivity(intent);
        }
        if (v==btnExplorer)
        {
            Intent intent = new Intent(Create_Team.this, Main_Page.class);
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
    public void  initiallizedActivityResults()
    {
        activityResultLauncherUpload = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null)
                        {
                            Uri selected = result.getData().getData();
                            try
                            {
                                InputStream is = getContentResolver().openInputStream(selected);
                                php = BitmapFactory.decodeStream(is);
                                ivPic.setImageBitmap(php);
                            }
                            catch (FileNotFoundException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }
                    }
    });
    }
    public boolean searchForName(ArrayList<Team> teamsList, String name)
    {
        for (int i = 0; i < teamsList.size(); i++)
        {
            if (teamsList.get(i).getTeamName().equals(name))
                return false;
        }
        return true;
    }

}