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
import android.widget.Spinner;
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
import java.util.List;

public class Create_League extends AppCompatActivity implements View.OnClickListener {

    EditText etLeagueName, etDescription, etLeagueCapacity, etAdvancers, etRelegation;
    Button btnCreate, btnLogo;
    ImageView ivPic;
    Bitmap php;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ActivityResultLauncher<Intent> activityResultLauncherUpload;
    DatabaseReference ref;
    ArrayList<League> leagues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_league);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseAuth = FirebaseAuth.getInstance();

        leagues = new ArrayList<>();

        etLeagueName = (EditText) findViewById(R.id.etLeagueName);
        etLeagueCapacity = (EditText) findViewById(R.id.etLeagueCap);
        etAdvancers = (EditText) findViewById(R.id.etAdvancers);
        etDescription = (EditText) findViewById(R.id.etLeagueDescription);
        etRelegation = (EditText) findViewById(R.id.etRelegation);
        btnCreate = (Button) findViewById(R.id.btnCreateTeam);
        btnLogo = (Button) findViewById(R.id.btnUploadLogo);
        ivPic = findViewById(R.id.ivLogo);
        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");
        php = null;
        initiallizedActivityResults();
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogo.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        ref = firebaseDatabase.getReference("Leagues").child(firebaseAuth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        leagues.add(snap.getValue(League.class));
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
            if (!(etLeagueName.getText().toString().isEmpty()) && !(etDescription.getText().toString().isEmpty()) && (Integer.parseInt( etLeagueCapacity.getText().toString())>1) && (Integer.parseInt( etAdvancers.getText().toString())>1) && (Integer.parseInt( etRelegation.getText().toString())>1) && ((Integer.parseInt(etRelegation.getText().toString()))+(Integer.parseInt(etAdvancers.getText().toString()))) < (Integer.parseInt(etLeagueCapacity.getText().toString())))
            {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                String name = etLeagueName.getText().toString();
                String description = etDescription.getText().toString();
                int cap = Integer.parseInt( etLeagueCapacity.getText().toString());
                int advancers = Integer.parseInt( etAdvancers.getText().toString());
                int relegation = Integer.parseInt( etRelegation.getText().toString());
                League league = new League(uid, name, description, cap, advancers, relegation);
                league.setPicAsString(php);
                leagues.add(league);
                ref.setValue(leagues);
                Toast.makeText(Create_League.this, "League created", Toast.LENGTH_LONG).show();
            }
        }
        else if (v==btnLogo)
        {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            activityResultLauncherUpload.launch(intent);
        }
    }
    public void  initiallizedActivityResults()
    {
        activityResultLauncherUpload = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>()
                {
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
                            } catch (FileNotFoundException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }
}
