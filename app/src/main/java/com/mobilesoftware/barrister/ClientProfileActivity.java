package com.mobilesoftware.barrister;

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
import com.mobilesoftware.barrister.Attorney.AttorneyHomeActivity;
import com.mobilesoftware.barrister.Attorney.MyProfileActivity;

import java.util.HashMap;

public class ClientProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText ClientFirstName,ClientLastName,
            ClientTitle,ClientEmail,ClientProfile;

    private String FirstName,LastName,Title,Email,Profile;
    private Button SubmitBtn;
    private ProgressDialog loadingBar;
    private FirebaseUser mAuth;
    private DatabaseReference ClientRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        mToolbar = findViewById(R.id.client_profile_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ClientRef = FirebaseDatabase.getInstance().getReference().child("ClientPersonalData");

        initializeFields();

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInfo();
            }
        });
    }



    private void initializeFields()
    {
        ClientFirstName = findViewById(R.id.client_profile_first_name);
        ClientLastName = findViewById(R.id.client_profile_last_name);
        ClientTitle = findViewById(R.id.client_profile_title);
        ClientEmail = findViewById(R.id.client_profile_email);
        ClientProfile = findViewById(R.id.client_profile);
        SubmitBtn = findViewById(R.id.client_profile_submit);
        loadingBar = new ProgressDialog(this);

    }

    private void uploadInfo() {
        FirstName = ClientFirstName.getText().toString();
        LastName = ClientLastName.getText().toString();
        Title = ClientTitle.getText().toString();
        Email = ClientEmail.getText().toString();
        Profile = ClientProfile.getText().toString();


        if (TextUtils.isEmpty(FirstName)){
            Toast.makeText(this, "Please write First Name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(LastName)){
            Toast.makeText(this, "Please write Last Name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Title)){
            Toast.makeText(this, "Please write  Title", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Please write Email", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Profile)){
            Toast.makeText(this, "Please write Your Profile", Toast.LENGTH_SHORT).show();
        }else {
            loadInfoToDatabase();
        }
    }

    private void loadInfoToDatabase() {
        loadingBar.setTitle("Adding Profile...");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String,Object> clientMap = new HashMap<>();
        clientMap.put("FirstName",FirstName);
        clientMap.put("LastName",LastName);
        clientMap.put("Title",Title);
        clientMap.put("Email",Email);
        clientMap.put("Profile",Profile);

        ClientRef.child(mAuth.getUid()).updateChildren(clientMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(ClientProfileActivity.this, ClientHomeActivity.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(ClientProfileActivity.this, "Profile Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(ClientProfileActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
