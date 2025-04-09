package com.example.project_start;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class Sign_up extends AppCompatActivity implements View.OnClickListener {

    Button btnSumbit, btndateOfBirth, btnPic;
    EditText etEnterUserName, etEnterPass, etFirstName, etLastName;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Bitmap bitmap;
    Date date;
    ImageView ivPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btndateOfBirth = (Button) findViewById(R.id.btnDate);
        btnPic = (Button) findViewById(R.id.btnPicture);
        btnSumbit = (Button) findViewById(R.id.btnSubmitSU);
        etEnterPass = (EditText) findViewById(R.id.etEnterPassword);
        etEnterUserName = (EditText) findViewById(R.id.etEnterUsername);
        etFirstName = (EditText) findViewById(R.id.etEnterFirstName);
        etLastName = (EditText) findViewById(R.id.etEnterLastName);
        ivPic = (ImageView) findViewById(R.id.ivPicture);
        firebaseDatabase = FirebaseDatabase.getInstance("https://newpcproject-c165b-default-rtdb.europe-west1.firebasedatabase.app/");


        btnSumbit.setOnClickListener(this);
        btnPic.setOnClickListener(this);
        btndateOfBirth.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        if(v==btnSumbit)
        {
            String em = "";
            String ps = "";
            if (true) //add checker
            {
                em = etEnterUserName.getText().toString();
                ps = etEnterPass.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(em, ps).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String FName = etFirstName.getText().toString();
                            String LName = etLastName.getText().toString();
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                            String em = firebaseAuth.getCurrentUser().getEmail().toString();
                            User user = new User(uid , em , FName , LName , date , "");
                            user.setPicAsString(bitmap);
                            DatabaseReference ref = firebaseDatabase.getReference("Users").child(uid);
                            firebaseAuth = FirebaseAuth.getInstance();
                            ref.setValue(user);
                            Toast.makeText(Sign_up.this, "Registration successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Sign_up.this, Main_Page.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Sign_up.this, "Registration successful", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            } else {
                Toast.makeText(Sign_up.this, "Not entered email or pass", Toast.LENGTH_LONG).show();
            }
        }
        else if(v==btndateOfBirth){
            Calendar systemCalender = Calendar.getInstance();
            int year = systemCalender.get(Calendar.YEAR);
            int month = systemCalender.get(Calendar.MONTH);
            int day = systemCalender.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(Sign_up.this, new SetDate(), year, month, day);
            datePickerDialog.show();
        } else if (v==btnPic) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent , 0);
        }

    }

    public class  SetDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = new Date(year, month, dayOfMonth);
            String str = dayOfMonth + "/" + (month+1) + "/" + year;
            btndateOfBirth.setText(str);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                bitmap = (Bitmap) data.getExtras().get("data");
                ivPic.setImageBitmap(bitmap);
            }
        }
    }
}