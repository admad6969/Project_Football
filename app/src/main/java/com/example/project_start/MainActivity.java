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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnlogin, btnSignup;
    EditText etUsername, etPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        btnlogin = (Button) findViewById(R.id.ButtonLogIn);
        btnSignup = (Button) findViewById(R.id.ButtonSignUp);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPass = (EditText) findViewById(R.id.etPassword);

        btnSignup.setOnClickListener(this);
        btnlogin.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fUser = firebaseAuth.getCurrentUser();
        /*if (fUser != null)
        {
            startActivity(new Intent(MainActivity.this, Main_Page.class));
        }*/
    }

    @Override
    public void onClick(View v) {
        if (v == btnlogin) {
            String email = "";
            String password = "";
            if (etUsername != null && !etUsername.equals("") && etPass != null && !etPass.equals("")) {
                email = etUsername.getText().toString();
                password = etPass.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, Main_Page.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "sign in error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Not entered correct email or pass", Toast.LENGTH_LONG).show();
            }

        }
        if (v == btnSignup) {
            Intent intent = new Intent(MainActivity.this, Sign_up.class);
            startActivity(intent);
        }
    }
}