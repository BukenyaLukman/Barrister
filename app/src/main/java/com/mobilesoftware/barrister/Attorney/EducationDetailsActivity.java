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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EducationDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText SchoolStart,SchoolEnd,School,
            DegreeObtained,Description;
    private String SchoolEntYear,SchoolEndYear,SchoolName,Degree,DescriptionName,saveCurrentDate,saveCurrentTime;
    private Button SaveBtn;
    private ProgressDialog loadingBar;
    private FirebaseUser mAuth;
    private DatabaseReference EducationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details);

        mToolbar  = findViewById(R.id.my_education_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Education Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        EducationRef = FirebaseDatabase.getInstance().getReference().child("Education");
        initializeFields();

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterEducationDetails();
            }
        });
    }

    private void RegisterEducationDetails() {
        SchoolEntYear = SchoolStart.getText().toString();
        SchoolEndYear = SchoolEnd.getText().toString();
        SchoolName = School.getText().toString();
        Degree = DegreeObtained.getText().toString();
        DescriptionName = Description.getText().toString();

        if (TextUtils.isEmpty(SchoolEntYear)){
            Toast.makeText(this, "Please Enter Year of School Entry", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(SchoolEndYear)){
            Toast.makeText(this, "Please Enter Year of Completion", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(SchoolName)){
            Toast.makeText(this, "Please Enter School Name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Degree)){
            Toast.makeText(this, "Please Enter Year of School Entry", Toast.LENGTH_SHORT).show();
        }else{
            uploadInfoToStorage();
        }
    }

    private void uploadInfoToStorage() {
        loadingBar.setTitle("Uploading...");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        HashMap<String,Object> eduMap = new HashMap<>();
        eduMap.put("SchoolEntryYear",SchoolEntYear);
        eduMap.put("SchoolEndYear",SchoolEndYear);
        eduMap.put("SchoolName",SchoolName);
        eduMap.put("Degree",Degree);
        eduMap.put("Description",DescriptionName);

        EducationRef.child(mAuth.getUid()).updateChildren(eduMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(EducationDetailsActivity.this, "Education Details Uploaded", Toast.LENGTH_SHORT).show();
                            gotoHomeActivity();
                        }else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(EducationDetailsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void gotoHomeActivity(){
        Intent intent = new Intent(EducationDetailsActivity.this, AttorneyHomeActivity.class);
        startActivity(intent);
    }


    private void initializeFields() {
        SchoolStart = findViewById(R.id.start_of_school_year_input);
        SchoolEnd = findViewById(R.id.end_of_school_year_input);
        School = findViewById(R.id.school_input_area);
        DegreeObtained = findViewById(R.id.degree_obtained_input);
        Description = findViewById(R.id.degree_description_optional);
        SaveBtn = findViewById(R.id.save_info_btn);
        loadingBar = new ProgressDialog(this);
    }
}
