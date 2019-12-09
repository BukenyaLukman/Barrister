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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobilesoftware.barrister.R;

import java.util.HashMap;


public class AttorneyRegisterActivity extends AppCompatActivity {

    private EditText FirstName,SecondName,Email,Password;
    private Button SignUpBtn;
    private Toolbar mToolbar;
    private String firstName,lastName,password,email,AttorneyID;
    private TextView AlreadyHaveAccount;
    private DatabaseReference AttorneyDataRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_attorney_register);
        mToolbar = findViewById(R.id.attorney_appbar);


        AttorneyDataRef = FirebaseDatabase.getInstance().getReference().child("AttorneyData");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Attorney Sign Up");

        mAuth = FirebaseAuth.getInstance();


        initializeFields();

        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToAttorneyLoginActivity();
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               CreateAccount();
                //goToAttorneyActivity();
            }
        });
    }

    private void sendUserToAttorneyLoginActivity(){
        Intent loginIntent = new Intent(AttorneyRegisterActivity.this, AttorneyLoginActivity.class);
        startActivity(loginIntent);
    }

    private void CreateAccount() {
        email = Email.getText().toString();
        password = Password.getText().toString();
        firstName = FirstName.getText().toString();
        lastName = SecondName.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
        }else{

            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AttorneyID = firstName;

            saveInfoToDataBase();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AttorneyRegisterActivity.this, "Account Created Successful...", Toast.LENGTH_SHORT).show();
                        //goToAttorneyActivity();
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(AttorneyRegisterActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void saveInfoToDataBase(){
        HashMap<String,Object> dataMap = new HashMap<>();
        dataMap.put("FirstName",firstName);
        dataMap.put("LastName",lastName);
        dataMap.put("Email",email);
        dataMap.put("Password",password);

        AttorneyDataRef.child(AttorneyID).updateChildren(dataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                           goToAttorneyActivity();
                        }else{
                            progressDialog.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AttorneyRegisterActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToAttorneyActivity() {
        Intent LoginIntent = new Intent(AttorneyRegisterActivity.this, AttorneyHomeActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }

    private void initializeFields() {
        FirstName = findViewById(R.id.attorney_first_name);
        SecondName = findViewById(R.id.attorney_second_name);
        Email = findViewById(R.id.attorney_email);
        Password = findViewById(R.id.attorney_password);
        SignUpBtn = findViewById(R.id.attorney_sign_up_btn);
        AlreadyHaveAccount = findViewById(R.id.attorney_already_have_account);

        progressDialog = new ProgressDialog(this);
    }
}
