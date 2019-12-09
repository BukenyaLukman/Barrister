package com.mobilesoftware.barrister.Attorney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobilesoftware.barrister.R;

import java.util.HashMap;

public class MyProfileActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText ProfileFirstName,ProfileLastName,ProfileTitle,
            ProfileFirmName,ProfileWebsite,ProfileEmail,ProfileBiography;
    private String FirstName,LastName,Title,FirmName,Website,Email,Biography,AttorneyKey;
    private Button SubmitButton;
    private ProgressDialog loadingBar;

    private FirebaseUser mAuth;
    private DatabaseReference AttorneyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mToolbar = findViewById(R.id.my_profile_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        AttorneyRef = FirebaseDatabase.getInstance().getReference().child("AttorneyPersonalData");


        initializeFields();

        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveToDatabase();
            }
        });
    }

    private void initializeFields() {
        ProfileLastName = findViewById(R.id.profile_last_name);
        ProfileFirstName = findViewById(R.id.profile_first_name);
        ProfileWebsite = findViewById(R.id.profile_website);
        ProfileTitle = findViewById(R.id.profile_title);
        ProfileFirmName = findViewById(R.id.profile_firm_name);
        ProfileBiography = findViewById(R.id.profile_biography);
        ProfileEmail = findViewById(R.id.profile_email);
        SubmitButton = findViewById(R.id.profile_submit);
        loadingBar = new ProgressDialog(this);
    }

    private void SaveToDatabase(){
        Website = ProfileWebsite.getText().toString();
        Title = ProfileTitle.getText().toString();
        FirmName = ProfileFirmName.getText().toString();
        Email = ProfileEmail.getText().toString();
        FirstName = ProfileFirstName.getText().toString();
        LastName = ProfileLastName.getText().toString();
        Biography = ProfileBiography.getText().toString();


        AttorneyKey = FirstName + " " + LastName;


        if (TextUtils.isEmpty(Title)){
            Toast.makeText(this, "Please Enter title", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(FirstName)){
            Toast.makeText(this, "Please Enter FirstName", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(LastName)){
            Toast.makeText(this, "Please Enter LastName", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Biography)){
            Toast.makeText(this, "Please Enter Biography", Toast.LENGTH_SHORT).show();
        }else{
            storeInformation();
        }

    }

    private void storeInformation(){
        loadingBar.setTitle("Adding Profile...");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String,Object> profileMap = new HashMap<>();
        profileMap.put("pid",mAuth.getUid());
        profileMap.put("Website",Website);
        profileMap.put("Title",Title);
        profileMap.put("FirmName",FirmName);
        profileMap.put("LastName",LastName);
        profileMap.put("Email",Email);
        profileMap.put("FirstName",FirstName);
        profileMap.put("Biography",Biography);

        AttorneyRef.child(mAuth.getUid()).updateChildren(profileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(MyProfileActivity.this, AttorneyHomeActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(MyProfileActivity.this, "Profile Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(MyProfileActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
