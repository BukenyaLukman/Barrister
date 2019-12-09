package com.mobilesoftware.barrister.Attorney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobilesoftware.barrister.R;

import java.util.HashMap;

public class MyProjectsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText AreaOfLaw,AreaOfPractice,CityOfOperation;
    private String LawInput,PracticeInput,CityInPut;
    private Button SaveButton;
    private ProgressDialog loadingBar;
    private DatabaseReference ProjectsRef;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects);
        FirebaseApp.initializeApp(this);
        mToolbar = findViewById(R.id.my_projects_app_bar);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ProjectsRef = FirebaseDatabase.getInstance().getReference().child("AttorneyData");



        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Projects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeFields();

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });
    }

    private void validateInfo(){
        LawInput = AreaOfLaw.getText().toString();
        PracticeInput = AreaOfPractice.getText().toString();
        CityInPut = CityOfOperation.getText().toString();

        if (TextUtils.isEmpty(LawInput)){
            Toast.makeText(this, "Please Fill the Area of Law", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(PracticeInput)){
            Toast.makeText(this, "Provide the area of Practice", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(CityInPut)){
            Toast.makeText(this, "Provide the City of Operation", Toast.LENGTH_SHORT).show();
        }else{
            UploadInfo();
        }

    }

    private void UploadInfo() {
        loadingBar.setTitle("Uploading...");
        loadingBar.setMessage("Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        saveInfoToDatabase();
    }

    private void saveInfoToDatabase() {
        HashMap<String,Object> projectMap = new HashMap<>();
        projectMap.put("AreaOfLaw",LawInput);
        projectMap.put("AreaOfPractice",PracticeInput);
        projectMap.put("CityOfOperation",CityInPut);

        ProjectsRef.child(mUser.getUid()).updateChildren(projectMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           loadingBar.dismiss();
                           Toast.makeText(MyProjectsActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                           AreaOfLaw.setText("");
                           AreaOfPractice.setText("");
                           CityOfOperation.setText("");
                       }else{
                           String message = task.getException().toString();
                           Toast.makeText(MyProjectsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }

    private void initializeFields() {
        AreaOfLaw = findViewById(R.id.area_of_law_input);
        AreaOfPractice = findViewById(R.id.area_of_practice_input);
        CityOfOperation = findViewById(R.id.city_of_operation_input);
        SaveButton = findViewById(R.id.search_projects);
        loadingBar = new ProgressDialog(this);
    }
}
