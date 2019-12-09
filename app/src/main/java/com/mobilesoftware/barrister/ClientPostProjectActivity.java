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

import java.util.HashMap;

public class ClientPostProjectActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText ClientAreaLaw,ClientAreaPractice,ClientTitle,ClientLegalNeed,NameOfClient;
    private String AreaOfLaw,AreaOfPractice,Title,LegalNeed,NameClient;
    private Button PostBtn;

    private ProgressDialog loadingBar;
    private FirebaseUser mAuth;
    private DatabaseReference ClientLegalNeedRef;
    private DatabaseReference LegalNeedRef;
    private String AttorneyID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_post_project);

        AttorneyID = getIntent().getStringExtra("pid");


        mToolbar = findViewById(R.id.client_post_legal_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Post Your Legal Needs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        ClientLegalNeedRef = FirebaseDatabase.getInstance().getReference().child("ClientLegalNeedsTarget");
        LegalNeedRef = FirebaseDatabase.getInstance().getReference().child("ClientLegalNeeds");

        initializeFields();



        PostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AttorneyID != null){
                    getAttorney();
                }else{
                    uploadInfo();
                }
            }
        });

    }

    private void getAttorney() {
        AreaOfLaw = ClientAreaLaw.getText().toString();
        AreaOfPractice = ClientAreaPractice.getText().toString();
        Title = ClientTitle.getText().toString();
        LegalNeed = ClientLegalNeed.getText().toString();
        NameClient = NameOfClient.getText().toString();

        if (TextUtils.isEmpty(AreaOfLaw)){
            Toast.makeText(this, "Please write the Area of Law Interested", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(AreaOfPractice)){
            Toast.makeText(this, "Please write the interested Area of Practice", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Title)){
            Toast.makeText(this, "Please write Title of Your Legal Needs", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(LegalNeed)){
            Toast.makeText(this, "Please write Description of Legal Needs", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(NameClient)){
            Toast.makeText(this, "Please Enter Client's Name", Toast.LENGTH_SHORT).show();
        }else{
            loadInfoToDatabaseWithID();
        }
    }

    private void loadInfoToDatabaseWithID() {
        loadingBar.setTitle("Adding Profile...");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String,Object> legalNeedsMap = new HashMap<>();
        legalNeedsMap.put("AreaOfLaw",AreaOfLaw);
        legalNeedsMap.put("AreaOfPractice",AreaOfPractice);
        legalNeedsMap.put("TitleOfLegal",Title);
        legalNeedsMap.put("LegalNeed",LegalNeed);
        legalNeedsMap.put("NameOfClient",NameClient);


        ClientLegalNeedRef.child(AttorneyID).updateChildren(legalNeedsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(ClientPostProjectActivity.this, ClientHomeActivity.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(ClientPostProjectActivity.this, "Profile Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(ClientPostProjectActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void uploadInfo() {
        AreaOfLaw = ClientAreaLaw.getText().toString();
        AreaOfPractice = ClientAreaPractice.getText().toString();
        Title = ClientTitle.getText().toString();
        LegalNeed = ClientLegalNeed.getText().toString();
        NameClient = NameOfClient.getText().toString();

        if (TextUtils.isEmpty(AreaOfLaw)){
            Toast.makeText(this, "Please write the Area of Law Interested", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(AreaOfPractice)){
            Toast.makeText(this, "Please write the interested Area of Practice", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Title)){
            Toast.makeText(this, "Please write Title of Your Legal Needs", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(LegalNeed)){
            Toast.makeText(this, "Please write Description of Legal Needs", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(NameClient)){
            Toast.makeText(this, "Please Enter Client's Name", Toast.LENGTH_SHORT).show();
        }else{
            loadInfoToDatabase();
        }

    }

    private void loadInfoToDatabase() {
        loadingBar.setTitle("Adding Profile...");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        HashMap<String,Object> legalNeedsMap = new HashMap<>();
        legalNeedsMap.put("AreaOfLaw",AreaOfLaw);
        legalNeedsMap.put("AreaOfPractice",AreaOfPractice);
        legalNeedsMap.put("TitleOfLegal",Title);
        legalNeedsMap.put("LegalNeed",LegalNeed);
        legalNeedsMap.put("NameOfClient",NameClient);

        LegalNeedRef.child(mAuth.getUid()).updateChildren(legalNeedsMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(ClientPostProjectActivity.this, ClientHomeActivity.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(ClientPostProjectActivity.this, "Profile Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }else{
                    String message = task.getException().toString();
                    Toast.makeText(ClientPostProjectActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeFields() {
        ClientAreaLaw = findViewById(R.id.client_area_of_law);
        ClientAreaPractice = findViewById(R.id.client_area_of_practice);
        ClientTitle = findViewById(R.id.client_title);
        ClientLegalNeed = findViewById(R.id.client_legal_need_description);
        PostBtn = findViewById(R.id.client_save_info_btn);
        NameOfClient = findViewById(R.id.client_name_project);
        loadingBar = new ProgressDialog(this);
    }
}
