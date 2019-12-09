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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class ClientRegisterActivity extends AppCompatActivity {

    private EditText FirstName,SecondName,Email,Password;
    private Toolbar mToolbar;
    private Button SignUpBtn;
    private TextView AlreadyHaveAccount;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.client_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Client Sign Up");

        initializeFields();


        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToClientLoginActivity();
            }
        });


        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
                //goToClientActivity();

            }
        });
    }

    private void CreateAccount() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String firstName = FirstName.getText().toString();
        String lastName = SecondName.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Email...", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
        }else
            {

            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ClientRegisterActivity.this, "Account Created Successful...", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            goToClientActivity();
                        }else{
                            String message = task.getException().toString();
                            Toast.makeText(ClientRegisterActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private void GoToClientLoginActivity(){
        Intent LoginIntent = new Intent(ClientRegisterActivity.this,ClientLoginActivity.class);
        startActivity(LoginIntent);
    }

    private void goToClientActivity() {
        Intent LoginIntent = new Intent(ClientRegisterActivity.this,ClientHomeActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
    }

    private void initializeFields() {
        FirstName = findViewById(R.id.client_first_name);
        SecondName = findViewById(R.id.client_second_name);
        Email = findViewById(R.id.client_email);
        Password = findViewById(R.id.client_password);
        SignUpBtn = findViewById(R.id.client_sign_up_btn);
        AlreadyHaveAccount = findViewById(R.id.client_already_have_account);
        progressDialog = new ProgressDialog(this);
    }
}
